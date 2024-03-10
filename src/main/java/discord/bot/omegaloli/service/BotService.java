package discord.bot.omegaloli.service;

import discord.bot.omegaloli.config.BotConfig;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BotService {

    private JDA jda;

    private final BotConfig config;

    public void startBot() {

        jda = JDABuilder.createDefault(config.getToken())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.customStatus("Заглядывай в #dev"))
                .build();
    }

    public void shutDownBot() {
        jda.shutdown();
    }

    public void registerListeners(Object... listener) {
        jda.addEventListener(listener);
    }
}
