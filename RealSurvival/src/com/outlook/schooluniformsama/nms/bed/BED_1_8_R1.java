package com.outlook.schooluniformsama.nms.bed;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R1.PacketPlayOutBed;

public class BED_1_8_R1 implements BedNMS{
	
	private PacketPlayOutAnimation ppoa;
	private PacketPlayOutBed ppob;
	
	@Override
	public void sleep(Player p, Location l) {
		ppob = new PacketPlayOutBed(((CraftPlayer) p).getHandle(), new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppob);
	}

	@Override
	public void level(Player p) {
		ppoa = new PacketPlayOutAnimation(((CraftPlayer)p).getHandle(),2 );
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoa);
	}

}
