package tk.smileyik.realsurvival.nms.nbt;


import tk.smileyik.realsurvival.nms.nbt.impls.NBTNBTTagCompoundImpl;

public interface NBTNBTTagCompound {
  String getString(String key);
  void setString(String key, String value);
  boolean hasKey(String key);

  static Class<?> getClazz() {
    return new NBTNBTTagCompoundImpl().getClazz();
  }

  static NBTNBTTagCompound newNBTTagCompound(Object obj) {
    return new NBTNBTTagCompoundImpl(obj);
  }
}
