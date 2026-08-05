package org.ares.listeners;

import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

public interface IEventListener<T extends Event> {

    Class<T> getEventType();

    Mono<Void> execute(T event);
}
