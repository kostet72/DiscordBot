package discord.bot.omegaloli;

import discord.bot.omegaloli.service.BotService;
import discord.bot.omegaloli.service.BotUserService;
import discord.bot.omegaloli.listener.member.MemberJoinsListener;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
				new MemberJoinsListener(userService)
		);
	}
}
