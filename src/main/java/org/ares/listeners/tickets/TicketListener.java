package org.ares.listeners.tickets;

import discord4j.common.util.Snowflake;
import discord4j.core.event.domain.channel.TextChannelCreateEvent;
import discord4j.core.event.domain.interaction.ButtonInteractionEvent;
import discord4j.core.object.PermissionOverwrite;
import discord4j.core.object.component.*;
import discord4j.core.object.entity.Guild;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.MessageCreateSpec;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public class TicketListener {
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

    public static Mono<Void> sendTicketDashboard(TextChannelCreateEvent event) {
        List<LayoutComponent> components = List.of(
                ActionRow.of(
                        Button.danger("close_ticket", "Fermer le ticket"),
                        Button.secondary("claim_ticket", "Prendre en charge")
                )
        );


        if (event.getChannel().getName().startsWith("ticket-")) {
            return event.getChannel().createMessage(MessageCreateSpec.builder()
                            .addAllComponents(components)
                            .addEmbed(EmbedCreateSpec.builder()
                                    .title("Bienvenue sur votre ticket")
                                    .description("Un membre du staff va prendre votre ticket en charge")
                                    .build())
                            .build())
                    .then();
        }
        return Mono.empty();
    }
}