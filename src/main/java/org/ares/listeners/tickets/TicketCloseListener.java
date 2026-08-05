package org.ares.listeners.tickets;

import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Mono;

public class TicketCloseListener {

    public static Mono<Void> CloseTicketRequest(ButtonInteractionEvent event) {
        if (!event.getCustomId().equals("close_ticket")) {
            return Mono.empty();
        }
        return event.getInteraction().getMember()
                .map(member -> member.getBasePermissions())
                .orElse(Mono.empty())
                .flatMap(permissions -> {
                    if (permissions.contains(Permission.ADMINISTRATOR)) {
                        return event.deferReply().withEphemeral(true)
                                .then(event.getInteraction().getChannel())
                                .flatMap(messageChannel -> messageChannel.delete(
                                        "Ticket fermé par " + event.getInteraction().getUser().getUsername()))
                                .then();
                    }
                    return event.reply("Vous n'avez pas la permission de faire cela").withEphemeral(true);
                });
    }
}