package jda.bot.defect.command.embed;

import jda.bot.defect.command.CommandInterface;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.components.text.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class EmbedCommand implements CommandInterface {

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getDescription() {
        return "Создать embed";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        if (event.getMember().hasPermission(Permission.MESSAGE_MENTION_EVERYONE)) {

            TextInput title = (
                    TextInput.create("title",
                                    "Заголовок",
                                    TextInputStyle.SHORT)
                            .setRequired(true)
                            .setPlaceholder("Заголовок")
                            .build()
            );

            TextInput titleURL = (
                    TextInput.create("titleURL",
                                    "Ссылка на заголовок",
                                    TextInputStyle.SHORT)
                            .setRequired(false)
                            .setPlaceholder("Ссылка при нажатии на заголовок")
                            .build()
            );

            TextInput description = (
                    TextInput.create("description",
                                    "Описание",
                                    TextInputStyle.PARAGRAPH)
                            .setRequired(false)
                            .setPlaceholder("Описание")
                            .build()
            );

            TextInput imageURL = (
                    TextInput.create("imageURL",
                                    "Превью",
                                    TextInputStyle.SHORT)
                            .setRequired(false)
                            .setPlaceholder("Картинка в начале сообщения (ссылка)")
                            .build()
            );

            TextInput footer = (
                    TextInput.create("footer",
                                    "Примечание",
                                    TextInputStyle.PARAGRAPH)
                            .setRequired(false)
                            .setPlaceholder("Примечание")
                            .build()
            );

            Modal embed = (
                    Modal.create("embed", "Создать embed")
                            .setTitle("Заполните поля ниже")
                            .addActionRow(title)
                            .addActionRow(titleURL)
                            .addActionRow(description)
                            .addActionRow(imageURL)
                            .addActionRow(footer)
                            .build()
                    );

            event.replyModal(embed).queue();
        }
        else event.reply("Недостаточно прав").setEphemeral(true).queue();
    }
}
