package me.superorca.jellyfish.modules.fun;

import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.Session;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;
import static me.superorca.jellyfish.core.embed.EmbedColor.SUCCESS;

public class MinecraftCommand extends Command {
    public MinecraftCommand(Jellyfish bot) {
        super(bot);
    }

    @Override
    public @NotNull String getLabel() {
        return "minecraft";
    }

    @Override
    public @NotNull String getDescription() {
        return "\u2139\uFE0F about Minecraft";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.FUN;
    }

    @Override
    public List<SubcommandData> getSubcommands() {
        return Arrays.asList(new SubcommandData("player", "\u2139\uFE0F about \uD83E\uDDCD").addOption(OptionType.STRING, "id", "Enter a valid Minecraft username or UUID.", true), new SubcommandData("skin", "Skin of a \uD83E\uDDCD").addOption(OptionType.STRING, "id", "Enter a valid Minecraft username or UUID.", true), new SubcommandData("server", "\u2139\uFE0F about a server").addOption(OptionType.STRING, "ip", "Enter a valid Minecraft server IP.", true));
    }

    @Override
    public void execute(@NotNull SlashCommandInteractionEvent event) {
        OptionMapping idOption = event.getOption("id");
        OptionMapping ipOption = event.getOption("ip");
        String id;
        String ip;

        switch (event.getSubcommandName()) {
            case "player" -> {
                id = idOption.getAsString();
                Session.get("https://mc-heads.net/minecraft/profile/%s".formatted(id), response -> {
                    if (response.getStatus() == 204) {
                        event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a valid Minecraft username or UUID.".formatted(id)).build()).queue();
                        return;
                    }
                    JSONObject data = response.getBody().getObject();
                    String uuid = data.getString("id");
                    String name = data.getString("name");
                    JSONArray nameData = data.getJSONArray("name_history");
                    StringBuilder nameHistory = new StringBuilder();
                    for (int i = nameData.length() - 1; i >= 0; i--) {
                        JSONObject jsonObject = nameData.getJSONObject(i);
                        String prevName = jsonObject.getString("name");
                        nameHistory.append(jsonObject.has("changedToAt") ? "`%s` (<t:%d:R>)".formatted(prevName, jsonObject.getLong("changedToAt") / 1000L) : "`%s` (Original)".formatted(prevName)).append("\n");
                    }
                    event.getHook().editOriginalEmbeds(new Embed(SUCCESS).setTitle(name).setDescription("UUID: `%s`".formatted(uuid)).addField("Name History", nameHistory.toString(), false).setThumbnail("https://mc-heads.net/head/%s".formatted(uuid)).setFooter("Powered by mc-heads.net and namemc.com").build()).setActionRow(Button.link("https://namemc.com/profile/%s".formatted(name), "NameMC")).queue();
                });
            }
            case "skin" -> {
                id = idOption.getAsString();
                Session.get("https://mc-heads.net/minecraft/profile/%s".formatted(id), response -> {
                    if (response.getStatus() == 204) {
                        event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a valid Minecraft username or UUID.".formatted(id)).build()).queue();
                        return;
                    }
                    JSONObject data = response.getBody().getObject();
                    String uuid = data.getString("id");
                    String name = data.getString("name");
                    event.getHook().editOriginalEmbeds(new Embed(SUCCESS).setTitle(name).setImage("https://mc-heads.net/body/%s".formatted(uuid)).setFooter("Powered by mc-heads.net").build()).setActionRow(Button.link("https://mc-heads.net/download/%s".formatted(uuid), "Download")).queue();
                });
            }
            case "server" -> {
                ip = ipOption.getAsString();
                Session.get("https://api.mcsrvstat.us/2/%s".formatted(ip), response -> {
                    JSONObject data = response.getBody().getObject();
                    JSONObject debug = data.getJSONObject("debug");
                    if (debug.has("error") && debug.getJSONObject("error").has("ping")) {
                        event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a valid Minecraft server IP.".formatted(ip)).build()).queue();
                        return;
                    }
                    String rawIp = data.getString("ip");
                    int port = data.getInt("port");
                    JSONArray motdData = data.getJSONObject("motd").getJSONArray("clean");
                    StringBuilder motd = new StringBuilder().append("```\n");
                    for (int i = 0; i < motdData.length(); i++) {
                        motd.append(motdData.getString(i)).append("\n");
                    }
                    motd.append("\n```");
                    boolean online = data.getBoolean("online");
                    int protocol = data.getInt("protocol");
                    String hostname = data.getString("hostname");
                    String base64Image = data.getString("icon").split(",")[1];
                    byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                    JSONObject players = data.getJSONObject("players");
                    int num = players.getInt("online");
                    int max = players.getInt("max");
                    String version = data.getString("version");
                    event.getHook().sendFile(imageBytes, "server-icon.png").queue(m -> {
                        m.editMessageEmbeds(new Embed(SUCCESS).setTitle(hostname).setDescription("""
                                Raw IP: `%s`
                                Port: `%d`
                                Online? `%b`
                                Protocol: `%d`
                                Players: `%d`/`%d`
                                Version: `%s`
                                """.formatted(rawIp, port, online, protocol, num, max, version)).addField("MOTD", motd.toString(), false).setThumbnail("attachment://server-icon.png").setFooter("Powered by mcsrvstat.us").build()).queue();
                    });
                });
            }
        }
    }
}
