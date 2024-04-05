package discord.bot.omegaloli.listener.rules;

import discord.bot.omegaloli.constant.ChannelId;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import org.jetbrains.annotations.NotNull;

public class RulesUpdateListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {

        User user = event.getAuthor();
        MessageChannel rulesChannel = event.getChannel();
        MessageChannel newsChannel = event.getGuild().getNewsChannelById(ChannelId.NEWS_CHANNEL);
        int length = event.getMessage().getContentRaw().length();

        if (!user.isBot() && newsChannel != null && rulesChannel.getIdLong() == ChannelId.RULES_CHANNEL && length >= 100) {
            newsChannel.sendMessage(user.getAsMention() + " внёс изменения в правила сервера").queue();
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {

        User user = event.getAuthor();
        MessageChannel rulesChannel = event.getChannel();
        MessageChannel newsChannel = event.getGuild().getNewsChannelById(ChannelId.NEWS_CHANNEL);

        if (!user.isBot() && newsChannel != null && rulesChannel.getIdLong() == ChannelId.RULES_CHANNEL) {
            newsChannel.sendMessage(user.getAsMention() + " внёс изменения в правила сервера").queue();
        }
    }
}
