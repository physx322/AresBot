package org.clicdroit;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.rest.RestClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clicdroit.commands.CommandRegistry;
import org.clicdroit.listeners.ListenerRegistry;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        DiscordClient client = DiscordClient.create(System.getenv("BOT_TOKEN")); // Créez une variable d'environement au lancement appelé "BOT_TOKEN".
        GatewayDiscordClient gateway = client.login().block();

        // Listeners des events bouton et autres.
        gateway.on(ButtonInteractionEvent.class, event -> switch (event.getCustomId()) {
            case "main" -> event.reply("Bouton actionné").withEphemeral(true);
                    default -> event.reply("Action inconnue");
                }).subscribe();

        // Registery des commandes slash.
        CommandRegistry registry = new CommandRegistry();
        registry.registerToDiscord(RestClient.create(client.getCoreResources().getToken()), 1396773019690729524L);
        registry.listenTo(gateway);

        // Registery des différente écoute, MemberJoin etc.
        ListenerRegistry listenerRegistry = new ListenerRegistry();
        listenerRegistry.bindTo(gateway);

        gateway.onDisconnect().block();
    }
}
