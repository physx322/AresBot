package org.ares.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

public class TicketCommand implements ISlashCommand {
    @Override
    public String getName() {
        return "ticket-create";
    }

    @Override
    public ApplicationCommandRequest getRequest() {
        return ApplicationCommandRequest.builder()
                .name("ticket-create")
                .description("créer le dashboard ticket")
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .title("Contacté le staff")
                .description("Toute ouverture abusive d'un ticket sera sanctionné")
                .build();
        return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                        .addEmbed(embed)
                        .build().withComponents(ActionRow.of(
                                Button.primary("ticket", "Ouvrir un ticket")
                        )));
    }
}
