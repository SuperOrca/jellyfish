package me.superorca.jellyfish.modules.misc;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

public class PingCommand extends Command {
    public PingCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "ping";
    }

    @Override
    public @NotNull String getDescription() {
        return "Pong! \uD83C\uDFD3";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.MISC;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        event.getHook().editOriginalEmbeds(new Embed().setDescription(":ping_pong:  `%dms`".formatted(event.getJDA().getGatewayPing())).build()).queue();
    }
}
