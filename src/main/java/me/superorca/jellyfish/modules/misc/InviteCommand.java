package me.superorca.jellyfish.modules.misc;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

public class InviteCommand extends Command {
    public InviteCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "invite";
    }

    @Override
    public @NotNull String getDescription() {
        return "\uD83E\uDD16 invite";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.MISC;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        event.getHook().editOriginalEmbeds(new Embed().setDescription("Click a link below to invite Jellyfish to your server.").build()).setActionRow(
                Button.link(bot.getConfig("invite"), "Invite Jellyfish"),
                Button.link(bot.getConfig("support"), "Support Server")
        ).queue();
    }
}
