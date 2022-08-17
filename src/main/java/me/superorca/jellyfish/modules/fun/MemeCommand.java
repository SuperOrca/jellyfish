package me.superorca.jellyfish.modules.fun;

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

public class MemeCommand extends Command {
    public MemeCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "meme";
    }

    @Override
    public @NotNull String getDescription() {
        return "Random \uD83D\uDDBC";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.FUN;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        Unirest.get("https://meme-api.herokuapp.com/gimme").asJsonAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                JSONObject data = response.getBody().getObject();
                if (data.getBoolean("nsfw") && !event.getTextChannel().isNSFW()) {
                    execute(event);
                    return;
                }
                String link = data.getString("postLink");
                String subreddit = data.getString("subreddit");
                String title = data.getString("title");
                String url = data.getString("url");
                String author = data.getString("author");
                event.getHook().editOriginalEmbeds(new Embed()
                        .setFooter("Powered by meme-api.herokuapp.com")
                        .setTitle(title, link)
                        .setDescription("from `r/%s` by `u/%s`" .formatted(subreddit, author))
                        .setImage(url)
                        .build()).queue();
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
