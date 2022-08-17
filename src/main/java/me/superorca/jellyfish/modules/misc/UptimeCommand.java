package me.superorca.jellyfish.modules.misc;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

public class UptimeCommand extends Command {
    public UptimeCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "uptime";
    }

    @Override
    public @NotNull String getDescription() {
        return "\u231B of \uD83E\uDD16";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.MISC;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        long duration = System.currentTimeMillis() - bot.getStartTime().toEpochMilli();
        String uptime = DurationFormatUtils.formatDurationWords(duration, true, true);
        event.getHook().editOriginalEmbeds(new Embed().setDescription("Uptime: `%s`".formatted(uptime)).build()).queue();
    }
}
