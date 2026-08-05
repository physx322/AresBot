package org.ares.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.core.spec.InteractionApplicationCommandCallbackSpec;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.Color;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class StackCommand implements ISlashCommand {
    @Override
    public String getName() {
        return "stack";
    }

    @Override
    public ApplicationCommandRequest getRequest() {
        return ApplicationCommandRequest.builder()
                .name("stack")
                .description("Renvoie la stack utilisé pour le bot")
                .build();
    }

    @Override
    public Mono<Void> handle(ChatInputInteractionEvent event) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .title("Stack de developpement ClicBot")
                .description("Découvre les outils que nous utilisons pour développé le bot **ClicBot**")
                .addField("Language de programmation", "Java", true)
                .addField("Librairies", "Discord4J\nLog4j", true)
                .addField("CI/CD", "Jenkins", true)
                .addField("Controle de version collaboratif", "GitHub", true)
                .addField("Gestionnaire de version", "Git", true)
                .addField("IDE", "IntelliJ IDEA", true)
                .addField("Hebergement", "Docker", true)
                .timestamp(Instant.now())
                .color(Color.BLUE)
                .build();

        return event.reply(InteractionApplicationCommandCallbackSpec.builder()
                .addEmbed(embed)
                .build()
                .withEphemeral(true));
    }
}
