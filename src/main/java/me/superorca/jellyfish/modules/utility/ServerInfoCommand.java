package me.superorca.jellyfish.modules.utility;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class ServerInfoCommand extends Command {
    public ServerInfoCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "serverinfo";
    }

    @Override
    public @NotNull String getDescription() {
        return "\u2139\uFE0F about the server";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.UTILITY;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {

    }
}
