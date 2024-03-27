package discord.bot.omegaloli.command.music;

import discord.bot.omegaloli.constant.ChannelId;
import discord.bot.omegaloli.player.PlayerManager;
import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.net.*;
import java.util.List;
import java.util.ArrayList;

public class PlayCommand implements CommandInterface {

    @Override
    public String genName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Воспроизвести трек или видео в аудио формате, указав промт";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "промт", "Добавьте название/год выхода/автора или другую информацию, " +
                "по которой можно отыскать аудио на ютубе", true));

        return data;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        TextChannel channel = event.getGuild().getTextChannelById(ChannelId.MUSIC_CHANNEL);
        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        GuildVoiceState selfVoiceState = event.getGuild().getSelfMember().getVoiceState();

        String link = String.join(" ", event.getOption("промт").getAsString());

        if (!isUrl(link))
            link = "ytsearch:" + link + " audio";

        if (memberVoiceState != null && !memberVoiceState.inAudioChannel()) {
            event.reply("Ты должен находиться в голосовом канале, чтобы использовать эту команду").setEphemeral(true).queue();
            return;
        }

        if (selfVoiceState != null && !selfVoiceState.inAudioChannel()) {

            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel voiceChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

            audioManager.openAudioConnection(voiceChannel);
        }

        PlayerManager.getInstance().play(channel, link);
    }

    public boolean isUrl(String link) {

        try {
            new URI(link);
            return true;
        }
        catch (URISyntaxException e) {
            return false;
        }
    }
}
