package jda.bot.defect;

import jda.bot.defect.service.BotService;

import org.springframework.boot.*;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {

	private final BotService bot;

    public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Override
	public void run(String... args) {

		bot.startBot();

		bot.registerListeners(

				bot.registerCommands(List.of(
				))
		);
	}
}
