package discord.bot.omegaloli.command;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.ArrayList;

public class CommandManager extends ListenerAdapter {

    private final List<CommandInterface> commands = new ArrayList<>();

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        for (Guild guild : event.getJDA().getGuilds())
            for (CommandInterface command : commands) {

                if (command.getOptions() != null)
                    guild.upsertCommand(command.genName(), command.getDescription()).addOptions(command.getOptions()).queue();
                else
                    guild.upsertCommand(command.genName(), command.getDescription()).queue();
            }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        for (CommandInterface command : commands)
            if (command.genName().equals(event.getName())) {

                command.execute(event);
                return;
            }
    }

    public void registerCommand(CommandInterface command) {
        commands.add(command);
    }
}
