package me.superorca.jellyfish.modules.utility;

import com.google.gson.JsonObject;
import me.superorca.jellyfish.Jellyfish;
import me.superorca.jellyfish.core.Category;
import me.superorca.jellyfish.core.Command;
import me.superorca.jellyfish.core.embed.Embed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static me.superorca.jellyfish.core.embed.EmbedColor.ERROR;

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
        return List.of(new OptionData(OptionType.STRING, "ip", "A valid IPv4 address.", true));
    }

    @Override
    public void execute(@NotNull SlashCommandEvent event) {
        String ip = event.getOption("ip").getAsString();

        if (!pattern.matcher(ip).find()) {
            event.getHook().editOriginalEmbeds(new Embed(ERROR).setDescription("`%s` is not a valid IPv4 address.".formatted(ip)).build()).queue();
            return;
        }

        Request request = new Request.Builder().url("https://ipinfo.io/%s/json".formatted(ip)).build();
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
                String rawIp = jsonObject.get("ip").getAsString();
                String hostname = jsonObject.has("hostname") ? jsonObject.get("hostname").getAsString() : "N/A";
                String[] parts = jsonObject.get("org").getAsString().split(" ");
                String asn = parts[0];
                String org = Stream.of(parts).skip(1).collect(Collectors.joining(" "));
                boolean anycast = jsonObject.has("anycast") && jsonObject.get("anycast").getAsBoolean();
                String[] coords = jsonObject.get("loc").getAsString().split(",");
                String lat = coords[0];
                String lon = coords[1];
                String country = jsonObject.get("country").getAsString();
                String region = jsonObject.get("region").getAsString();
                String city = jsonObject.get("city").getAsString();
                String postal = jsonObject.has("postal") ? jsonObject.get("postal").getAsString() : "N/A";
                event.getHook().editOriginalEmbeds(new Embed()
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
        });
    }
}
