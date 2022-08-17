package me.superorca.jellyfish.modules.utility;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Util;
import me.superorca.jellyfish.core.embed.Embed;
import me.superorca.jellyfish.core.embed.EmbedColor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class CardCommand extends Command {
    public CardCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "card";
    }

    @Override
    public @NotNull String getDescription() {
        return "card";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.UTILITY;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        BufferedImage image = new BufferedImage(3000, 300, TYPE_INT_ARGB);

        Font font = new Font("Arial", Font.BOLD, 120);
        Graphics g = image.getGraphics();
        g.setFont(font);
        g.setColor(EmbedColor.DEFAULT.getColor());
        g.drawString("This is a random quote.", 0, 150);

        event.getHook().sendFile(Util.toByteArray(image, "png"), "card.png").queue(m -> {
            m.editMessageEmbeds(new Embed()
                    .setImage("attachment://card.png")
                    .build()).queue();
        });
    }
}
