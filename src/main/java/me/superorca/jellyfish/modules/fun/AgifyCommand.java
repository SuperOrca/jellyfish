package me.superorca.jellyfish.modules.fun;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;

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
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        String name = event.getOption("name").getAsString();

        Session.get("https://api.agify.io/?name=%s".formatted(name), response -> {
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
        });
    }
}
