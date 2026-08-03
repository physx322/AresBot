package org.clicdroit.listeners;

import discord4j.core.event.domain.message.MessageCreateEvent;
import reactor.core.publisher.Mono;

public class MessageListener implements IEventListener<MessageCreateEvent> {

    @Override
    public Class<MessageCreateEvent> getEventType() {
        return MessageCreateEvent.class;
    }

    @Override
    public Mono<Void> execute(MessageCreateEvent event) {
        event.getMessage().getAuthor().ifPresent(author -> {
            if (!author.isBot()) {
                System.out.println("Message: " + event.getMessage().getContent() + " de: " + author.getUsername());
            }
        });
        return Mono.empty();
    }
}
