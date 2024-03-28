package discord.bot.omegaloli.player;

import net.dv8tion.jda.api.entities.Guild;
import com.sedmelluq.discord.lavaplayer.track.*;
import com.sedmelluq.discord.lavaplayer.player.*;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

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
                textChannel.sendMessage(String.format(
                        "Добавялю в очередь:   **%s**   в исполнении   **%s**",
                        audioTrack.getInfo().title,
                        audioTrack.getInfo().author
                )).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if (!tracks.isEmpty()) {

                    musicManager.getTrackScheduler().queue(tracks.get(0));
                    textChannel.sendMessage(String.format(
                            "Добавялю в очередь:   **%s**   в исполнении   **%s**",
                            tracks.get(0).getInfo().title,
                            tracks.get(0).getInfo().author
                    )).queue();
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
}
