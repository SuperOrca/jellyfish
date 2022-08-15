package me.superorca.jellyfish.core;

import me.superorca.jellyfish.Jellyfish;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class Command {

    public Jellyfish bot;

    public Command(Jellyfish bot) {
        this.bot = bot;
    }

    @NotNull
    public abstract String getLabel();

    @NotNull
    public abstract String getDescription();

    @NotNull
    public abstract Category getCategory();

    public Predicate<SlashCommandEvent> getChecks() {
        return event -> true;
    }

    public List<OptionData> getOptions() {
        return new ArrayList<>();
    }

    public List<SubcommandData> getSubcommands() {
        return new ArrayList<>();
    }

    public Permission getUserPermission() {
        return null;
    }

    public Permission getBotPermission() {
        return Permission.MESSAGE_WRITE;
    }

    public abstract void execute(@NotNull SlashCommandEvent event);

    public void click(@NotNull ButtonClickEvent event) {
    }
}