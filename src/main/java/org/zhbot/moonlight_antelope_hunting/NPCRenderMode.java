package org.zhbot.moonlight_antelope_hunting;

import lombok.Getter;

public enum NPCRenderMode {
    HULL("Hull", true, false),
    TILE("Tile", false, true),
    BOTH("Both", true, true);

    private final String name;

    @Getter
    private final boolean showHull;

    @Getter
    private final boolean showTile;

    NPCRenderMode(String name, boolean showHull, boolean showTile)
    {
        this.name = name;
        this.showHull = showHull;
        this.showTile = showTile;
    }

    @Override
    public String toString() {
        return name;
    }
}
