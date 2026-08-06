package org.ares.listeners.tickets;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.entity.PartialMember;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

import java.util.Map;

public class TicketCloseListener {

    public static Mono<Void> closeTicketRequest(ButtonInteractionEvent event) {
        Map<Long, String> myTickets = TicketListener.tickets;
        return event.getInteraction().getMember()
                .map(PartialMember::getBasePermissions)
                .orElse(Mono.empty())
                .flatMap(permissions -> {
                    if (permissions.contains(Permission.ADMINISTRATOR)) {
                        return event.deferReply().withEphemeral(true)
                                .then(event.getInteraction().getChannel())
                                .flatMap(messageChannel ->
                                        messageChannel.delete("Ticket fermé par " + event.getInteraction().getUser().getUsername())
                                );
                    }
                    return event.reply("Vous n'avez pas la permission de faire cela").withEphemeral(true);
                });
    }
}