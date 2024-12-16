package jda.bot.defect.service;

import jda.bot.defect.command.*;
import jda.bot.defect.banner.Banner;
import jda.bot.defect.config.BotConfig;

import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.api.requests.GatewayIntent;

import lombok.SneakyThrows;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class BotService {

    private JDA jda;
    private final BotConfig config;
    private final Banner banner;

    public void startBot() {

        jda = JDABuilder.createDefault(config.getToken())
                .enableCache(CacheFlag.VOICE_STATE)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setActivity(Activity.customStatus("bruh"))
                .build();
    }

    public void shutDownBot() {
        jda.shutdown();
    }

    public void registerListeners(Object... listener) {
        jda.addEventListener(listener);
    }

    public CommandManager registerCommands(List<CommandInterface> commands) {

        CommandManager manager = new CommandManager();
        for (CommandInterface command : commands)
            manager.registerCommand(command);

        return manager;
    }

    @SneakyThrows
    @Scheduled(fixedRate = 60000)
    public void updateBanner() {
        banner.updateBanner(jda);
    }
}
