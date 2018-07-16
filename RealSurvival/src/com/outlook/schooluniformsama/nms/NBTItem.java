package com.outlook.schooluniformsama.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.outlook.schooluniformsama.RealSurvival;

public class NBTItem {
	private static String version = RealSurvival.getVersion();
	//Class
	private static Class<?> CraftItemStack;
	private static Class<?> ItemStack;
	private static Class<?> NBTTagCompound;
	private static Class<?> NBTTagString;
	private static Class<?> NBTBase;
	
	private static Constructor<?> NBTTagStringConstructor;
	private static Constructor<?> NBTTagCompoundConstructor;
	
	private static Method asNMSCopy;
	private static Method hasTag;
	private static Method getTag;
	private static Method set;
	private static Method setTag;
	private static Method asBukkitCopy;
	private static Method getString;
	private static Method hasKey;
	
	public static void init(){
		try {
			CraftItemStack = Class.forName("org.bukkit.craftbukkit."+version+".inventory.CraftItemStack");
			ItemStack = Class.forName("net.minecraft.server."+version+".ItemStack");
			NBTTagCompound = Class.forName("net.minecraft.server."+version+".NBTTagCompound");
			NBTTagString = Class.forName("net.minecraft.server."+version+".NBTTagString");
			NBTBase = Class.forName("net.minecraft.server."+version+".NBTBase");
			
			NBTTagStringConstructor  = NBTTagString.getConstructor(String.class);
			NBTTagCompoundConstructor = NBTTagCompound.getConstructor();
			
			asNMSCopy = CraftItemStack.getMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
			hasTag = ItemStack.getMethod("hasTag");
			getTag = ItemStack.getMethod("getTag");
			set = NBTTagCompound.getMethod("set", String.class,NBTBase);
			setTag = ItemStack.getMethod("setTag", NBTTagCompound);
			asBukkitCopy = CraftItemStack.getMethod("asBukkitCopy", ItemStack);
			getString = NBTTagCompound.getMethod("getString", String.class);
			hasKey = NBTTagCompound.getMethod("hasKey", String.class);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static org.bukkit.inventory.ItemStack setNBT(org.bukkit.inventory.ItemStack is, String key, String value){
		try {
			Object item = asNMSCopy.invoke(CraftItemStack,is);
			Object nbttc;
			if((boolean)hasTag.invoke(item))
				nbttc = getTag.invoke(item);
			else
				nbttc = NBTTagCompoundConstructor.newInstance();
			Object nbtts = NBTTagStringConstructor.newInstance(value);
			set.invoke(nbttc, key,nbtts);
			setTag.invoke(item, nbttc);
			return (org.bukkit.inventory.ItemStack) asBukkitCopy.invoke(CraftItemStack,item);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNBTValue(org.bukkit.inventory.ItemStack is, String key) {
		try {
			Object item = asNMSCopy.invoke(CraftItemStack,is);
			if(!(boolean)hasTag.invoke(item))return null;
			Object nbttc = getTag.invoke(item);
			if(!(boolean)hasKey.invoke(nbttc, key))return null;
			return (String) getString.invoke(nbttc, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
/*	import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;

	import net.minecraft.server.v1_11_R1.ItemStack;
	import net.minecraft.server.v1_11_R1.NBTTagCompound;
	import net.minecraft.server.v1_11_R1.NBTTagString;*/
}
