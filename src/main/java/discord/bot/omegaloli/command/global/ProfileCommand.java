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
                new OptionData(OptionType.STRING, "пользователь", "Введите тег пользователя. " +
                        "Чтобы получить свой профиль, просто оставьте поле пустым", false)
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

            builder = profileBuilder(userService.getUserInfoById(userId));
            event.replyEmbeds(builder.build()).queue();
        }
        else if (name != null && Boolean.TRUE.equals(userService.checkUserRegistry(null, name.getAsString()))) {

            builder = profileBuilder(userService.getUserInfoByName(name.getAsString()));
            event.replyEmbeds(builder.build()).queue();
        }
        else event.reply(TextMessage.USER_NOT_FOUND_MESSAGE).setEphemeral(true).queue();
    }

    private static EmbedBuilder profileBuilder(BotUser botUser) {

        EmbedBuilder profileBuilder = new EmbedBuilder();
        profileBuilder.setColor(Color.decode("#9400D3"));

        profileBuilder.setTitle(botUser.getName());
        profileBuilder.addField("Уровень", botUser.getLvl(), true);
        profileBuilder.addField("Cообщений", botUser.getExperience().toString(), true);
        profileBuilder.addField("В числе участников", "с " + botUser.getRegistrationDate().format(DateTimeFormatter.ISO_DATE), false);

        return profileBuilder;
    }
}
