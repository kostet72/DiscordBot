package discord.bot.omegaloli.player;

import com.sedmelluq.discord.lavaplayer.track.*;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;

import lombok.*;

import java.util.concurrent.*;

@Getter
@RequiredArgsConstructor
public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {

        if (endReason.mayStartNext)
            playNextTrack();
    }

    public void playNextTrack() {
        player.startTrack(queue.poll(), false);
    }

    public void queue(AudioTrack track) {

        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
    }
}
