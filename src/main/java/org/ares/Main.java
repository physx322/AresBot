package org.ares;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import discord4j.core.event.domain.command.ApplicationCommandEvent;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.entity.User;
import discord4j.core.object.presence.ClientActivity;
import discord4j.core.object.presence.ClientPresence;
import discord4j.rest.RestClient;

import org.ares.commands.CommandRegistry;
import org.ares.listeners.ListenerRegistry;
import org.ares.listeners.tickets.TicketCloseListener;
import org.ares.listeners.tickets.TicketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.ares.listeners.tickets.TicketListener.createTicketSalon;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        DiscordClient client = DiscordClient.create(System.getenv("BOT_TOKEN")); // Créez une variable d'environement au lancement appelé "BOT_TOKEN".
        GatewayDiscordClient gateway = client.login().block();


        gateway.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(readyEvent -> {
                    User self = readyEvent.getSelf();
                    logger.info("%s connecté !" + self.getUsername());
                    self.getClient().updatePresence(ClientPresence.online(
                            ClientActivity.streaming("Regarde ClicDroit", "https://twitch.tv/")
                    ));
                });


        // Listeners des events bouton et autres.
        gateway.on(ButtonInteractionEvent.class, event ->
                switch (event.getCustomId()) {
                    case "main" -> event.reply("Bouton actionné").withEphemeral(true);
                    case "ticket" -> createTicketSalon(event);
                    case "close_ticket" -> TicketCloseListener.closeTicketRequest(event);
                    default -> event.reply("Action inconnue").withEphemeral(true);
                }).subscribe();

        gateway.on(TextChannelCreateEvent.class)
                .flatMap(TicketListener::sendTicketDashboard)
                .subscribe();

        // Registery des commandes slash.
        CommandRegistry registry = new CommandRegistry();
        registry.registerToDiscord(RestClient.create(client.getCoreResources().getToken()));
        registry.listenTo(gateway);

        // Registery des différente écoute, MemberJoin etc.
        ListenerRegistry listenerRegistry = new ListenerRegistry();
        listenerRegistry.bindTo(gateway);

        gateway.onDisconnect().block();
    }
}
