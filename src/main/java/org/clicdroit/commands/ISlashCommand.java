package org.clicdroit.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.discordjson.json.ApplicationCommandRequest;
import reactor.core.publisher.Mono;

public interface ISlashCommand {
    String getName();

    ApplicationCommandRequest getRequest();

    Mono<Void> handle(ChatInputInteractionEvent event);
}
