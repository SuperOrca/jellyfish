package me.superorca.jellyfish.core.embed;

import lombok.Getter;

import java.awt.*;

@Getter
public enum EmbedColor {
    DEFAULT(149, 225, 211),
    ERROR(243, 129, 129),
    SUCCESS(234, 255, 208),
    WARNING(252, 227, 138);

    private final Color color;

    EmbedColor(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }
}
