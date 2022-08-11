package me.superorca.jellyfish.core;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.embed.Embed;
import me.superorca.jellyfish.modules.animals.CatCommand;
import me.superorca.jellyfish.modules.animals.DogCommand;
import me.superorca.jellyfish.modules.misc.PingCommand;
import me.superorca.jellyfish.modules.misc.SourceCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
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

                // misc
                new PingCommand(bot),
                new SourceCommand(bot)
        );
    }

    public static List<CommandData> unpackCommandData() {
        List<CommandData> commandData = new ArrayList<>();
        for (Command command : commands) {
            CommandData slashCommand = new CommandData(command.getLabel(), command.getDescription()).addOptions(command.getOptions());
            if (command.getUserPermission() != null) {
                slashCommand.setDefaultEnabled(false);
            }
            if (!command.getSubcommands().isEmpty()) {
                slashCommand.addSubcommands(command.getSubcommands());
            }
            commandData.add(slashCommand);
        }
        return commandData;
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
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        Command command = commandsMap.get(event.getName());
        if (command != null) {
            event.deferReply().queue();
            Role botRole = event.getGuild().getBotRole();
            if (command.getBotPermission() != null) {
                if (!botRole.hasPermission(command.getBotPermission()) && !botRole.hasPermission(Permission.ADMINISTRATOR)) {
                    event.getHook().setEphemeral(true);
                    event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("I need the `" + command.getBotPermission().getName() + "` permission to execute that command.").build()).queue();
                    return;
                }
            }
            command.execute(event);
        }
    }

    @Override
    @SubscribeEvent
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        event.getGuild().updateCommands().addCommands(unpackCommandData()).queue(success -> {
        }, fail -> {
        });
    }
}
