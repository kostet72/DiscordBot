package discord.bot.omegaloli.service;

import discord.bot.omegaloli.constant.LVL;
import discord.bot.omegaloli.model.entity.BotUser;
import discord.bot.omegaloli.constant.ServerRoleId;

import reactor.core.publisher.Mono;
import net.dv8tion.jda.api.entities.*;
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

    public Boolean checkUserRegistry(Long userId) {

        return template.exists(query(where("id").is(userId)), BotUser.class)
                .flatMap(isExist -> {

                    if (Boolean.TRUE.equals(isExist))
                        return Mono.just(true);

                    else return Mono.just(false);
                })
                .block();
    }

    public BotUser getUserInfo(Long userId) {
        return template.selectOne(query(where("id").is(userId)), BotUser.class)
                .switchIfEmpty(Mono.empty())
                .block();
    }

    public void registerUser(User user) {

        template.getDatabaseClient()
                .sql(String.format(
                        "INSERT INTO users (id, name, lvl, experience, registration_date, warns_count) " +
                                "VALUES ('%s', '%s', '%s', '%s', '%s', '%s') ON CONFLICT (id) DO NOTHING;",
                        user.getIdLong(), user.getName(), "0", 0, Date.from(Instant.now()), 0
                ))
                .fetch()
                .rowsUpdated()
                .block();
    }

    public String updateExperienceAndSetLevel(Long userId, Member member, Guild guild, Integer experience) {

        Role lvl1 = guild.getRoleById(ServerRoleId.LVL_1_ROLE_ID);
        Role lvl2 = guild.getRoleById(ServerRoleId.LVL_2_ROLE_ID);
        Role lvl3 = guild.getRoleById(ServerRoleId.LVL_3_ROLE_ID);
        Role lvl4 = guild.getRoleById(ServerRoleId.LVL_4_ROLE_ID);
        Role lvl5 = guild.getRoleById(ServerRoleId.LVL_5_ROLE_ID);

        return template.selectOne(query(where("id").is(userId)), BotUser.class)
                .flatMap(u -> {

                    Integer exp = u.getExperience() + experience;
                    String level = LVL.getLVLsRange(exp);

                    return template.update(query(where("id").is(userId)),
                        update("experience", exp).set("lvl", level), BotUser.class)
                        .flatMap(lvl -> {

                            if (!u.getLvl().equals(level) && member != null &&
                                    lvl1 != null && lvl2 != null && lvl3 != null && lvl4 != null && lvl5 != null) {

                                switch (level) {
                                    case "1" -> guild.addRoleToMember(member, lvl1).queue();
                                    case "2" -> {
                                        guild.addRoleToMember(member, lvl2).queue();
                                        guild.removeRoleFromMember(member, lvl1).queue();
                                    }
                                    case "3" -> {
                                        guild.addRoleToMember(member, lvl3).queue();
                                        guild.removeRoleFromMember(member, lvl2).queue();
                                    }
                                    case "4" -> {
                                        guild.addRoleToMember(member, lvl4).queue();
                                        guild.removeRoleFromMember(member, lvl3).queue();
                                    }
                                    case "5" -> {
                                        guild.addRoleToMember(member, lvl5).queue();
                                        guild.removeRoleFromMember(member, lvl4).queue();
                                    }
                                }

                                return Mono.just(level);
                            }

                            else return Mono.empty();
                        });
                })
                .switchIfEmpty(Mono.empty())
                .block();
    }

    public void warnUser(Long userId) {

        template.selectOne(query(where("id").is(userId)), BotUser.class)
                .flatMap(u -> template.update(query(where("id").is(userId)),
                        update("warns_count", u.getWarnsCount() + 1),
                        BotUser.class))
                .switchIfEmpty(Mono.empty())
                .block();
    }

    public void banUser(Long userId) {
        template.delete(query(where("id").is(userId)), BotUser.class);
    }
}
