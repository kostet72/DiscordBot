package discord.bot.omegaloli.listener.user.member;

import discord.bot.omegaloli.constant.*;
import discord.bot.omegaloli.service.BotUserService;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

@RequiredArgsConstructor
public class MemberJoinsListener extends ListenerAdapter {

    private final BotUserService userService;

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        User user = event.getUser();
        MessageChannel channel = event.getGuild().getTextChannelById(ChannelId.WELCOME_CHANNEL);

        if (!user.isBot() && channel != null) {

            if (Boolean.TRUE.equals(userService.checkUserRegistry(user.getIdLong())))
                channel.sendMessage(user.getAsMention() + " возвращается на сервер! Рады снова тебя видеть").queue();

            else if (Boolean.FALSE.equals(userService.checkUserRegistry(user.getIdLong()))) {

                userService.registerUser(user);

                welcomeBuilder(event, channel,
                        TextMessage.WELCOME_MESSAGE[new Random().nextInt(TextMessage.WELCOME_MESSAGE.length)]);
            }
        }
    }

    public void welcomeBuilder(GuildMemberJoinEvent event, MessageChannel channel, String title) {

        User user = event.getUser();

        channel.sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.decode("#9400D3"))
                        .setAuthor(user.getName(), "https://discord.com/users/" + user.getId(), user.getAvatarUrl())
                        .setTitle(title)
                        .setDescription("Добро пожаловать! Рады тебя видеть")
                        .build()
        ).queue();
    }
}
