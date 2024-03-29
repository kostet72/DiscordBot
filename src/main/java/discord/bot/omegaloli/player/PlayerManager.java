package discord.bot.omegaloli.player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import com.sedmelluq.discord.lavaplayer.track.*;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import java.awt.*;
import java.util.Map;
import java.util.List;
import java.util.HashMap;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, MusicManager> guildMusicManager = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    private PlayerManager() {
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static PlayerManager getInstance() {

        if (INSTANCE == null)
            INSTANCE = new PlayerManager();

        return INSTANCE;
    }

    public MusicManager getGuildMusicManager(Guild guild) {

        return guildMusicManager.computeIfAbsent(guild.getIdLong(), (guildId) -> {

            final MusicManager musicManager = new MusicManager(audioPlayerManager);
            guild.getAudioManager().setSendingHandler(musicManager.getAudioForwarder());

            return musicManager;
        });
    }

    public void play(TextChannel textChannel, String url) {

        final MusicManager musicManager = getGuildMusicManager(textChannel.getGuild());

        audioPlayerManager.loadItemOrdered(guildMusicManager, url, new AudioLoadResultHandler() {

            @Override
            public void trackLoaded(AudioTrack audioTrack) {

                musicManager.getTrackScheduler().queue(audioTrack);
                textChannel.sendMessageEmbeds(trackBuilder(audioTrack).build()).addActionRow().queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if (!tracks.isEmpty()) {

                    musicManager.getTrackScheduler().queue(tracks.get(0));
                    textChannel.sendMessageEmbeds(trackBuilder(tracks.get(0)).build()).queue();
                }
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

    private static EmbedBuilder trackBuilder(AudioTrack track) {

        long durationInSeconds = track.getInfo().length / 1000;
        String duration = String.format("%02d:%02d:%02d", durationInSeconds / 3600, (durationInSeconds % 3600) / 60, durationInSeconds % 60);

        EmbedBuilder trackBuilder = new EmbedBuilder();
        trackBuilder.setColor(Color.decode("#9400D3"));

        trackBuilder.setTitle("Добавляю в очередь:   **" + track.getInfo().title + "**");
        trackBuilder.addField("Автор:", track.getInfo().author, true);
        trackBuilder.addField("Продолжительность:", duration, true);

        trackBuilder.setUrl(track.getInfo().uri);
        trackBuilder.setThumbnail(track.getInfo().artworkUrl);

        return trackBuilder;
    }
}
