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
        return Mono.justOrEmpty(event.getInteraction().getMember())
                .flatMap(member ->
                        member.getRoles()
                                .filter(role -> role.getName().equals("Fondateur"))
                                .hasElements()
                                .flatMap(isAdmin -> {
                                    if (!isAdmin) {
                                        return event.reply("Vous n'avez pas la permission d'utiliser cette commande.");
                                    }

                                    EmbedCreateSpec embed = EmbedCreateSpec.builder()
                                            .title("Contacter le staff")
                                            .description("Toute ouverture abusive d'un ticket sera sanctionnée.")
                                            .build();

                                    return event.reply(
                                            InteractionApplicationCommandCallbackSpec.builder()
                                                    .addEmbed(embed)
                                                    .addComponent(ActionRow.of(
                                                            Button.primary("ticket", "Ouvrir un ticket")
                                                    ))
                                                    .build()
                                    );
                                })
                )
                .switchIfEmpty(event.reply("Impossible de récupérer le membre."));
    }
}
