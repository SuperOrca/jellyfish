package me.superorca.jellyfish.modules.animals;

import com.google.gson.JsonObject;
import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;

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
        Request request = new Request.Builder().url("https://some-random-api.ml/img/dog").build();
        bot.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("An error occurred while running the command.").build()).queue();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onFailure(call, new IOException());
                    return;
                }

                JsonObject jsonObject = bot.getGson().fromJson(response.body().string(), JsonObject.class);
                String url = jsonObject.get("link").getAsString();
                event.getHook().editOriginalEmbeds(new Embed().setImage(url).setFooter("Powered by some-random-api.ml").build()).queue();
            }
        });
    }
}
