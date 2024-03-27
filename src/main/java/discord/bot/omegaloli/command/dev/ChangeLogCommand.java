package discord.bot.omegaloli.command.dev;

import discord.bot.omegaloli.constant.ServerRoleId;
import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class ChangeLogCommand implements CommandInterface {

    @Override
    public String genName() {
        return "changelog";
    }

    @Override
    public String getDescription() {
        return "Рассказать об изменениях в обновлении бота";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        Role developerRole = event.getGuild().getRoleById(ServerRoleId.DEVELOPER_ROLE_ID);

        if (event.getMember().getRoles().contains(developerRole)) {

            TextInput changeLogVersion = (
                    TextInput.create("change-log-version",
                                    "Версия",
                                    TextInputStyle.PARAGRAPH)
                            .setRequired(true)
                            .setRequiredRange(1, 30)
                            .setPlaceholder("Обозначьте изменения версией")
                            .build()
            );

            TextInput changeLogAuthor = (
                    TextInput.create("change-log-author",
                                    "Автор изменений",
                                    TextInputStyle.PARAGRAPH)
                            .setRequired(true)
                            .setRequiredRange(1, 30)
                            .setPlaceholder("Кто работал над этими изменениями?")
                            .build()
            );

            TextInput changeLogList = (
                    TextInput.create("change-log-list",
                                    "Список изменений",
                                    TextInputStyle.PARAGRAPH)
                            .setRequired(true)
                            .setRequiredRange(1, 4000)
                            .setPlaceholder("Что поменялось с прошлой версии?")
                            .build()
            );

            Modal createChangeLog = (
                    Modal.create("create-change-log",
                                    "Показать список изменений")
                            .addActionRow(changeLogVersion)
                            .addActionRow(changeLogAuthor)
                            .addActionRow(changeLogList)
                            .build()
            );

            event.replyModal(createChangeLog).queue();
        }
    }
}
