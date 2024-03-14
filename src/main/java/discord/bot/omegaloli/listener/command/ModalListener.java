package discord.bot.omegaloli.listener.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if (event.getModalId().equals("create-change-log"))
            changeLogBuilder(event);
    }

    public void changeLogBuilder(ModalInteractionEvent event) {

        ModalMapping versionValue = event.getValue("change-log-version");
        ModalMapping authorValue = event.getValue("change-log-author");
        ModalMapping changesValue = event.getValue("change-log-list");

        if (versionValue != null && authorValue != null && changesValue != null) {

            String version = versionValue.getAsString();
            String author = authorValue.getAsString();
            String changes = changesValue.getAsString();

            EmbedBuilder changeLogBuilder = new EmbedBuilder();
            changeLogBuilder.setColor(Color.decode("#9400D3"));

            changeLogBuilder.setTitle("Список изменений");
            changeLogBuilder.addField("Версия:", version, true);
            changeLogBuilder.addField("Автор:", author, true);
            changeLogBuilder.addField("Список изменений:", changes, false);
            changeLogBuilder.addBlankField(false);

            changeLogBuilder.setFooter("Тыкай по синему тексту, чтобы перейти на GitHub проекта");
            changeLogBuilder.setUrl("https://github.com/kostet72/DiscordBot");

            event.replyEmbeds(changeLogBuilder.build()).queue();
        }
    }
}
