package discord.bot.omegaloli.player;

import net.dv8tion.jda.api.audio.AudioSendHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.MutableAudioFrame;

import java.nio.ByteBuffer;

public class AudioForwarder implements AudioSendHandler {

    private final AudioPlayer player;
    private final MutableAudioFrame frame = new MutableAudioFrame();
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public AudioForwarder(AudioPlayer player) {
        this.player = player;
        frame.setBuffer(buffer);
    }

    @Override
    public boolean canProvide() {
        return player.provide(frame);
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        buffer.flip();
        return buffer;
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
