package me.superorca.jellyfish.modules.utility;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static me.superorca.jellyfish.Emotes.*;

public class ServerCommand extends Command {
    public ServerCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "server";
    }

    @Override
    public @NotNull String getDescription() {
        return "\u2139\uFE0F about the server";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.UTILITY;
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        Guild guild = event.getGuild();
        Member owner = guild.retrieveOwner().complete();
        int categories = guild.getCategories().size();
        int channels = guild.getTextChannels().size();
        int vcs = guild.getVoiceChannels().size();
        List<Member> memberList = guild.getMembers();
        int members = memberList.size();
        long bots = memberList.stream().filter(member -> member.getUser().isBot()).count();
        long humans = members - bots;
        int emojis = guild.getEmotes().size();
        int roles = guild.getRoles().size();
        event.getHook().editOriginalEmbeds(new Embed()
                .setAuthor(guild.getName(), null, guild.getIconUrl())
                .setDescription("""
                        ID: `%d`
                        Owner: %s
                        Created: <t:%d:f> (<t:%3$d:R>)

                        Channels: %s `%d` | %s `%d` | %s `%d`
                        Members: :bust_in_silhouette: `%d` | :robot: `%d` | %s `%d`
                        Misc: :smile: `%d` Emojis | :flag_white: `%d` Roles
                        """.formatted(guild.getIdLong(), owner.getAsMention(), guild.getTimeCreated().toEpochSecond(), CATEGORY, categories, TEXT_CHANNEL, channels, VOICE_CHANNEL, vcs, humans, bots, MEMBERS, members, emojis, roles))
                .setThumbnail(guild.getIconUrl())
                .build()).setActionRow(
                        Button.link(guild.getIconUrl(), "Icon")
        ).queue();
    }
}
