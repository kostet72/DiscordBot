package discord.bot.omegaloli.listener.user.member;

import discord.bot.omegaloli.constant.ChannelId;
import discord.bot.omegaloli.service.BotUserService;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MemberJoinsListener extends ListenerAdapter {

    private final BotUserService userService;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        User user = event.getUser();
        MessageChannel channel = event.getGuild().getTextChannelById(ChannelId.WELCOME_CHANNEL);

        if (!user.isBot() && channel != null) {

            if (Boolean.TRUE.equals(userService.findUser(user.getIdLong()))) {

                channel.sendMessage("С возвращением, " + user.getAsMention() + "! Рады снова тебя видеть").queue();
            }
            else if (Boolean.FALSE.equals(userService.findUser(user.getIdLong()))) {
                userService.registerUser(user);
                channel.sendMessage("Добро пожаловать, " + user.getAsMention() + "!").queue();
            }
        }
    }
}
