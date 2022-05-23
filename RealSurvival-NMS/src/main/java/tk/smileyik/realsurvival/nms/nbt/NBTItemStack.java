package tk.smileyik.realsurvival.nms.nbt;

public interface NBTItemStack {
  NBTNBTTagCompound getTag();
  boolean hasTag();
  void setTag(NBTNBTTagCompound nbtTagCompound);
  Class<?> getClazz();
}
