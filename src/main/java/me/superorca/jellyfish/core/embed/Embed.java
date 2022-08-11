package me.superorca.jellyfish.core.embed;

import net.dv8tion.jda.api.EmbedBuilder;

import java.time.Instant;

public class Embed extends EmbedBuilder {
    private final EmbedColor status;

    public Embed() {
        this.status = EmbedColor.DEFAULT;
        this.setColor(status.getColor());
        this.setTimestamp(Instant.now());
    }

    public Embed(EmbedColor status) {
        this.status = status;
        this.setColor(status.getColor());
        this.setTimestamp(Instant.now());
    }
}
