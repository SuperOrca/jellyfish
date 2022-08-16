package me.superorca.jellyfish.modules.utility;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;
import static me.superorca.jellyfish.core.embed.EmbedColor.SUCCESS;

public class EmoteCommand extends Command {
    private final List<JSONObject> emojis = new ArrayList<>();

    public EmoteCommand(Jellyfish bot) {
        super(bot);

        Unirest.get("https://emoji.gg/api").asJsonAsync(new Callback<>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                JSONArray list = response.getBody().getArray();
                for (int i = 0; i < list.length(); i++) {
                    emojis.add(list.getJSONObject(i));
                }
            }

            @Override
            public void failed(UnirestException e) {
            }

            @Override
            public void cancelled() {
            }
        });
    }

    @Override
    public @NotNull String getLabel() {
        return "emote";
    }

    @Override
    public @NotNull String getDescription() {
        return "test";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.UTILITY;
    }

    @Override
    public List<SubcommandData> getSubcommands() {
        return Arrays.asList(
                new SubcommandData("upload", "\uD83D\uDCC1 Upload an emote")
                        .addOption(OptionType.STRING, "name", "Enter a name for the emote.", true)
                        .addOption(OptionType.STRING, "link", "Enter a valid image url.", true),
                new SubcommandData("remove", "\uD83D\uDDD1 Delete an emote")
                        .addOption(OptionType.STRING, "name", "Enter a name of an emote.", true),
                new SubcommandData("add", "\u2795 Add an emote from emote.gg")
                        .addOption(OptionType.STRING, "id", "Enter a valid emote id from emote.gg.", true)
        );
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        OptionMapping nameOption = event.getOption("name");
        OptionMapping linkOption = event.getOption("link");
        OptionMapping idOption = event.getOption("id");
        String name;
        String link;
        String id;

        switch (event.getSubcommandName()) {
            case "upload":
                name = nameOption.getAsString();
                link = linkOption.getAsString();
                String finalName = name;
                Unirest.get(link).asBinaryAsync(new Callback<>() {
                    @Override
                    public void completed(HttpResponse<InputStream> response) {
                        InputStream file = response.getBody();
                        Emote emote = null;
                        try {
                            emote = event.getGuild().createEmote(finalName, Icon.from(file)).complete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Emote finalEmote = emote;
                        event.getHook().editOriginalEmbeds(new Embed(SUCCESS)
                                .setDescription("%s Added `%s`".formatted(finalEmote.getAsMention(), finalName))
                                .build()).queue();
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
                break;
            case "remove":
                name = nameOption.getAsString();
                List<Emote> search = event.getGuild().getEmotesByName(name, true);
                if (search.isEmpty()) {
                    event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a emote.".formatted(name)).build()).queue();
                    return;
                }
                Emote emote = search.get(0);
                emote.delete().queue();
                event.getHook().editOriginalEmbeds(new Embed().setDescription("Removed `%s`".formatted(emote.getName())).build()).queue();
                break;
            case "add":
                id = idOption.getAsString();
                String slug = id.toLowerCase(Locale.ROOT).replace("-", "_");
                Optional<JSONObject> optionalEmoji = emojis.stream().filter(emoji -> emoji.getString("slug").toLowerCase(Locale.ROOT).equals(slug)).findFirst();
                if (optionalEmoji.isEmpty()) {
                    event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a valid emote id from emote.gg.".formatted(slug)).build()).queue();
                    return;
                }
                JSONObject emoji = optionalEmoji.get();
                Unirest.get(emoji.getString("image")).asBinaryAsync(new Callback<>() {
                    @Override
                    public void completed(HttpResponse<InputStream> response) {
                        InputStream file = response.getBody();
                        Emote emote = null;
                        try {
                            emote = event.getGuild().createEmote(emoji.getString("title"), Icon.from(file)).complete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Emote finalEmote = emote;
                        event.getHook().editOriginalEmbeds(new Embed(SUCCESS)
                                .setDescription("%s Added `%s`".formatted(finalEmote.getAsMention(), emoji.getString("title")))
                                .build()).queue();
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
                break;
        }
    }
}
