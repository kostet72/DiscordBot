package discord.bot.omegaloli.player;

import com.sedmelluq.discord.lavaplayer.player.*;

import lombok.Getter;

@Getter
public class MusicManager {

    private final AudioPlayer player;
    private final TrackScheduler trackScheduler;
    private final AudioForwarder audioForwarder;

    public MusicManager(AudioPlayerManager manager) {

        player = manager.createPlayer();
        trackScheduler = new TrackScheduler(player);
        player.addListener(trackScheduler);
        audioForwarder = new AudioForwarder(player);
    }
}
