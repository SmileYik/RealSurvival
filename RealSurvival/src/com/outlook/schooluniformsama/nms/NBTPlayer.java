package com.outlook.schooluniformsama.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;

public class NBTPlayer {
	private static String version = RealSurvival.getVersion();
	private static Class<?> Entity;
	private static Class<?> CraftPlayer;
	private static Class<?> EntityHuman;
	private static Class<?> BlockPosition;
	private static Class<?> IChatBaseComponent;
	private static Class<?> ChatComponentText;
	private static Class<?> ChatMessageType;
	private static Class<?> PacketPlayOutChat;
	private static Class<?> PacketPlayOutAnimation;
	private static Class<?> PacketPlayOutBed;
	private static Class<?> PlayerConnection;
	private static Class<?> Packet;

	
	private static Constructor<?> PacketPlayOutBedConstructor;
	private static Constructor<?> PacketPlayOutAnimationConstructor;
	private static Constructor<?> BlockPositionConstructor;
	private static Constructor<?> ChatComponentTextConstructor;
	private static Constructor<?> PacketPlayOutChatConstructor;
	
	private static Method getHandle;
	private static Method sendPacket;
	private static Method chatMessageTypeMethod;
	
	public static void init(){
		try {
			
			CraftPlayer = Class.forName("org.bukkit.craftbukkit."+version+".entity.CraftPlayer");
			BlockPosition = Class.forName("net.minecraft.server."+version+".BlockPosition");
			PacketPlayOutAnimation = Class.forName("net.minecraft.server."+version+".PacketPlayOutAnimation");
			PacketPlayOutBed = Class.forName("net.minecraft.server."+version+".PacketPlayOutBed");
			PlayerConnection = Class.forName("net.minecraft.server."+version+".PlayerConnection");
			Packet = Class.forName("net.minecraft.server."+version+".Packet");
			Entity  = Class.forName("net.minecraft.server."+version+".Entity");
			EntityHuman = Class.forName("net.minecraft.server."+version+".EntityHuman");
			
			PacketPlayOutAnimationConstructor = PacketPlayOutAnimation.getConstructor(Entity,int.class);
			BlockPositionConstructor = BlockPosition.getConstructor(int.class,int.class,int.class);
			
			getHandle = CraftPlayer.getMethod("getHandle");
			sendPacket = PlayerConnection.getMethod("sendPacket", Packet);
			
			
			if(version.equals("v1_7_R4"))
				PacketPlayOutBedConstructor = PacketPlayOutBed.getConstructor(EntityHuman,int.class,int.class,int.class);
			else
				PacketPlayOutBedConstructor = PacketPlayOutBed.getConstructor(EntityHuman,BlockPosition);
			
			if(version.equals("v1_7_R4"))return;
			IChatBaseComponent = Class.forName("net.minecraft.server."+version+".IChatBaseComponent");
			ChatComponentText = Class.forName("net.minecraft.server."+version+".ChatComponentText");
			PacketPlayOutChat = Class.forName("net.minecraft.server."+version+".PacketPlayOutChat");
			
			ChatComponentTextConstructor = ChatComponentText.getConstructor(String.class);
			
			if(Data.versionData[0]>=12){
				ChatMessageType = Class.forName("net.minecraft.server."+version+".ChatMessageType");
				chatMessageTypeMethod = ChatMessageType.getMethod("a",Byte.TYPE);
				PacketPlayOutChatConstructor = PacketPlayOutChat.getConstructor(IChatBaseComponent,ChatMessageType);
			}else{
				PacketPlayOutChatConstructor = PacketPlayOutChat.getConstructor(IChatBaseComponent,byte.class);
			}
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void sleep(Player p, Location l){
		try {
			Object player = getHandle.invoke(CraftPlayer.cast(p));
			Object outBed;
			
			if(version.equals("v1_7_R4"))
				outBed = PacketPlayOutBedConstructor.newInstance(player,l.getBlockX(),l.getBlockY(),l.getBlockZ());
			else
				outBed = PacketPlayOutBedConstructor.newInstance(player,BlockPositionConstructor.newInstance(l.getBlockX(),l.getBlockY(),l.getBlockZ()));
			
			Field con = player.getClass().getDeclaredField("playerConnection");
			Object playerConnection = con.get(player);
			
			
			if(l.getBlock().getType() == Material.BED_BLOCK){
				sendPacket.invoke(playerConnection, outBed);
			}else{
				Material m = l.getBlock().getType();
				l.getBlock().setType(Material.BED_BLOCK);
				Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RealSurvival"), new Runnable() {
					@Override
					public void run() {
						try {
							sendPacket.invoke(playerConnection, outBed);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						l.getBlock().setType(m);
					}
				}, 1);			
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void leaveBed(Player p){
		try {
			Object player = getHandle.invoke(CraftPlayer.cast(p));
			Field con = player.getClass().getDeclaredField("playerConnection");
			Object playerConnection = con.get(player);
			Object outAnimation = PacketPlayOutAnimationConstructor.newInstance(player,2);
			sendPacket.invoke(playerConnection, outAnimation);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean sendActionBar(Player p,String msg){
		if(version.equals("v1_7_R4"))return false;
		try {
			Object player = getHandle.invoke(CraftPlayer.cast(p));
			Field con = player.getClass().getDeclaredField("playerConnection");
			Object playerConnection = con.get(player);
			Object ppoc;

			if(Data.versionData[0]>=12)
				ppoc = PacketPlayOutChatConstructor.newInstance(ChatComponentTextConstructor.newInstance(msg),chatMessageTypeMethod.invoke(ChatMessageType, (byte)2));
			else ppoc = PacketPlayOutChatConstructor.newInstance(ChatComponentTextConstructor.newInstance(msg),(byte)2);
			sendPacket.invoke(playerConnection, ppoc);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
}
