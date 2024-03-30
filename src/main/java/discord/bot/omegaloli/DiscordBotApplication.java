package discord.bot.omegaloli;

import discord.bot.omegaloli.service.BotService;
import discord.bot.omegaloli.service.BotUserService;

import discord.bot.omegaloli.listener.command.ModalListener;
import discord.bot.omegaloli.listener.rules.RulesUpdateListener;
import discord.bot.omegaloli.listener.user.member.MemberJoinsListener;
import discord.bot.omegaloli.listener.user.experience.UserExperienceListener;

import discord.bot.omegaloli.command.fun.*;
import discord.bot.omegaloli.command.dev.*;
import discord.bot.omegaloli.command.admin.*;
import discord.bot.omegaloli.command.music.*;
import discord.bot.omegaloli.command.global.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class DiscordBotApplication implements CommandLineRunner {

	private final BotService bot;

	@Autowired
	private BotUserService userService;

    public static void main(String[] args) {
		SpringApplication.run(DiscordBotApplication.class, args);
	}

	@Override
	public void run(String... args) {

		bot.startBot();

		bot.registerListeners(

				new MemberJoinsListener(userService),
				new UserExperienceListener(userService),
				new RulesUpdateListener(),
				new ModalListener(),

				bot.registerCommands(List.of(

						// ADMIN
						new WarnCommand(userService),

						// DEV
						new ChangeLogCommand(),

						// GLOBAL
						new HelpCommand(),
						new ProfileCommand(userService),
						new EventCommand(),

						// FUN
						new RPSCommand(),

						// MUSIC
						new PlayCommand(),
						new SkipCommand(),
						new StopCommand()
				))
		);
	}
}
