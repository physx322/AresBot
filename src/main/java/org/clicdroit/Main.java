package org.clicdroit;

import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.rest.RestClient;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.clicdroit.commands.CommandRegistry;
import org.clicdroit.listeners.ListenerRegistry;
import reactor.core.publisher.Mono;

import java.util.Set;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        DiscordClient client = DiscordClient.create(System.getenv("BOT_TOKEN")); // Créez une variable d'environement au lancement appelé "BOT_TOKEN".
        GatewayDiscordClient gateway = client.login().block();

        // Listeners des events bouton et autres.
        gateway.on(ButtonInteractionEvent.class, event ->
                switch (event.getCustomId()) {
                    case "main" -> event.reply("Bouton actionné").withEphemeral(true);
                    case "ticket" -> CreateTicketSalon(event);
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

    public static Mono<Void> CreateTicketSalon(ButtonInteractionEvent event) {
        if (!event.getCustomId().equals("ticket")) {
            return Mono.empty();
        }
        Guild guild = event.getInteraction().getGuild().block();
        var categoryId = 1534193855363289159L; // ID de la catégorie "Tickets"
        assert guild != null;
        return guild.createTextChannel(channel -> {
                    channel.setName("ticket-" + event.getUser().getUsername());
                    channel.setParentId(Snowflake.of(categoryId));
                    channel.setPermissionOverwrites(Set.of(
                            PermissionOverwrite.forRole(
                                    guild.getId(),
                                    PermissionSet.none(),
                                    PermissionSet.of(Permission.VIEW_CHANNEL)
                            ),

                            PermissionOverwrite.forMember(
                                    event.getUser().getId(),
                                    PermissionSet.none(),
                                    PermissionSet.of(Permission.VIEW_CHANNEL, Permission.SEND_MESSAGES)
                            )

                    ));
                })
                .then(event.reply("Salon créé !").withEphemeral(true));
    }
}
