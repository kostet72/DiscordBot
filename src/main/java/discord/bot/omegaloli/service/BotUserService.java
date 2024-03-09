package discord.bot.omegaloli.service;

import discord.bot.omegaloli.model.entity.BotUser;

import reactor.core.publisher.Mono;
import net.dv8tion.jda.api.entities.User;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;

import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
public class BotUserService {

    private final R2dbcEntityTemplate template;

    public Boolean findUser(Long userId) {
        return template.exists(query(where("id").is(userId)), BotUser.class)
                .flatMap(isExist -> {

                    if (Boolean.TRUE.equals(isExist))
                        return Mono.just(true);

                    else return Mono.just(false);
                }).block();
    }

    public void registerUser(User user) {

        template.getDatabaseClient()
                .sql(String.format(
                        "INSERT INTO users (id, name, experience, registration_date) " +
                                "VALUES ('%s', '%s', '%s', '%s') ON CONFLICT (id) DO NOTHING;",
                        user.getIdLong(), user.getName(), 0, Date.from(Instant.now())
                ))
                .fetch()
                .rowsUpdated()
                .block();
    }
}
