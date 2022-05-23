package tk.smileyik.realsurvival.nms.nbt.impls;

import tk.smileyik.realsurvival.nms.NMSFactory;
import tk.smileyik.realsurvival.nms.nbt.NBTItemStack;
import tk.smileyik.realsurvival.nms.nbt.NBTNBTTagCompound;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NBTItemStackImpls implements NBTItemStack {
  private static Class<?> ItemStack;

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

  static {
    String version = NMSFactory.getVersion();
    try {
      ItemStack      = Class.forName("net.minecraft.server." + version + "ItemStack");
      // ItemStack methods
      hasTag = ItemStack.getDeclaredMethod("hasTag");
      getTag = ItemStack.getDeclaredMethod("getTag");
      setTag = ItemStack.getDeclaredMethod("setTag", NBTNBTTagCompound.getClazz());
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      e.printStackTrace();
    }

  }

  private final Object item;

  public NBTItemStackImpls(Object item) {
    this.item = item;
  }

  @Override
  public NBTNBTTagCompound getTag() {
    try {
      return NBTNBTTagCompound.newNBTTagCompound(
              getTag.invoke(item)
      );
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean hasTag() {
    try {
      return (boolean) hasTag.invoke(item);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void setTag(NBTNBTTagCompound nbtTagCompound) {
    try {
      setTag.invoke(item, nbtTagCompound);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Class<?> getClazz() {
    return ItemStack;
  }
}
