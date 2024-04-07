package discord.bot.omegaloli.command.global;

import discord.bot.omegaloli.constant.TextMessage;
import discord.bot.omegaloli.model.entity.BotUser;
import discord.bot.omegaloli.service.BotUserService;
import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import lombok.RequiredArgsConstructor;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class ProfileCommand implements CommandInterface {

    private final BotUserService userService;

    @Override
    public String getName() {
        return "profile";
    }

    @Override
    public String getDescription() {
        return "Получить информацию о профиле пользователя";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> data = new ArrayList<>();
        data.add(
                new OptionData(OptionType.USER, "пользователь", "Выберите пользователя", true)
        );

        return data;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        Member userToCheck = event.getOption("пользователь").getAsMember();

        if (userToCheck != null && Boolean.TRUE.equals(userService.checkUserRegistry(userToCheck.getIdLong()))) {

            event.replyEmbeds(profileBuilder(
                    userService.getUserInfo(userToCheck.getIdLong()), userToCheck)
                    .build()
            ).queue();
        }
        else event.reply(TextMessage.USER_NOT_FOUND_EXCEPTION).setEphemeral(true).queue();
    }

    private static EmbedBuilder profileBuilder(BotUser botUser, Member member) {

        User user = member.getUser();

        return new EmbedBuilder()
                .setColor(Color.decode("#9400D3"))
                .setAuthor(user.getName(), "https://discord.com/users/" + user.getId(), user.getAvatarUrl())
                .addField("Уровень", botUser.getLvl(), true)
                .addField("Опыт", botUser.getExperience().toString(), true)
                .addField("В числе участников", "с " + botUser.getRegistrationDate().format(DateTimeFormatter.ISO_DATE), false);
    }
}
