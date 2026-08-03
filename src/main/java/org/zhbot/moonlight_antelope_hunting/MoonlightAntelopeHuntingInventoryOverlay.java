package org.zhbot.moonlight_antelope_hunting;

import net.runelite.api.Client;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.WidgetItemOverlay;

import javax.inject.Inject;
import java.awt.*;

public class MoonlightAntelopeHuntingInventoryOverlay extends WidgetItemOverlay {
    private final Client client;
    private final MoonlightAntelopeHuntingPlugin plugin;
    private final MoonlightAntelopeHuntingConfig config;

    @Inject
    private MoonlightAntelopeHuntingInventoryOverlay(Client client, MoonlightAntelopeHuntingPlugin plugin, MoonlightAntelopeHuntingConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;

        showOnInventory();
        setPriority(2f);
    }

    @Override
    public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem widgetItem) {
        if (!plugin.inHunterArea())
            return;

        switch (itemId)
        {
            case ItemID.CHISEL:
            case /*ItemID.JEWELLERS_CHISEL*/34024:
                if (!config.inventoryChiselEnabled())
                    return;

                renderBox(graphics, widgetItem, config.inventoryChiselColour());

                break;
            case ItemID.HUNTING_ANTELOPEMOON_HORN:
                if (!config.inventoryHornEnabled())
                    return;

                renderBox(graphics, widgetItem, config.inventoryHornColour());

                break;
        }
    }

    public void renderBox(Graphics2D graphics, WidgetItem widgetItem, Color color)
    {
        var bounds = widgetItem.getCanvasBounds();
        if (bounds == null || bounds.width <= 0 || bounds.height <= 0)
            return;
        var mousePosition = client.getMouseCanvasPosition();

        var borderColour = new Color(color.getRed(), color.getGreen(), color.getBlue(), 255);
        OverlayUtil.renderHoverableArea(graphics, bounds, mousePosition, color, borderColour, borderColour.darker());
    }
}
