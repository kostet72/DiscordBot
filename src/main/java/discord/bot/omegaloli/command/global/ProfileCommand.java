package discord.bot.omegaloli.command.global;

import discord.bot.omegaloli.constant.TextMessage;
import discord.bot.omegaloli.model.entity.BotUser;
import discord.bot.omegaloli.service.BotUserService;
import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
public class ProfileCommand implements CommandInterface {

    private final BotUserService userService;

    @Override
    public String genName() {
        return "профиль";
    }

    @Override
    public String getDescription() {
        return "Профиль пользователя";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> data = new ArrayList<>();
        data.add(
                new OptionData(OptionType.STRING, "пользователь", "Введите тег пользователя", false)
        );

        return data;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        EmbedBuilder builder;
        OptionMapping name = event.getOption("пользователь");
        User user = event.getUser();
        Long userId = user.getIdLong();

        if (name == null && Boolean.TRUE.equals(userService.checkUserRegistry(userId, null))) {

            builder = embedBuilder(userService.getUserInfoById(userId));
            event.replyEmbeds(builder.build()).queue();
        }
        else if (name != null && Boolean.TRUE.equals(userService.checkUserRegistry(null, name.getAsString()))) {

            builder = embedBuilder(userService.getUserInfoByName(name.getAsString()));
            event.replyEmbeds(builder.build()).queue();
        }
        else event.reply(TextMessage.USER_NOT_FOUND_MESSAGE).queue();
    }

    @NotNull
    private static EmbedBuilder embedBuilder(BotUser botUser) {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setColor(Color.decode("#9400D3"));

        builder.setTitle(botUser.getName());
        builder.addField("Уровень", botUser.getLvl(), true);
        builder.addField("Cообщений", botUser.getExperience().toString(), true);
        builder.addField("В числе участников", "с " + botUser.getRegistrationDate().format(DateTimeFormatter.ISO_DATE), false);

        return builder;
    }
}
