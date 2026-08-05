package org.ares.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

public class PingCommand implements ISlashCommand {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public ApplicationCommandRequest getRequest() {
        return ApplicationCommandRequest.builder()
                .name("ping")
                .description("Renvoie un pong")
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        return event.reply("Pong !")
                .withEphemeral(true)
                .withComponents(ActionRow.of(
                        Button.primary("main", "Bouton 1")
                ));
    }
}
