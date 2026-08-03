package org.zhbot.moonlight_antelope_hunting;

import net.runelite.client.config.*;

import java.awt.*;

@ConfigGroup(MoonlightAntelopeHuntingConfig.group)
public interface MoonlightAntelopeHuntingConfig extends Config
{
	String group = "moonlight-antelope-hunting";

	@ConfigSection(
			name = "Roots",
			description = "Roots settings",
			position = 0
	)
	String rootsSection = "rootsSection";

	@ConfigItem(
			keyName = "rootsEnabled",
			name = "Overlay Enabled",
			description = "Enable roots overlay",
			section = rootsSection,
			position = 0
	)
	default boolean rootsEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "rootsEmptyColour",
			name = "Empty Colour",
			description = "Roots overlay colour when empty",
			section = rootsSection,
			position = 1
	)
	default Color rootsEmptyColour()
	{
		return new Color(255, 0, 0, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "rootsLowColour",
			name = "Low Colour",
			description = "Roots overlay colour when low",
			section = rootsSection,
			position = 2
	)
	default Color rootsLowColour()
	{
		return new Color(255, 255, 0, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "rootsFullColour",
			name = "Full Colour",
			description = "Roots overlay colour when full",
			section = rootsSection,
			position = 3
	)
	default Color rootsFullColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigSection(
			name = "Pitfall",
			description = "Pitfall settings",
			position = 1
	)
	String pitfallSection = "pitfallSection";

	@ConfigItem(
			keyName = "pitfallEnabled",
			name = "Overlay Enabled",
			description = "Enable pitfall overlay",
			section = pitfallSection,
			position = 0
	)
	default boolean pitfallEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "pitfallDefaultColour",
			name = "Default Colour",
			description = "Pitfall overlay colour when ready to be trapped",
			section = pitfallSection,
			position = 1
	)
	default Color pitfallDefaultColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "pitfallEmptyColour",
			name = "Empty Colour",
			description = "Pitfall overlay colour when no logs",
			section = pitfallSection,
			position = 2
	)
	default Color pitfallEmptyColour()
	{
		return new Color(255, 0, 0, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "pitfallTrappedColour",
			name = "Trapped Colour",
			description = "Pitfall overlay colour when trapped",
			section = pitfallSection,
			position = 3
	)
	default Color pitfallTrappedColour()
	{
		return new Color(0, 255, 255, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "pitfallCaughtColour",
			name = "Caught Colour",
			description = "Pitfall overlay colour when caught",
			section = pitfallSection,
			position = 4
	)
	default Color pitfallCaughtColour()
	{
		return new Color(255, 0, 255, 50);
	}

	@ConfigSection(
			name = "Antelopes",
			description = "Antelope settings",
			position = 2
	)
	String antelopesSection = "antelopesSection";

	@ConfigItem(
			keyName = "antelopesEnabled",
			name = "Overlay Enabled",
			description = "Enable antelopes overlay",
			section = antelopesSection,
			position = 0
	)
	default boolean antelopesEnabled()
	{
		return true;
	}

	@ConfigItem(
			keyName = "antelopesRenderMode",
			name = "Render Mode",
			description = "The mode to render antelopes",
			section = antelopesSection,
			position = 1
	)
	default NPCRenderMode antelopesRenderMode()
	{
		return NPCRenderMode.HULL;
	}

	@Alpha
	@ConfigItem(
			keyName = "antelopesColour",
			name = "Colour",
			description = "Antelope overlay colour",
			section = antelopesSection,
			position = 2
	)
	default Color antelopesColour()
	{
		return new Color(0, 255, 255, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "antelopesTauntedColour",
			name = "Taunted Colour",
			description = "Antelope taunted overlay colour",
			section = antelopesSection,
			position = 3
	)
	default Color antelopesTauntedColour()
	{
		return new Color(255, 0, 255, 50);
	}

	@Alpha
	@ConfigItem(
			keyName = "antelopesRespawnColour",
			name = "Respawn Colour",
			description = "Respawn tile colour",
			section = antelopesSection,
			position = 4
	)
	default Color antelopesRespawnColour()
	{
		return new Color(0, 255, 255);
	}

	@ConfigItem(
			keyName = "antelopesRemoveTease",
			name = "Remove Tease",
			description = "Removes the Tease option on already trapped antelopes",
			section = antelopesSection,
			position = 6
	)
	default boolean antelopesRemoveTease()
	{
		return true;
	}

	@ConfigSection(
			name = "Inventory",
			description = "Inventory settings",
			position = 3
	)
	String inventorySection = "inventorySection";

	@ConfigItem(
			keyName = "inventoryChiselEnabled",
			name = "Highlight Chisel",
			description = "Enable chisel overlay",
			section = inventorySection,
			position = 0
	)
	default boolean inventoryChiselEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "inventoryChiselColour",
			name = "Chisel Colour",
			description = "Chisel overlay colour in inventory",
			section = inventorySection,
			position = 1
	)
	default Color inventoryChiselColour()
	{
		return new Color(0, 255, 0, 50);
	}

	@ConfigItem(
			keyName = "inventoryHornEnabled",
			name = "Highlight Horn",
			description = "Enable horn overlay",
			section = inventorySection,
			position = 2
	)
	default boolean inventoryHornEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "inventoryHornColour",
			name = "Horn Colour",
			description = "Horn overlay colour in inventory",
			section = inventorySection,
			position = 3
	)
	default Color inventoryHornColour()
	{
		return new Color(0, 255, 255, 50);
	}

	@ConfigSection(
			name = "Miscellaneous",
			description = "Miscellaneous settings",
			position = 4
	)
	String miscellaneousSection = "miscellaneousSection";

	@ConfigItem(
			keyName = "invalidTilesEnabled",
			name = "Show invalid pit tiles",
			description = "Show the pit tiles that are unwalkable",
			section = miscellaneousSection,
			position = 0
	)
	default boolean invalidTilesEnabled()
	{
		return true;
	}

	@Alpha
	@ConfigItem(
			keyName = "invalidTilesColour",
			name = "Invalid Tiles Colour",
			description = "Invalid tiles colour",
			section = miscellaneousSection,
			position = 1
	)
	default Color invalidTilesColour()
	{
		return new Color(255, 0, 0, 50);
	}
}
