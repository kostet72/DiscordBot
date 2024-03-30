package discord.bot.omegaloli.model.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class BotUser {

    private Long id;
    private String name;
    private String lvl;
    private Integer experience;
    private LocalDateTime registrationDate;
    private Integer warnsCount;
}
