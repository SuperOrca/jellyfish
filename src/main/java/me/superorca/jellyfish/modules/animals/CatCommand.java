package me.superorca.jellyfish.modules.animals;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import static me.superorca.jellyfish.core.embed.EmbedColor.SUCCESS;

public class CatCommand extends Command {
    public CatCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "cat";
    }

    @Override
    public @NotNull String getDescription() {
        return "Random \uD83D\uDC31";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.ANIMALS;
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Session.get("https://api.thecatapi.com/v1/images/search?size=full&format=json?limit=1", response -> {
            JSONObject data = response.getBody().getArray().getJSONObject(0);
            event.getHook().editOriginalEmbeds(new Embed(SUCCESS).setImage(data.getString("url")).setFooter("Powered by thecatapi.com").build()).queue();
        });
    }
}
