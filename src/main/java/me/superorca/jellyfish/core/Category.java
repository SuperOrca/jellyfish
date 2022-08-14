package me.superorca.jellyfish.core;

import lombok.Getter;

@Getter
public enum Category {
    ANIMALS(":dog:", "Animals"),
    FUN(":smile:", "Fun"),
    MISC(":gear:", "Misc"),
    UTILITY(":tools:", "Utility");

    private final String emoji;
    private final String name;

    Category(String emoji, String name) {
        this.emoji = emoji;
        this.name = name;
    }
}
