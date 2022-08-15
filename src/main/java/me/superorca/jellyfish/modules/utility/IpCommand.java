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
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;
import static me.superorca.jellyfish.core.embed.EmbedColor.SUCCESS;

public class IpCommand extends Command {
    private final Pattern pattern;

    public IpCommand(Jellyfish bot) {
        super(bot);

        this.pattern = Pattern.compile("^(?!0\\.0\\.0\\.0|255\\.255\\.255\\.255)((((2([0-4][0-9]|5[0-5]))|1[0-9]{2}|[0-9]{1,2})\\.){3}((2([0-4][0-9]|5[0-5]))|1[0-9]{2}|[0-9]{1,2}))$");
    }

    @Override
    public @NotNull String getLabel() {
        return "ip";
    }

    @Override
    public @NotNull String getDescription() {
        return "\u2139\uFE0F about an IP Address";
    }

    @Override
    public @NotNull Category getCategory() {
        return Category.UTILITY;
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "ip", "Enter a valid IPv4 address.", true));
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        String ip = event.getOption("ip").getAsString();

        if (!pattern.matcher(ip).find()) {
            event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a valid IPv4 address.".formatted(ip)).build()).queue();
            return;
        }

        Unirest.get("https://ipinfo.io/%s/json".formatted(ip)).asJsonAsync(new Callback<JsonNode>() {
            @Override
            public void completed(HttpResponse<JsonNode> response) {
                JSONObject data = response.getBody().getObject();
                String rawIp = data.getString("ip");
                String hostname = data.has("hostname") ? data.getString("hostname") : "N/A";
                String[] parts = data.has("org") ? data.getString("org").split(" ") : new String[]{"N/A", "N/A"};
                String asn = parts[0];
                String org = Stream.of(parts).skip(1).collect(Collectors.joining(" "));
                boolean anycast = data.has("anycast") && data.getBoolean("anycast");
                String[] coords = data.getString("loc").split(",");
                String lat = coords[0];
                String lon = coords[1];
                String country = data.getString("country");
                String region = data.getString("region");
                String city = data.getString("city");
                String postal = data.has("postal") ? data.getString("postal") : "N/A";
                event.getHook().editOriginalEmbeds(new Embed(SUCCESS)
                        .setTitle(rawIp)
                        .setDescription("""
                                Hostname: `%s`
                                ASN: `%s`
                                Org: `%s`
                                Anycast? `%b`
                                """.formatted(hostname, asn, org, anycast))
                        .addField("Location", """
                                Latitude: `%s`
                                Longitude: `%s`
                                Country: `%s`
                                Region: `%s`
                                City: `%s`
                                Postal: `%s`
                                """.formatted(lat, lon, country, region, city, postal), true)
                        .setThumbnail("https://static-maps.yandex.ru/1.x/?lang=en-US&ll=%1$s,%2$s&z=4&l=map&size=450,450&pt=%1$s,%2$s,pm2rdl".formatted(lon, lat))
                        .setImage("https://static-maps.yandex.ru/1.x/?lang=en-US&ll=%1$s,%2$s&z=10&l=map&size=650,450&pt=%1$s,%2$s,pm2rdl".formatted(lon, lat))
                        .setFooter("Powered by ipinfo.io and yandex.ru").build()).queue();
            }

            @Override
            public void failed(UnirestException e) {
                event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` occurred whilst running the command.").build()).queue();
            }

            @Override
            public void cancelled() {
                event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("An error occurred whilst running the command.").build()).queue();
            }
        });
    }
}
