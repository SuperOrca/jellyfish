package me.superorca.jellyfish.core;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.embed.Embed;
import me.superorca.jellyfish.modules.animals.CatCommand;
import me.superorca.jellyfish.modules.animals.DogCommand;
import me.superorca.jellyfish.modules.animals.ShibeCommand;
import me.superorca.jellyfish.modules.fun.*;
import me.superorca.jellyfish.modules.misc.InviteCommand;
import me.superorca.jellyfish.modules.misc.PingCommand;
import me.superorca.jellyfish.modules.misc.SourceCommand;
import me.superorca.jellyfish.modules.misc.UptimeCommand;
import me.superorca.jellyfish.modules.utility.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;

public class Registry extends ListenerAdapter {
    private static final List<Command> commands = new ArrayList<>();
    private static final Map<String, Command> commandsMap = new HashMap<>();

    public Registry(Jellyfish bot) {
        mapCommand(
                // animals
                new CatCommand(bot),
                new DogCommand(bot),
                new ShibeCommand(bot),

                // fun
                new AgifyCommand(bot),
                new GenderifyCommand(bot),
                new MemeCommand(bot),
                new MinecraftCommand(bot),
                new NationalizeCommand(bot),

                // misc
                new InviteCommand(bot),
                new PingCommand(bot),
                new SourceCommand(bot),
                new UptimeCommand(bot),

                // utility
                new CardCommand(bot),
                new EmoteCommand(bot),
                new IpCommand(bot),
                new ServerCommand(bot),
                new UserCommand(bot)
        );
    }

    public static List<SlashCommandData> unpackCommandData() {
        List<SlashCommandData> slashCommandData = new ArrayList<>();
        for (Command command : commands) {
            SlashCommandData slashCommand = Commands.slash(command.getLabel(), command.getDescription());
            if (command.getUserPermission() != null)
                slashCommand.setDefaultPermissions(DefaultMemberPermissions.enabledFor(command.getUserPermission()));
            if (!command.getOptions().isEmpty()) slashCommand.addOptions(command.getOptions());
            if (!command.getSubcommands().isEmpty()) slashCommand.addSubcommands(command.getSubcommands());
            slashCommandData.add(slashCommand);
        }
        return slashCommandData;
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static Map<String, Command> getCommandsMap() {
        return commandsMap;
    }

    private void mapCommand(Command... cmds) {
        for (Command cmd : cmds) {
            commandsMap.put(cmd.getLabel(), cmd);
            commands.add(cmd);
        }
    }

    @Override
    @SubscribeEvent
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Command command = commandsMap.get(event.getName());
        if (command != null) {
            event.deferReply().queue();
            Role botRole = event.getGuild().getBotRole();
            if (command.getBotPermission() != null) {
                if (!botRole.hasPermission(command.getBotPermission()) && !botRole.hasPermission(Permission.ADMINISTRATOR)) {
                    event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("I need the `" + command.getBotPermission().getName() + "` permission to execute that command.").build()).queue();
                    return;
                }
            }
            if (command.getChecks().test(event)) command.execute(event);
        }
    }

    @Override
    @SubscribeEvent
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Command command = commandsMap.get(event.getMessage().getInteraction().getName().split(" ")[0]);
        command.click(event);
    }

    @Override
    @SubscribeEvent
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(unpackCommandData()).queue(success -> {
        }, fail -> {
        });
    }
}
