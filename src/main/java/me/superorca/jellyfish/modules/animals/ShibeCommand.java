package me.superorca.jellyfish.modules.animals;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import static me.superorca.jellyfish.core.embed.EmbedColor.SUCCESS;

public class ShibeCommand extends Command {
    public ShibeCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "shibe";
    }

    @Override
    public @NotNull String getDescription() {
        return "Random Shiba Inu \uD83D\uDC36";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.ANIMALS;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        Session.get("https://shibe.online/api/shibes", response -> {
            JSONArray data = response.getBody().getArray();
            event.getHook().editOriginalEmbeds(new Embed(SUCCESS).setImage(data.getString(0)).setFooter("Powered by shibe.online").build()).queue();
        });
    }
}
