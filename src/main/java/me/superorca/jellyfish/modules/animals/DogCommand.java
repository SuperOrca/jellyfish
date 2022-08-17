package me.superorca.jellyfish.modules.animals;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import static me.superorca.jellyfish.core.embed.EmbedColor.SUCCESS;

public class DogCommand extends Command {
    public DogCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "dog";
    }

    @Override
    public @NotNull String getDescription() {
        return "Random \uD83D\uDC36";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.ANIMALS;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        Session.get("https://api.thedogapi.com/v1/images/search?size=full&format=json?limit=1", response -> {
            JSONObject data = response.getBody().getArray().getJSONObject(0);
            event.getHook().editOriginalEmbeds(new Embed(SUCCESS).setImage(data.getString("url")).setFooter("Powered by thedogapi.com").build()).queue();
        });
    }
}
