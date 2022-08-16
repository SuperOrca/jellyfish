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
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;

public class AgifyCommand extends Command {
    public AgifyCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "agify";
    }

    @Override
    public @NotNull String getDescription() {
        return "Guess the age of a \uD83E\uDDCD";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.FUN;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "name", "Enter a name.", true));
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        String name = event.getOption("name").getAsString();
        Unirest.get("https://api.agify.io/?name=%s".formatted(name)).asJsonAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                JSONObject data = response.getBody().getObject();
                String nameData = data.getString("name");
                int age = data.getInt("age");
                int count = data.getInt("count");

                event.getHook().editOriginalEmbeds(new Embed()
                        .setFooter("Powered by agify.io")
                        .setTitle(nameData)
                        .setDescription("""
                                Age: `%d`
                                Count: `%d`
                                """.formatted(age, count)).build()).queue();
            }

            @Override
            public void failed(UnirestException e) {
                event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` occurred whilst running the command.".formatted(e.getMessage())).build()).queue();
            }

            @Override
            public void cancelled() {
                event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("An error occurred whilst running the command.").build()).queue();
            }
        });
    }
}