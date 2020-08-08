package miskyle.realsurvival.status.listener;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.data.ConfigManager;
import miskyle.realsurvival.data.ItemManager;
import miskyle.realsurvival.data.PlayerManager;
import miskyle.realsurvival.data.item.RSItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;

public class MobMakeDisease implements Listener{
  @SuppressWarnings("deprecation")
  @EventHandler
  public void onMobHurtPlayer(final EntityDamageByEntityEvent e) {
    if (ConfigManager.getDiseaseConfig().isMob()
        || e.getEntity().getType() != EntityType.PLAYER
        || PlayerManager.isActive(e.getEntity().getName())) {
      return;
    }
    Player p = (Player) e.getEntity();
    PlayerData pd = PlayerManager.getPlayerData(p.getName());
    ItemStack handItem = null;
    StringBuilder sb = new StringBuilder();
    if(e.getDamager() instanceof LivingEntity) {
      handItem = ConfigManager.getBukkitVersion()>8?((LivingEntity)e.getDamager()).getEquipment().getItemInMainHand():((LivingEntity)e.getDamager()).getEquipment().getItemInHand();
      sb.append(e.getDamager().getCustomName());
    }else if(e.getDamager() instanceof Projectile) {
      Projectile pro = (Projectile)e.getDamager();
      if(pro.getShooter() instanceof LivingEntity) {
        handItem = ConfigManager.getBukkitVersion()>8?((LivingEntity)pro.getShooter()).getEquipment().getItemInMainHand():((LivingEntity)pro.getShooter()).getEquipment().getItemInHand();
        if (ConfigManager.getBukkitVersion() <= 8) {
          sb.append(((LivingEntity)pro.getShooter()).getName());          
        } else {
          sb.append(((LivingEntity)pro.getShooter()).getCustomName());
        }
      }
    }
    
    RSItemData data = ItemManager.getDiseaseSource(handItem);
    if(data == null || data.getDrugData() == null
        || !data.getDrugData().isMakeDisease()) {
      return;
    }
    if(data.getDrugData().isMakeDisease()) {
      data.getDrugData().getGetDisease().forEach((k,v)->{
        if(Math.random()*100<v) {
          pd.getDisease().addDisease(k);
          PlayerManager.bar.sendActionBar(p, Msg.tr("messages.make-disease",sb.toString(),k));
        }
      });
    }
  }
}
