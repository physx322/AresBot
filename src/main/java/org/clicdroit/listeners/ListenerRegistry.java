package org.clicdroit.listeners;

import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class ListenerRegistry {

    private final List<IEventListener<? extends Event>> listeners = new ArrayList<>();

    public ListenerRegistry() {
        register(new MessageListener());
    }

    private void register(IEventListener<? extends Event> listener) {
        listeners.add(listener);
    }

    public void bindTo(GatewayDiscordClient gateway) {
        for (IEventListener<? extends Event> listener : listeners) {
            bindOne(gateway, listener);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Event> void bindOne(GatewayDiscordClient gateway, IEventListener<T> listener) {
        gateway.on(listener.getEventType())
                .flatMap(event -> listener.execute(event)
                        .onErrorResume(error -> {
                            error.printStackTrace();
                            return Mono.empty();
                        }))
                .subscribe();
    }
}