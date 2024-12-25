package jda.bot.defect.config;

import lombok.*;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@Component
public class BotConfig {

    @Value("${bot.token}")
    private String token;

    @Value("${bot.server}")
    private String serverId;

    @Value("${file.path.dev}")
    private String filePath;
}
