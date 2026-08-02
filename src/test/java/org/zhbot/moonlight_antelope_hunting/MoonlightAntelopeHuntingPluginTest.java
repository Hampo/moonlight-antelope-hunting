package org.zhbot.moonlight_antelope_hunting;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class MoonlightAntelopeHuntingPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(MoonlightAntelopeHuntingPlugin.class);
		RuneLite.main(args);
	}
}