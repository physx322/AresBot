package org.ares.commands;

import discord4j.core.GatewayDiscordClient;
import discord4j.rest.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandRegistry {

    private final Map<String, ISlashCommand> commands = new HashMap<>();

    public CommandRegistry() {
        register(new PingCommand());
        register(new StackCommand());
        register(new TicketCommand());
    }

    private void register(ISlashCommand command) {
        commands.put(command.getName(), command);
    }

    public ISlashCommand getCommand(String name) {
        return commands.get(name);
    }

    public void registerToDiscord(RestClient restClient, long guildId) {
        long applicationId = restClient.getApplicationId().block();

        List<discord4j.discordjson.json.ApplicationCommandRequest> requests = commands.values()
                .stream()
                .map(ISlashCommand::getRequest)
                .toList();

        restClient.getApplicationService()
                .bulkOverwriteGuildApplicationCommand(applicationId, guildId, requests)
                .subscribe();
    }

    public void listenTo(GatewayDiscordClient gateway) {
        gateway.on(discord4j.core.event.domain.interaction.ChatInputInteractionEvent.class, event -> {
            ISlashCommand command = getCommand(event.getCommandName());
            if (command == null) {
                return event.reply("Commande inconnue.");
            }
            return command.handle(event);
        }).subscribe();
    }
}