package me.superorca.jellyfish.modules.utility;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserInfoCommand extends Command {
    public UserInfoCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "userinfo";
    }

    @Override
    public @NotNull String getDescription() {
        return "\u2139\uFE0F about \uD83E\uDDCD";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.UTILITY;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.USER, "user", "Select a user or enter a user id.", true));
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        User user = event.getOption("user").getAsUser();
        event.getHook().editOriginalEmbeds(new Embed()
                .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                .setDescription("""
                        %s
                        
                        ID: `%d`
                        Created: <t:%d:f> (<t:%3$d:R>)
                        Avatar: [`link`](%s)
                        """.formatted(user.getAsMention(), user.getIdLong(), user.getTimeCreated().toEpochSecond(), user.getAvatarUrl()))
                .setThumbnail(user.getAvatarUrl())
                .build()
        ).queue();
    }
}
