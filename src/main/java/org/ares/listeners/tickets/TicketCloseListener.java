package org.ares.listeners.tickets;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import reactor.core.publisher.Mono;

public class TicketCloseListener {

    public static Mono<Void> closeTicketRequest(ButtonInteractionEvent event) {
        if (event.getCustomId().equals("close_ticket")) {
           return Mono.empty();
        }
        return event.getInteraction().getChannel()
                .flatMap(messageChannel -> messageChannel.delete("Ticket fermé par " + event.getInteraction().getUser().getUsername()));
    }
}
