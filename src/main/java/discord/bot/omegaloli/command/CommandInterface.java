package discord.bot.omegaloli.command;

import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public interface CommandInterface {

    String getName();
    String getDescription();
    List<OptionData> getOptions();

    void execute(SlashCommandInteraction event);
}
