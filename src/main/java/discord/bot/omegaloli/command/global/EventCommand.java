package discord.bot.omegaloli.command.global;

import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.components.text.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class EventCommand implements CommandInterface {

    @Override
    public String getName() {
        return "event";
    }

    @Override
    public String getDescription() {
        return "Создать своё событие, чтобы другие участники могли узнать о вашем мероприятии";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        TextInput eventName = (
                TextInput.create("event-name-field",
                                "Тема",
                                TextInputStyle.PARAGRAPH)
                        .setRequired(true)
                        .setRequiredRange(1, 30)
                        .setPlaceholder("Дайте своему событию тему. Например: \"Совместный просмотр фильма\"")
                        .build()
        );

        TextInput eventDate = (
                TextInput.create("event-date",
                                "Дата начала",
                                TextInputStyle.SHORT)
                        .setRequired(false)
                        .setMaxLength(50)
                        .setPlaceholder("Необязательное поле")
                        .build()
        );

        TextInput eventDescription = (
                TextInput.create("event-description-field",
                                "Описание",
                                TextInputStyle.PARAGRAPH)
                        .setRequired(true)
                        .setMaxLength(500)
                        .setPlaceholder("Опишите суть/место/требования своего события")
                        .build()
        );

        Modal createEvent = (
                Modal.create("create-event",
                                "Создайте своё событие")
                        .addActionRow(eventName)
                        .addActionRow(eventDate)
                        .addActionRow(eventDescription)
                        .build()
        );

        event.replyModal(createEvent).queue();
    }
}
