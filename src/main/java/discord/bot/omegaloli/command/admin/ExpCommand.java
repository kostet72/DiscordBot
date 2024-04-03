package discord.bot.omegaloli.command.admin;

import discord.bot.omegaloli.constant.TextMessage;
import discord.bot.omegaloli.service.BotUserService;
import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class ExpCommand implements CommandInterface {

    private BotUserService userService;

    @Override
    public String getName() {
        return "exp";
    }

    @Override
    public String getDescription() {
        return "Добавить опыта пользователю";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.USER, "пользователь", "Выберите пользователя", true));
        data.add(new OptionData(OptionType.INTEGER, "опыт", "Введите кол-во опыта", true));

        return data;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        Member userToGiveExp = event.getOption("пользователь").getAsMember();
        Integer experience = event.getOption("опыт").getAsInt();

        if (userToGiveExp != null && event.getMember().hasPermission(Permission.BAN_MEMBERS)
                && Boolean.TRUE.equals(userService.checkUserRegistry(userToGiveExp.getIdLong()))) {

            userService.updateExperienceAndSetLevel(userToGiveExp.getIdLong(), event.getMember(), event.getGuild(), experience);
            event.reply("Опыт успешно добавлен пользователю").setEphemeral(true).queue();
        }
        else event.reply(TextMessage.USER_NOT_FOUND_EXCEPTION).setEphemeral(true).queue();
    }
}
