package discord.bot.omegaloli.listener.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if (event.getModalId().equals("create-event"))
            eventBuilder(event);

        if (event.getModalId().equals("create-change-log"))
            changeLogBuilder(event);
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

            EmbedBuilder eventBuilder = new EmbedBuilder();
            eventBuilder.setColor(Color.decode("#9400D3"));

            eventBuilder.setTitle(userName + " создаёт событие!");
            eventBuilder.addField("Тема:", name, false);
            eventBuilder.addField("Дата начала:", date, false);
            eventBuilder.addField("Описание:", description, false);

            event.replyEmbeds(eventBuilder.build()).queue();
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

            EmbedBuilder changeLogBuilder = new EmbedBuilder();
            changeLogBuilder.setColor(Color.decode("#9400D3"));

            changeLogBuilder.setTitle("Список изменений");
            changeLogBuilder.addField("Версия:", version, true);
            changeLogBuilder.addField("Автор:", author, true);
            changeLogBuilder.addField("Список изменений:", changes, false);
            changeLogBuilder.addBlankField(false);

            changeLogBuilder.setFooter("Тыкай по синему тексту, чтобы перейти на GitHub проекта");
            changeLogBuilder.setUrl("https://github.com/kostet72/DiscordBot");

            event.replyEmbeds(changeLogBuilder.build()).queue();
        }
    }
}
