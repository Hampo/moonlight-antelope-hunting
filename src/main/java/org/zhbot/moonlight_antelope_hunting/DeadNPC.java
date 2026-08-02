package org.zhbot.moonlight_antelope_hunting;

import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@Getter
public class DeadNPC {
    private final WorldPoint location;
    private final int respawnTick;

    public DeadNPC(WorldPoint location, int respawnTick) {
        this.location = location;
        this.respawnTick = respawnTick;
    }
}
