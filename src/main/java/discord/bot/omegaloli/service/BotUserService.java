package discord.bot.omegaloli.service;

import discord.bot.omegaloli.constant.LVL;
import discord.bot.omegaloli.model.entity.BotUser;

import reactor.core.publisher.Mono;
import net.dv8tion.jda.api.entities.User;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.Instant;

import static org.springframework.data.relational.core.query.Query.query;
import static org.springframework.data.relational.core.query.Update.update;
import static org.springframework.data.relational.core.query.Criteria.where;

@Service
@RequiredArgsConstructor
public class BotUserService {

    private final R2dbcEntityTemplate template;

    public Boolean checkUserRegistry(Long userId, String name) {

        if (userId != null) {

            return template.exists(query(where("id").is(userId)), BotUser.class)
                    .flatMap(isExist -> {

                        if (Boolean.TRUE.equals(isExist))
                            return Mono.just(true);

                        else return Mono.just(false);
                    })
                    .block();
        }
        else if (name != null) {

            return template.exists(query(where("name").is(name)), BotUser.class)
                    .flatMap(isExist -> {

                        if (Boolean.TRUE.equals(isExist))
                            return Mono.just(true);

                        else return Mono.just(false);
                    })
                    .block();
        }
        else return false;
    }

    public BotUser getUserInfoById(Long userId) {
        return template.selectOne(query(where("id").is(userId)), BotUser.class)
                .switchIfEmpty(Mono.empty())
                .block();
    }

    public BotUser getUserInfoByName(String name) {
        return template.selectOne(query(where("name").is(name)), BotUser.class)
                .switchIfEmpty(Mono.empty())
                .block();
    }

    public void registerUser(User user) {

        template.getDatabaseClient()
                .sql(String.format(
                        "INSERT INTO users (id, name, lvl, experience, registration_date) " +
                                "VALUES ('%s', '%s', '%s', '%s', '%s') ON CONFLICT (id) DO NOTHING;",
                        user.getIdLong(), user.getName(), "0", 0, Date.from(Instant.now())
                ))
                .fetch()
                .rowsUpdated()
                .block();
    }

    public String updateExperienceAndSetLevel(Long userId) {

        return template.selectOne(query(where("id").is(userId)), BotUser.class)
                .flatMap(u -> {

                    Integer experience = u.getExperience() + 1;
                    String level = LVL.getLVLsRange(experience);

                    return template.update(query(where("id").is(userId)),
                        update("experience", experience)
                                .set("lvl", level), BotUser.class)
                        .flatMap(lvl -> {

                            if (!u.getLvl().equals(level))
                                return Mono.just(level);

                            else return Mono.empty();
                        });
                })
                .switchIfEmpty(Mono.empty())
                .block();
    }
}
