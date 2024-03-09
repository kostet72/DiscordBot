package discord.bot.omegaloli.model.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class BotUser {

    private Long id;
    private String name;
    private Integer experience;
    private Date registrationDate;
}
