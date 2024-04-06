package discord.bot.omegaloli.listener.command;

import discord.bot.omegaloli.service.BotUserService;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

@RequiredArgsConstructor
public class ModalListener extends ListenerAdapter {

    private final BotUserService userService;

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if (event.getModalId().equals("create-event"))
            eventBuilder(event);

        if (event.getModalId().equals("create-change-log"))
            changeLogBuilder(event);

        userService.updateExperienceAndSetLevel(event.getUser().getIdLong(), event.getMember(), event.getGuild(), 3);
    }

    public void eventBuilder(ModalInteractionEvent event) {

        ModalMapping nameValue = event.getValue("event-name-field");
        ModalMapping dateValue = event.getValue("event-date");
        ModalMapping descriptionValue = event.getValue("event-description-field");

        String userName = event.getUser().getName();

        if (nameValue != null && dateValue != null && descriptionValue != null) {

            String name = nameValue.getAsString();
            String date = dateValue.getAsString();
            if (!date.isEmpty()) date = dateValue.getAsString();
            else date = "Уточните у организатора";
            String description = descriptionValue.getAsString();

            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.decode("#9400D3"))
                            .setTitle(userName + " создаёт событие!")
                            .addField("Тема:", name, false)
                            .addField("Дата начала:", date, false)
                            .addField("Описание:", description, false)
                            .build()
            ).queue();
        }
    }

    public void changeLogBuilder(ModalInteractionEvent event) {

        ModalMapping versionValue = event.getValue("change-log-version");
        ModalMapping authorValue = event.getValue("change-log-author");
        ModalMapping changesValue = event.getValue("change-log-list");

        if (versionValue != null && authorValue != null && changesValue != null) {

            String version = versionValue.getAsString();
            String author = authorValue.getAsString();
            String changes = changesValue.getAsString();

            event.replyEmbeds(
                    new EmbedBuilder()
                            .setColor(Color.decode("#9400D3"))
                            .setTitle("Список изменений")
                            .addField("Версия:", version, true)
                            .addField("Автор:", author, true)
                            .addField("Список изменений:", changes, false)
                            .addBlankField(false)
                            .setFooter("Тыкай по синему тексту, чтобы перейти на GitHub проекта")
                            .setUrl("https://github.com/kostet72/DiscordBot")
                            .build()
            ).queue();
        }
    }
}
