package org.zhbot.moonlight_antelope_hunting;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
public class DeadNPC {
    private final int index;
    private final WorldPoint location;
    private final int respawnTick;

    public DeadNPC(int index, WorldPoint location, int respawnTick) {
        this.index = index;
        this.location = location;
        this.respawnTick = respawnTick;
    }
}
