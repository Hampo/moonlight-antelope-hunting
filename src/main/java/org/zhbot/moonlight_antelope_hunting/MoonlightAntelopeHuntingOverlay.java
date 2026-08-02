package org.zhbot.moonlight_antelope_hunting;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

import javax.inject.Inject;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class MoonlightAntelopeHuntingOverlay extends Overlay {
    private static final Map<Integer, Integer> PITFALL_VARBIT_MAP = Map.of(
            ObjectID.HUNTING_PITFALL_22, VarbitID.HUNT_PITFALL_STATE22,
            ObjectID.HUNTING_PITFALL_23, VarbitID.HUNT_PITFALL_STATE23,
            ObjectID.HUNTING_PITFALL_24, VarbitID.HUNT_PITFALL_STATE24,
            ObjectID.HUNTING_PITFALL_25, VarbitID.HUNT_PITFALL_STATE25
    );

    private static final Set<Integer> LOG_IDS = Set.of(
            ItemID.OAK_LOGS,
            ItemID.WILLOW_LOGS,
            ItemID.MAPLE_LOGS,
            ItemID.YEW_LOGS,
            ItemID.MAGIC_LOGS
    );

    private static final Set<WorldPoint> INVALID_TILES = Set.of(
            new WorldPoint(1555, 9421, 0),
            new WorldPoint(1556, 9421, 0),

            new WorldPoint(1555, 9418, 0),
            new WorldPoint(1556, 9418, 0),

            new WorldPoint(1559, 9416, 0),
            new WorldPoint(1559, 9415, 0),

            new WorldPoint(1562, 9416, 0),
            new WorldPoint(1562, 9415, 0),

            new WorldPoint(1564, 9416, 0),
            new WorldPoint(1565, 9416, 0),
            new WorldPoint(1564, 9415, 0),
            new WorldPoint(1565, 9415, 0),

            new WorldPoint(1564, 9419, 0),
            new WorldPoint(1565, 9419, 0),

            new WorldPoint(1565, 9425, 0),
            new WorldPoint(1565, 9424, 0),

            new WorldPoint(1562, 9425, 0),
            new WorldPoint(1562, 9424, 0)
    );

    private final Client client;
    private final MoonlightAntelopeHuntingPlugin plugin;
    private final MoonlightAntelopeHuntingConfig config;

    @Inject
    private MoonlightAntelopeHuntingOverlay(Client client, MoonlightAntelopeHuntingPlugin plugin, MoonlightAntelopeHuntingConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!plugin.inHunterArea())
            return null;

        var inventory = client.getItemContainer(InventoryID.INV);
        if (inventory == null)
            return null;

        var logCount = inventory.count(ItemID.LOGS);

        if (config.rootsEnabled())
        {
            Color logColour;
            if (logCount == 0)
                logColour = config.rootsEmptyColour();
            else if (logCount < 3)
                logColour = config.rootsLowColour();
            else
                logColour = config.rootsFullColour();

            for (var root : plugin.getRootGameObjects())
            {
                renderObject(graphics, root, logColour);
                renderText(graphics, root, logCount + "/3", logColour);
            }
        }

        for (var logId : LOG_IDS)
            logCount += inventory.count(logId);

        if (config.pitfallEnabled())
        {
            var pitColour = logCount > 0 ? config.pitfallDefaultColour() : config.pitfallEmptyColour();

            for (var pit : plugin.getPitGameObjects())
            {
                var varbitId = PITFALL_VARBIT_MAP.get(pit.getId());
                var trapped = varbitId != null && client.getVarbitValue(varbitId) != 0;
                renderObject(graphics, pit, trapped ? config.pitfallTrappedColour() : pitColour);
            }
        }

        if (config.antelopesEnabled())
        {
            for (var npc : plugin.getActiveNPCs())
            {
                if (config.antelopesRenderMode().isShowHull())
                    renderNPC(graphics, npc, config.antelopesColour());
                if (config.antelopesRenderMode().isShowTile())
                    renderTile(graphics, npc, config.antelopesColour());
            }

            var currentTick = client.getTickCount();
            var iterator = plugin.getDeadNPCs().iterator();
            while (iterator.hasNext())
            {
                var npc = iterator.next();

                var ticksLeft = npc.getRespawnTick() - currentTick;
                if (ticksLeft <= 0)
                {
                    iterator.remove();
                    continue;
                }

                renderTile(graphics, npc.getLocation(), config.antelopesRespawnColour());
                renderText(graphics, npc.getLocation(), String.valueOf(ticksLeft), config.antelopesRespawnColour());
            }
        }

        if (config.invalidTilesEnabled())
            for (var tile : INVALID_TILES)
                renderTile(graphics, tile, config.invalidTilesColour());

        return null;
    }

    private void renderObject(Graphics2D graphics, GameObject gameObject, Color color)
    {
        var area = gameObject.getClickbox();
        var mousePosition = client.getMouseCanvasPosition();

        var borderColour = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        OverlayUtil.renderHoverableArea(graphics, area, mousePosition, color, borderColour, borderColour.darker());
    }

    private void renderText(Graphics2D graphics, GameObject gameObject, String text, Color color)
    {
        renderText(graphics, gameObject.getLocalLocation(), text, color);
    }

    private void renderText(Graphics2D graphics, WorldPoint worldPoint, String text, Color color)
    {
        renderText(graphics, LocalPoint.fromWorld(client, worldPoint), text, color);
    }

    private void renderText(Graphics2D graphics, LocalPoint localPoint, String text, Color color)
    {
        var point = Perspective.getCanvasTextLocation(client, graphics, localPoint, text, 0);
        if (point == null)
            return;

        OverlayUtil.renderTextLocation(graphics, point, text, color);
    }

    private void renderNPC(Graphics2D graphics, NPC npc, Color color)
    {
        var hull = npc.getConvexHull();
        var mousePosition = client.getMouseCanvasPosition();

        var borderColour = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        OverlayUtil.renderHoverableArea(graphics, hull, mousePosition, color, borderColour, borderColour.darker());
    }

    private void renderTile(Graphics2D graphics, WorldPoint worldPoint, Color color)
    {
        var localPoint = LocalPoint.fromWorld(client, worldPoint);
        if (localPoint == null)
            return;

        var tilePoly = Perspective.getCanvasTilePoly(client, localPoint);
        if (tilePoly == null)
            return;

        OverlayUtil.renderPolygon(graphics, tilePoly, color);
    }

    private void renderTile(Graphics2D graphics, NPC npc, Color color)
    {
        var tilePoly = npc.getCanvasTilePoly();
        if (tilePoly == null)
            return;

        OverlayUtil.renderPolygon(graphics, tilePoly, color);
    }
}
