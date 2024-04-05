package discord.bot.omegaloli.command.fun;

import discord.bot.omegaloli.constant.TextMessage;
import discord.bot.omegaloli.service.BotUserService;
import discord.bot.omegaloli.command.CommandInterface;

import net.dv8tion.jda.api.interactions.commands.*;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class RPSCommand implements CommandInterface {

    private final BotUserService userService;

    @Override
    public String getName() {
        return "suefa";
    }

    @Override
    public String getDescription() {
        return "Камень-Ножницы-бумага";
    }

    @Override
    public List<OptionData> getOptions() {

        List<OptionData> data = new ArrayList<>();
        data.add(new OptionData(OptionType.STRING, "выбор",
                "Ваберите камень, ножницы или бумагу", true)
                .addChoice("Камень", "Камень")
                .addChoice("Ножницы", "Ножницы")
                .addChoice("Бумага", "Бумага")
        );

        return data;
    }

    @Override
    public void execute(SlashCommandInteraction event) {

        String[] moves = {"Камень", "Ножницы", "Бумага"};
        OptionMapping option = event.getOption("выбор");

        if (option != null) {

            String playerMove = option.getAsString();
            String botMove = generateRandomMove(moves);
            String result = winnerVerification(playerMove, botMove);

            switch (result) {

                case TextMessage.BOT_WINS_MESSAGE:
                    userService.updateExperienceAndSetLevel(event.getUser().getIdLong(), event.getMember(), event.getGuild(), 1);

                case TextMessage.DRAW_MESSAGE:
                    userService.updateExperienceAndSetLevel(event.getUser().getIdLong(), event.getMember(), event.getGuild(), 2);

                case TextMessage.PLAYER_WINS_MESSAGE:
                    userService.updateExperienceAndSetLevel(event.getUser().getIdLong(), event.getMember(), event.getGuild(), 3);
            }

            event.reply(TextMessage.POCK_PAPER_SCISSORS_MESSAGE +
                    "```Ход опонента - " + botMove + "\nВаш ход - " + playerMove + "```" + result
            ).queue();
        }
    }

    public String generateRandomMove(String[] moves) {
        return moves[new Random().nextInt(moves.length)];
    }

    public String winnerVerification(String playerMove, String botMove) {

        if (playerMove.equals(botMove)) return TextMessage.DRAW_MESSAGE;

        else if (playerMove.equals("Камень") && botMove.equals("Ножницы") ||
                playerMove.equals("Ножницы") && botMove.equals("Бумага") ||
                playerMove.equals("Бумага") && botMove.equals("Камень"))
            return TextMessage.PLAYER_WINS_MESSAGE;

        else return TextMessage.BOT_WINS_MESSAGE;
    }
}
