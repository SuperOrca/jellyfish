package me.superorca.jellyfish.modules.utility;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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

        Session.get("https://emoji.gg/api", response -> {
            JSONArray list = response.getBody().getArray();
            for (int i = 0; i < list.length(); i++) {
                emojis.add(list.getJSONObject(i));
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
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        OptionMapping nameOption = event.getOption("name");
        OptionMapping linkOption = event.getOption("link");
        OptionMapping idOption = event.getOption("id");
        String name;
        String link;
        String id;

        switch (event.getSubcommandName()) {
            case "upload" -> {
                name = nameOption.getAsString();
                link = linkOption.getAsString();
                Session.getBinary(link, response -> {
                    InputStream file = response.getBody();
                    RichCustomEmoji emoji = null;
                    try {
                        emoji = event.getGuild().createEmoji(name, Icon.from(file)).complete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    event.getHook().editOriginalEmbeds(new Embed(SUCCESS)
                            .setDescription("%s Added `%s`".formatted(emoji.getAsMention(), name))
                            .build()).queue();
                });
            }
            case "remove" -> {
                name = nameOption.getAsString();
                RichCustomEmoji search = event.getGuild().getEmojiById(name);
                if (search == null) {
                    event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a emote.".formatted(name)).build()).queue();
                    return;
                }
                search.delete().queue();
                event.getHook().editOriginalEmbeds(new Embed().setDescription("Removed `%s`".formatted(search.getName())).build()).queue();
            }
            case "add" -> {
                id = idOption.getAsString();
                String slug = id.toLowerCase(Locale.ROOT).replace("-", "_");
                Optional<JSONObject> optionalEmoji = emojis.stream().filter(emoji -> emoji.getString("slug").toLowerCase(Locale.ROOT).equals(slug)).findFirst();
                if (optionalEmoji.isEmpty()) {
                    event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a valid emote id from emote.gg.".formatted(slug)).build()).queue();
                    return;
                }
                JSONObject emojiData = optionalEmoji.get();
                Session.getBinary(emojiData.getString("image"), response -> {
                    InputStream file = response.getBody();
                    RichCustomEmoji emoji = null;
                    try {
                        emoji = event.getGuild().createEmoji(emojiData.getString("title"), Icon.from(file)).complete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    event.getHook().editOriginalEmbeds(new Embed(SUCCESS)
                            .setDescription("%s Added `%s`".formatted(emoji.getAsMention(), emojiData.getString("title")))
                            .build()).queue();
                });
            }
        }
    }
}
