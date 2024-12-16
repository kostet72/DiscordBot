package jda.bot.defect.banner;

import jda.bot.defect.config.BotConfig;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

@Component
@RequiredArgsConstructor
public class Banner {

    private final BotConfig config;

    public void updateStats(Guild guild) throws IOException {

        BufferedImage banner = ImageIO.read(new File("src/main/resources/Banner_original.png"));
        Graphics2D g = banner.createGraphics();

        Integer count = 0;
        for (Member member : guild.getMembers()) {
            if (member.getVoiceState().inAudioChannel()) {
                count++;
            }
        }

        int x1 = 845;
        int x2 = 845;
        int y1 = 1240;
        int y2 = 1940;
        g.setFont(new Font("Gilroy", Font.PLAIN, 300));
        g.setColor(Color.decode("#223F9E"));
        g.drawString(String.valueOf(count), x1, y1);
        g.drawString(String.valueOf(guild.getMemberCount()), x2, y2);

        File newBanner = new File("src/main/resources/Banner.png");
        ImageIO.write(banner, "png", newBanner);
    }

    public void updateBanner(JDA jda) throws IOException {

        Guild guild = jda.getGuildById(config.getServerId());
        if (guild != null) {

            updateStats(guild);
            Icon bannerIcon = Icon.from(new File("src/main/resources/Banner.png"));

            guild.getManager().setBanner(bannerIcon).queue();
        }
    }
}
