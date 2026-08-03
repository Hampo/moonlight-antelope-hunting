package org.zhbot.moonlight_antelope_hunting;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.gameval.NpcID;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "Moonlight Antelope Hunting"
)
public class MoonlightAntelopeHuntingPlugin extends Plugin
{
	private static final WorldPoint CENTER = new WorldPoint(1561, 9421, 0);
	private static final int DISTANCE = 10;

	private static final int ANTELOPE_RESPAWN_TIME = 45;

	private static final Set<Integer> LOG_IDS = Set.of(
			net.runelite.api.gameval.ItemID.LOGS,
			net.runelite.api.gameval.ItemID.OAK_LOGS,
			net.runelite.api.gameval.ItemID.WILLOW_LOGS,
			net.runelite.api.gameval.ItemID.MAPLE_LOGS,
			net.runelite.api.gameval.ItemID.YEW_LOGS,
			ItemID.MAGIC_LOGS
	);

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private MoonlightAntelopeHuntingConfig config;

	@Inject
	private MoonlightAntelopeHuntingOverlay overlay;

	@Getter
	private final List<GameObject> rootGameObjects = new ArrayList<>();

	@Getter
	private final List<GameObject> pitGameObjects = new ArrayList<>();

	@Getter
	private final List<NPC> activeNPCs = new ArrayList<>();

	@Getter
	private final List<DeadNPC> deadNPCs = new ArrayList<>();

	private final Map<Integer, WorldPoint> spawnLocations = new HashMap<>();

	@Getter
	private final Map<Integer, Integer> logCounts = new HashMap<>();

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);

		rootGameObjects.clear();
		pitGameObjects.clear();

		activeNPCs.clear();
		deadNPCs.clear();
		spawnLocations.clear();

		logCounts.clear();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		GameState gameState = event.getGameState();
		if (gameState == GameState.LOADING)
		{
			rootGameObjects.clear();
			pitGameObjects.clear();

			activeNPCs.clear();
			deadNPCs.clear();
			spawnLocations.clear();
		}
	}

	@Subscribe
	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		var gameObject = event.getGameObject();
		if (!inHunterArea(gameObject.getWorldLocation()))
			return;

		switch (gameObject.getId())
		{
			case ObjectID.HG_CAVEKIT_ROOTS01_BOTTOM01:
				rootGameObjects.add(gameObject);
				break;
			case ObjectID.HUNTING_PITFALL_22:
			case ObjectID.HUNTING_PITFALL_23:
			case ObjectID.HUNTING_PITFALL_24:
			case ObjectID.HUNTING_PITFALL_25:
				pitGameObjects.add(gameObject);
				break;
		}
	}

	@Subscribe
	private void onGameObjectDespawned(GameObjectDespawned event)
	{
		var gameObject = event.getGameObject();

		switch (gameObject.getId())
		{
			case ObjectID.HG_CAVEKIT_ROOTS01_BOTTOM01:
				rootGameObjects.remove(gameObject);
				break;
			case ObjectID.HUNTING_PITFALL_22:
			case ObjectID.HUNTING_PITFALL_23:
			case ObjectID.HUNTING_PITFALL_24:
			case ObjectID.HUNTING_PITFALL_25:
				pitGameObjects.remove(gameObject);
				break;
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event) {
		NPC npc = event.getNpc();
		if (npc.getId() != NpcID.MOONLIGHT_ANTELOPE)
			return;

		activeNPCs.add(npc);
		spawnLocations.putIfAbsent(npc.getIndex(), npc.getWorldLocation());
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event) {
		NPC npc = event.getNpc();
		if (npc.getId() == NpcID.MOONLIGHT_ANTELOPE) {
			activeNPCs.remove(npc);

			var spawnTile = spawnLocations.get(npc.getIndex());
			if (spawnTile == null)
				spawnTile = npc.getWorldLocation();
			spawnLocations.remove(npc.getIndex());

			deadNPCs.add(new DeadNPC(spawnTile, client.getTickCount() + ANTELOPE_RESPAWN_TIME));
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.INV)
			return;

		var inventory = event.getItemContainer();

		for (var logId : LOG_IDS)
			logCounts.put(logId, inventory.count(logId));
	}

	@Provides
	MoonlightAntelopeHuntingConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(MoonlightAntelopeHuntingConfig.class);
	}

	public boolean inHunterArea()
	{
		var localPlayer = client.getLocalPlayer();
		if (localPlayer == null)
			return false;

		return inHunterArea(localPlayer.getWorldLocation());
	}

	public boolean inHunterArea(WorldPoint point)
	{
		return point.distanceTo(CENTER) <= DISTANCE;
	}
}
