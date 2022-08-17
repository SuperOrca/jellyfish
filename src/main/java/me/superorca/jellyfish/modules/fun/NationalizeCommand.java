package me.superorca.jellyfish.modules.fun;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.Util;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

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
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String name = event.getOption("name").getAsString();

        Session.get("https://api.nationalize.io/?name=%s".formatted(name), response -> {
            JSONObject data = response.getBody().getObject();
            String nameData = data.getString("name");
            JSONArray countries = data.getJSONArray("country");
            StringBuilder description = new StringBuilder();
            for (int i = 0; i < countries.length(); i++) {
                JSONObject country = countries.getJSONObject(i);
                description.append("%s: `%.2f`".formatted(Util.getCountryName(country.getString("country_id")), country.getDouble("probability"))).append("\n");
            }
            event.getHook().editOriginalEmbeds(new Embed()
                    .setFooter("Powered by nationalize.io")
                    .setTitle(nameData)
                    .setDescription(description.toString())
                    .build()).queue();
        });
    }
}
