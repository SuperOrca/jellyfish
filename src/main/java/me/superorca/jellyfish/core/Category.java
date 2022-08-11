package me.superorca.jellyfish.core;

import lombok.Getter;

@Getter
public enum Category {
    ANIMALS(":dog:", "Animals"),
    MISC(":gear:", "Misc");

    private final String emoji;
    private final String name;

    Category(String emoji, String name) {
        this.emoji = emoji;
        this.name = name;
    }
}
