package discord.bot.omegaloli.command.music;

import discord.bot.omegaloli.command.CommandInterface;

import discord.bot.omegaloli.player.MusicManager;
import discord.bot.omegaloli.player.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class SkipCommand implements CommandInterface {

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Перейти к следующему в очереди треку";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        GuildVoiceState memberVoiceState = event.getMember().getVoiceState();
        GuildVoiceState selfVoiceState = event.getGuild().getSelfMember().getVoiceState();

        if (!memberVoiceState.inAudioChannel()) {
            event.reply("Ты должен находиться в голосовом канале, чтобы использовать эту команду").setEphemeral(true).queue();
            return;
        }

        if (!selfVoiceState.inAudioChannel()) {
            event.reply("Сейчас бот не воспроизводит никакой музыки").setEphemeral(true).queue();
            return;
        }

        if (memberVoiceState.getChannel() != selfVoiceState.getChannel()) {
            event.reply("Ты должен находиться в одном голосовом канале с ботом").queue();
            return;
        }

        MusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        musicManager.getTrackScheduler().playNextTrack();

        if (musicManager.getTrackScheduler().getQueue().isEmpty())
            event.reply("Пропускаю трек ...\n Теперь играет последний трек в очереди. Добавьте новый трек в очередь, чтобы продолжить").queue();

        else event.reply("Трек пропущен, включаю следующий ...").queue();
    }
}
