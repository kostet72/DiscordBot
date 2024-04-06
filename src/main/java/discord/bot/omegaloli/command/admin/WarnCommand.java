package discord.bot.omegaloli.command.admin;

import discord.bot.omegaloli.constant.*;
import discord.bot.omegaloli.model.entity.BotUser;
import discord.bot.omegaloli.service.BotUserService;
import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class WarnCommand implements CommandInterface {

    private final BotUserService userService;

    @Override
    public String getName() {
        return "warn";
    }

    @Override
    public String getDescription() {
        return "Выдать пользователю предупреждение";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.USER, "пользователь", "Выберите пользователя", true));

        return data;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        Member userThatWarn = event.getMember();
        Member userToWarn = event.getOption("пользователь").getAsMember();
        MessageChannel channel = event.getGuild().getTextChannelById(ChannelId.GLOBAL_CHANNEL);

        if (userToWarn != null && userThatWarn != null && channel != null
                && Boolean.TRUE.equals(userService.checkUserRegistry(userToWarn.getIdLong()))) {

            BotUser botUser = userService.getUserInfo(userToWarn.getIdLong());

            if (userThatWarn.hasPermission(Permission.BAN_MEMBERS) && botUser.getWarnsCount() >= 2) {

                userService.banUser(userToWarn.getIdLong());
                userToWarn.ban(7, TimeUnit.DAYS).queue();
                channel.sendMessage(userToWarn.getAsMention() + " был забанен за получений трёх предупреждений").queue();
                event.reply("Пользователь был заблокирован").setEphemeral(true).queue();
            }
            else {
                if (userThatWarn.hasPermission(Permission.MODERATE_MEMBERS) && botUser.getWarnsCount() < 3) {

                    userService.warnUser(botUser.getId());
                    String warnsCount = String.valueOf(botUser.getWarnsCount() + 1);
                    channel.sendMessage(userToWarn.getAsMention() + " получает " + warnsCount + "-ое предупреждение. " +
                            "Когда их число достигнет трёх, он будет забанен").queue();
                    event.reply("Пользователь получил предупреждение").setEphemeral(true).queue();
                }
                else event.reply(TextMessage.DO_NOT_HAVE_PERMISSION_EXCEPTION).setEphemeral(true).queue();
            }
        }
        else event.reply(TextMessage.USER_NOT_FOUND_EXCEPTION).setEphemeral(true).queue();
    }
}
