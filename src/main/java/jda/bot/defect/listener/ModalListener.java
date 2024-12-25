package jda.bot.defect.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {

        if (event.getModalId().equals("embed"))
            embedBuilder(event);
    }

    public void embedBuilder(ModalInteractionEvent event) {

        String title = event.getValue("title").getAsString();
        String titleURL = event.getValue("titleURL").getAsString();
        String description = event.getValue("description").getAsString();
        String footer = event.getValue("footer").getAsString();

        String imageURL = event.getValue("imageURL").getAsString();
        if (imageURL.isBlank()) imageURL = "https://media.discordapp.net/attachments/1184080275001589840/1184086447637151775/dance.gif.gif?ex=676d3670&is=676be4f0&hm=9cf970604a18a77452e17cf6b96779ca4bab5464e27eaa77089fbda1e4e96164&=&width=622&height=481";

        event.getChannel().sendMessageEmbeds(
                new EmbedBuilder()
                        .setColor(Color.decode("#223F9E"))
                        .setTitle(title, titleURL)
                        .setDescription(description)
                        .setThumbnail(imageURL)
                        .setFooter(footer)
                        .build()
        ).queue();

        event.reply("Сообщение доставлено").setEphemeral(true).queue();
    }
}
