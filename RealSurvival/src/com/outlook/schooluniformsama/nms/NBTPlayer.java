package com.outlook.schooluniformsama.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.RealSurvival;
import com.outlook.schooluniformsama.data.Data;

public class NBTPlayer {
	private static String version = RealSurvival.getVersion();
	private static Class<?> CraftPlayer;
	private static Class<?> IChatBaseComponent;
	private static Class<?> ChatComponentText;
	private static Class<?> ChatMessageType;
	private static Class<?> PacketPlayOutChat;
	private static Class<?> PlayerConnection;
	private static Class<?> Packet;


	private static Constructor<?> ChatComponentTextConstructor;
	private static Constructor<?> PacketPlayOutChatConstructor;

	private static Method getHandle;
	private static Method sendPacket;
	private static Method chatMessageTypeMethod;

	public static void init(){
		try {
			
			CraftPlayer = Class.forName("org.bukkit.craftbukkit."+version+".entity.CraftPlayer");
			PlayerConnection = Class.forName("net.minecraft.server."+version+".PlayerConnection");
			Packet = Class.forName("net.minecraft.server."+version+".Packet");

			getHandle = CraftPlayer.getMethod("getHandle");
			sendPacket = PlayerConnection.getMethod("sendPacket", Packet);

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
			
			
		} catch (ClassNotFoundException | SecurityException | NoSuchMethodException e) {
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
