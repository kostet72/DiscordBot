package discord.bot.omegaloli.command.global;

import discord.bot.omegaloli.constant.TextMessage;

import discord.bot.omegaloli.command.CommandInterface;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class HelpCommand implements CommandInterface {

    @Override
    public String genName() {
        return "помощь";
    }

    @Override
    public String getDescription() {
        return "Список возможностей бота";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteraction event) {
        event.reply(TextMessage.HELP_MESSAGE).queue();
    }
}
