package org.clicdroit.listeners;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.entity.Guild;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;

import java.util.Set;

public class TIcketListener {
    public static Mono<Void> CreateTicketSalon(ButtonInteractionEvent event) {
        if (!event.getCustomId().equals("ticket")) {
            return Mono.empty();
        }
        Guild guild = event.getInteraction().getGuild().block();
        var categoryId = 1534193855363289159L;
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
