package discord.bot.omegaloli.service;

import discord.bot.omegaloli.constant.LVL;
import discord.bot.omegaloli.model.entity.BotUser;
import discord.bot.omegaloli.constant.ServerRoleId;

import reactor.core.publisher.Mono;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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

    public String updateExperienceAndSetLevel(Long userId, MessageReceivedEvent event) {

        Member user = event.getMember();
        Guild guild = event.getGuild();
        Role lvl1 = event.getGuild().getRoleById(ServerRoleId.LVL_1_ROLE_ID);
        Role lvl2 = event.getGuild().getRoleById(ServerRoleId.LVL_2_ROLE_ID);
        Role lvl3 = event.getGuild().getRoleById(ServerRoleId.LVL_3_ROLE_ID);
        Role lvl4 = event.getGuild().getRoleById(ServerRoleId.LVL_4_ROLE_ID);
        Role lvl5 = event.getGuild().getRoleById(ServerRoleId.LVL_5_ROLE_ID);

        return template.selectOne(query(where("id").is(userId)), BotUser.class)
                .flatMap(u -> {

                    Integer experience = u.getExperience() + 1;
                    String level = LVL.getLVLsRange(experience);

                    return template.update(query(where("id").is(userId)),
                        update("experience", experience).set("lvl", level), BotUser.class)
                        .flatMap(lvl -> {

                            if (!u.getLvl().equals(level) && user != null &&
                                    lvl1 != null && lvl2 != null && lvl3 != null && lvl4 != null && lvl5 != null) {

                                switch (level) {
                                    case "1" -> guild.addRoleToMember(user, lvl1).queue();
                                    case "2" -> {
                                        guild.addRoleToMember(user, lvl2).queue();
                                        guild.removeRoleFromMember(user, lvl1).queue();
                                    }
                                    case "3" -> {
                                        guild.addRoleToMember(user, lvl3).queue();
                                        guild.removeRoleFromMember(user, lvl2).queue();
                                    }
                                    case "4" -> {
                                        guild.addRoleToMember(user, lvl4).queue();
                                        guild.removeRoleFromMember(user, lvl3).queue();
                                    }
                                    case "5" -> {
                                        guild.addRoleToMember(user, lvl5).queue();
                                        guild.removeRoleFromMember(user, lvl4).queue();
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
}
