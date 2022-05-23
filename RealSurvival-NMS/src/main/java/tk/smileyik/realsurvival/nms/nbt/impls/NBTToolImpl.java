package tk.smileyik.realsurvival.nms.nbt.impls;

import tk.smileyik.realsurvival.nms.NMSFactory;
import tk.smileyik.realsurvival.nms.nbt.NBTTool;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NBTToolImpl implements NBTTool {
  private static Class<?> CraftItemStack;
  private static Class<?> ItemStack;
  private static Class<?> NBTTagCompound;

  // NBTTagCompound constructor
  private static Constructor<?> newNBTTagCompound;

  // CraftItemStack methods
  /**
   * static
   * args: org.bukkit.inventory.ItemStack
   * return: ItemStack
   */
  private static Method asNMSCopy;
  /**
   * static
   * args: ItemStack
   * return: org.bukkit.inventory.ItemStack
   */
  private static Method asBukkitCopy;

  // ItemStack methods
  /**
   * return: boolean
   */
  private static Method hasTag;
  /**
   * return: NBTTagCompound
   */
  private static Method getTag;
  /**
   * args NBTTagCompound
   */
  private static Method setTag;

  //NBTTagCompound method
//  /**
//   * args: String int
//   */
//  private final Method setInt;
//  private final Method setDouble;
//  private final Method setBoolean;
//  private final Method setIntArray;
//  private final Method setByte;
//  private final Method setByteArray;
//  private final Method setFloat;
//  private final Method setLong;
//  private final Method setShort;
  private static Method setString;
  private static Method getString;
  /**
   * args: String
   * return: boolean
   */
  private static Method hasKey;

  static {
    String version = NMSFactory.getVersion();
    try {
      CraftItemStack = Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
      ItemStack      = Class.forName("net.minecraft.server." + version + ".ItemStack");
      NBTTagCompound = Class.forName("net.minecraft.server." + version + ".NBTTagCompound");

      // NBTTagCompound constructor
      newNBTTagCompound = NBTTagCompound.getDeclaredConstructor();

      // CraftItemStack methods
      asNMSCopy    = CraftItemStack.getDeclaredMethod("asNMSCopy", org.bukkit.inventory.ItemStack.class);
      asBukkitCopy = CraftItemStack.getDeclaredMethod("asBukkitCopy", ItemStack);

      // ItemStack methods
      hasTag = ItemStack.getDeclaredMethod("hasTag");
      getTag = ItemStack.getDeclaredMethod("getTag");
      setTag = ItemStack.getDeclaredMethod("setTag", NBTTagCompound);

      // NBTTagCompound method
      setString = NBTTagCompound.getDeclaredMethod("setString", String.class, String.class);
      getString = NBTTagCompound.getDeclaredMethod("getString", String.class);
      hasKey    = NBTTagCompound.getDeclaredMethod("hasKey", String.class);
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  private Object craftItemStack;
  private Object nbtTagCompound;

  private NBTToolImpl(org.bukkit.inventory.ItemStack itemStack) {
    try {
      craftItemStack = asNMSCopy.invoke(null, itemStack);
      nbtTagCompound = ((boolean) hasTag.invoke(craftItemStack)) ?
              getTag.invoke(craftItemStack) : newNBTTagCompound.newInstance();
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
      e.printStackTrace();
    }
  }

  public static NBTTool newNBTTool(org.bukkit.inventory.ItemStack itemStack) {
    return new NBTToolImpl(itemStack);
  }

  public NBTTool setNBT(String key, String value) {
    try {
      setString.invoke(nbtTagCompound, key, value);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return this;
  }

  public org.bukkit.inventory.ItemStack asBukkitItem() {
    try {
      setTag.invoke(craftItemStack, nbtTagCompound);
      return (org.bukkit.inventory.ItemStack)
              asBukkitCopy.invoke(null, craftItemStack);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean hasNBT(String key) {
    try {
      return (boolean) hasKey.invoke(nbtTagCompound, key);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return false;
  }

  public String getNBTString(String key) {
    if (hasNBT(key)) {
      try {
        return (String) getString.invoke(nbtTagCompound, key);
      } catch (IllegalAccessException | InvocationTargetException e) {
        e.printStackTrace();
      }
    }
    return null;
  }


}
