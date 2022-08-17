package me.superorca.jellyfish.modules.fun;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Util;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;

public class NationalizeCommand extends Command {
    public NationalizeCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "nationalize";
    }

    @Override
    public @NotNull String getDescription() {
        return "Guess the nationality of a \uD83E\uDDCD";
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
        Unirest.get("https://api.nationalize.io/?name=%s" .formatted(name)).asJsonAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                JSONObject data = response.getBody().getObject();
                String nameData = data.getString("name");
                JSONArray countries = data.getJSONArray("country");
                StringBuilder description = new StringBuilder();
                for (int i = 0; i < countries.length(); i++) {
                    JSONObject country = countries.getJSONObject(i);
                    description.append("%s: `%.2f`" .formatted(Util.getCountryName(country.getString("country_id")), country.getDouble("probability"))).append("\n");
                }
                event.getHook().editOriginalEmbeds(new Embed()
                        .setFooter("Powered by nationalize.io")
                        .setTitle(nameData)
                        .setDescription(description.toString())
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
