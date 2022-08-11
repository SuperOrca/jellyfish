package me.superorca.jellyfish;

import com.google.gson.Gson;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import lombok.Getter;
import me.superorca.jellyfish.core.Registry;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import okhttp3.OkHttpClient;

import javax.security.auth.login.LoginException;
import java.io.Closeable;
import java.time.Instant;

// https://colorhunt.co/palette/f38181fce38aeaffd095e1d3
// https://unsplash.com/photos/KLbUohEjb04
@Getter
public class Jellyfish implements Closeable {
    private final Jellyfish instance;
    private final Gson gson;
    private final OkHttpClient httpClient;
    private final AnnotatedEventManager eventManager;
    private final ShardManager shardManager;
    private final Instant startTime;
    private final EventWaiter waiter;

    public Jellyfish() throws LoginException {
        instance = this;
        gson = new Gson();
        httpClient = new OkHttpClient();
        eventManager = new AnnotatedEventManager();
        waiter = new EventWaiter();
        shardManager = DefaultShardManagerBuilder.createDefault(Config.get("token")).setStatus(OnlineStatus.ONLINE).setActivity(Activity.playing("/help")).enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS).addEventListeners(waiter, new Registry(this)).setAutoReconnect(true).build();
        startTime = Instant.now();
    }

    public static void main(String[] args) throws LoginException {
        new Jellyfish();
    }

    @Override
    public void close() {
        if (shardManager != null) shardManager.shutdown();
    }
}