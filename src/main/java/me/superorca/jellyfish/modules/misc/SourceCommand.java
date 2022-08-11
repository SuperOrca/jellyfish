package me.superorca.jellyfish.modules.misc;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;

public class SourceCommand extends Command {
    public SourceCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "source";
    }

    @Override
    public @NotNull String getDescription() {
        return "Source code of \uD83E\uDD16";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.MISC;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        event.getHook().editOriginalEmbeds(new Embed().setDescription("`Source:` [SuperOrca/Jellyfish](https://github.com/SuperOrca/Jellyfish)").build()).queue();
    }
}
