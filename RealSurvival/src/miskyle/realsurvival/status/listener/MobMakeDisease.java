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
import miskyle.realsurvival.data.item.RsItemData;
import miskyle.realsurvival.data.playerdata.PlayerData;
import miskyle.realsurvival.util.ItemUtil;

public class MobMakeDisease implements Listener {
  
  /**
   * 怪物致病事件.

   * @param e 实体对实体造成伤害.
   */
  @EventHandler
  public void onMobHurtPlayer(final EntityDamageByEntityEvent e) {
    if (ConfigManager.getDiseaseConfig().isMob() || e.getEntity().getType() != EntityType.PLAYER
        || PlayerManager.isActive(e.getEntity().getName())) {
      return;
    }
    Player p = (Player) e.getEntity();
    PlayerData pd = PlayerManager.getPlayerData(p.getName());
    ItemStack handItem = null;
    String mobName = null;
    if (e.getDamager() instanceof LivingEntity) {
      handItem = ItemUtil.getItemInMainHand((LivingEntity) e.getDamager());
      mobName = getMobName((LivingEntity) e.getDamager());
    } else if (e.getDamager() instanceof Projectile) {
      Projectile pro = (Projectile) e.getDamager();
      if (pro.getShooter() instanceof LivingEntity) {
        handItem = ItemUtil.getItemInMainHand((LivingEntity) pro.getShooter());
        mobName = getMobName((LivingEntity) pro.getShooter());
      }
    }

    final String mob = mobName;
    RsItemData data = ItemManager.getDiseaseSource(handItem);
    if (data == null || data.getDrugData() == null || !data.getDrugData().isMakeDisease()) {
      if (mobName != null && !mobName.isEmpty()) {
        //以名字判断是否给病.
        ConfigManager.getDiseaseConfig().getMobDiseases(mobName).forEach(disease -> {
          if (disease.isMakeDisease()) {
            pd.getDisease().addDisease(disease.getName());
            PlayerManager.bar.sendActionBar(p, 
                Msg.tr("messages.make-disease", mob, disease.getName()));
          }
        });
      }
      return;
    }
    // 致病环节.
    if (data.getDrugData().isMakeDisease()) {
      data.getDrugData().getGetDisease().forEach((k, v) -> {
        if (Math.random() * 100 < v) {
          pd.getDisease().addDisease(k);
          PlayerManager.bar.sendActionBar(p, Msg.tr("messages.make-disease", mob, k));
        }
      });
    }
  }
  
  /**
   * 获取指定实体的名字.

   * @param entity 实体
   * @return 如果该实体有自定义名字则会返回该自定义名字, 否则返回种类名字.
   */
  public static String getMobName(LivingEntity entity) {
    if (entity == null) {
      return null;
    }
    if (ConfigManager.getBukkitVersion() <= 8) {
      return entity.getType().name();
    } else {
      return entity.getCustomName() != null ? entity.getCustomName() : entity.getType().name();
    }
  }
}