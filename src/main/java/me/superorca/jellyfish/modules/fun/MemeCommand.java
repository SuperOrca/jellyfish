package me.superorca.jellyfish.modules.fun;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        Session.get("https://meme-api.herokuapp.com/gimme", response -> {
            JSONObject data = response.getBody().getObject();
            if (data.getBoolean("nsfw") && !event.getChannel().asTextChannel().isNSFW()) {
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
                    .setDescription("from `r/%s` by `u/%s`".formatted(subreddit, author))
                    .setImage(url)
                    .build()).queue();
        });
    }
}
