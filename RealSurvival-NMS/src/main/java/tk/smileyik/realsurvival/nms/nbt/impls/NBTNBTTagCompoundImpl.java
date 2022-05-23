package tk.smileyik.realsurvival.nms.nbt.impls;

import tk.smileyik.realsurvival.nms.NMSFactory;
import tk.smileyik.realsurvival.nms.nbt.NBTNBTTagCompound;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NBTNBTTagCompoundImpl implements NBTNBTTagCompound {
  private static Class<?> NBTTagCompound;

  // NBTTagCompound constructor
  private static Constructor<?> newNBTTagCompound;

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
      NBTTagCompound = Class.forName("net.minecraft.server." + version + "NBTTagCompound");
      // NBTTagCompound constructor
      newNBTTagCompound = NBTTagCompound.getDeclaredConstructor();
      // NBTTagCompound method
      setString = NBTTagCompound.getDeclaredMethod("setString", String.class, String.class);
      getString = NBTTagCompound.getDeclaredMethod("getString", String.class);
      hasKey = NBTTagCompound.getDeclaredMethod("hasKey", String.class);
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  private Object nbt;

  public NBTNBTTagCompoundImpl() {

  }

  public NBTNBTTagCompoundImpl(Object obj) {
    this.nbt = obj;
  }

  public Class<?> getClazz() {
    return NBTTagCompound;
  }

  @Override
  public String getString(String key) {
    try {
      return hasKey(key) ? (String) getString.invoke(nbt, key) : null;
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public void setString(String key, String value) {
    try {
      setString.invoke(nbt, key, value);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean hasKey(String key) {
    try {
      return (boolean) hasKey.invoke(nbt, key);
    } catch (IllegalAccessException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return false;
  }
}
