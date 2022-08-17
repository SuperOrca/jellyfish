package me.superorca.jellyfish.modules.animals;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;
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
    public void execute(@NotNull SlashCommandEvent event) {
        Unirest.get("https://api.thecatapi.com/v1/images/search?size=full&format=json?limit=1").asJsonAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                JSONObject data = response.getBody().getArray().getJSONObject(0);
                event.getHook().editOriginalEmbeds(new Embed(SUCCESS).setImage(data.getString("url")).setFooter("Powered by thecatapi.com").build()).queue();
            }

            @Override
            public void failed(UnirestException e) {
                event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` occurred whilst running the command." .formatted(e.getMessage())).build()).queue();
            }

            @Override
            public void cancelled() {
                event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("An error occurred whilst running the command.").build()).queue();
            }
        });
    }
}
