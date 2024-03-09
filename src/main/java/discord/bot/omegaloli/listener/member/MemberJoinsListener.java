package discord.bot.omegaloli.listener.member;

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
        MessageChannel channel = event.getGuild().getTextChannelById(1216054248253427912L);

        if (event.getUser().isBot()) return;

        if (Boolean.TRUE.equals(userService.findUser(user.getIdLong()))) {

            channel.sendMessage("С возвращением, " + user.getName() + "! Рады снова тебя видеть").queue();
        }
        else if (Boolean.FALSE.equals(userService.findUser(user.getIdLong()))) {
            userService.registerUser(user);
            channel.sendMessage("Добро пожаловать, " + user.getName() + "!").queue();
        }
    }
}
