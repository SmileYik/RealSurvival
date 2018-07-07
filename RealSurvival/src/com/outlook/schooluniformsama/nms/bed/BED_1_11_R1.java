package com.outlook.schooluniformsama.nms.bed;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_11_R1.PacketPlayOutBed;

public class BED_1_11_R1 implements BedNMS{
	
	private PacketPlayOutAnimation ppoa;
	private PacketPlayOutBed ppob;
	
	@Override
	public void sleep(Player p, Location l) {
		if(l.getBlock().getType() == Material.BED_BLOCK){
			ppob = new PacketPlayOutBed(((CraftPlayer) p).getHandle(), new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppob);
		}else{
			Material m = l.getBlock().getType();
			l.getBlock().setType(Material.BED_BLOCK);
			Bukkit.getScheduler().runTaskLater(RealSurvival.plugin, new Runnable() {
				@Override
				public void run() {
					ppob = new PacketPlayOutBed(((CraftPlayer) p).getHandle(), new BlockPosition(l.getBlockX(), l.getBlockY(), l.getBlockZ()));
					((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppob);
					l.getBlock().setType(m);
				}
			}, 3);			
		}
	}

	@Override
	public void level(Player p) {
		ppoa = new PacketPlayOutAnimation(((CraftPlayer)p).getHandle(),2 );
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppoa);
	}

}
