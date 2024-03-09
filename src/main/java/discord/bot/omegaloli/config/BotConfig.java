package discord.bot.omegaloli.config;

import lombok.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Setter
@Component
public class BotConfig {

    @Value("${bot.token}")
    private String token;
}
