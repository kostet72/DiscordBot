package discord.bot.omegaloli.listener.user.experience;

import discord.bot.omegaloli.constant.ChannelId;
import discord.bot.omegaloli.service.BotUserService;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class UserExperienceListener extends ListenerAdapter {

    private final BotUserService userService;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        User user = event.getAuthor();
        MessageChannel channel = event.getGuild().getTextChannelById(ChannelId.GLOBAL_CHANNEL);
        int length = event.getMessage().getContentRaw().length();

        String answer = userService.updateExperienceAndSetLevel(user.getIdLong(), event.getMember(), event.getGuild(), 1);

        if (!user.isBot() && channel != null && length >= 5 && answer != null)
            channel.sendMessage(user.getAsMention() + " достиг " + answer + "-го уровня!").queue();
    }
}
