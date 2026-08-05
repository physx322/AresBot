package org.ares;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.rest.RestClient;
import org.ares.commands.CommandRegistry;
import org.ares.listeners.ListenerRegistry;
import org.ares.listeners.tickets.TicketCloseListener;
import org.ares.listeners.tickets.TicketListener;

import static org.ares.listeners.tickets.TicketListener.CreateTicketSalon;

public class Main {

    public static void main(String[] args) {
        DiscordClient client = DiscordClient.create(System.getenv("BOT_TOKEN")); // Créez une variable d'environement au lancement appelé "BOT_TOKEN".
        GatewayDiscordClient gateway = client.login().block();

        // Listeners des events bouton et autres.
        gateway.on(ButtonInteractionEvent.class, event ->
                switch (event.getCustomId()) {
                    case "main" -> event.reply("Bouton actionné").withEphemeral(true);
                    case "ticket" -> CreateTicketSalon(event);
                    case "close_ticket" -> TicketCloseListener.CloseTicketRequest(event);
                    default -> event.reply("Action inconnue").withEphemeral(true);
                }).subscribe();

        gateway.on(TextChannelCreateEvent.class)
                .flatMap(TicketListener::sendTicketDashboard)
                .subscribe();

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
