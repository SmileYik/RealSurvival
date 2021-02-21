package miskyle.realsurvival.data;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import com.github.miskyle.mcpt.MCPT;
import miskyle.realsurvival.api.effect.RsEffect;
import miskyle.realsurvival.api.status.StatusType;
import miskyle.realsurvival.data.effect.EffectData;
import miskyle.realsurvival.data.effect.MinecraftPotionEffect;
import miskyle.realsurvival.data.effect.sub.EffectEnergy;
import miskyle.realsurvival.data.effect.sub.EffectSleep;
import miskyle.realsurvival.data.effect.sub.EffectTemperatureDown;
import miskyle.realsurvival.data.effect.sub.EffectTemperatureUp;
import miskyle.realsurvival.data.effect.sub.EffectThirst;
import miskyle.realsurvival.data.effect.sub.EffectWeight;
import miskyle.realsurvival.util.RsClassLoader;

public class EffectManager {
  private static EffectManager em;
  private Method methondEffect;

  private HashMap<String, Object> effects;

  public EffectManager() {
    em = this;
    setupRsEffect();
  }

  /**
   * 取读外来RSEffect.
   */
  public void setupRsEffect() {
    effects = new HashMap<String, Object>();
    // 初始化RSEffect的effect方法
    try {
      methondEffect = RsEffect.class.getMethod(
          "effect", Player.class, StatusType.class, int.class, int.class);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    // 获取目录下所有父类为RSEffect的子类
    new RsClassLoader(
        MCPT.plugin.getDataFolder() + "/effect/").loadAllClass(RsEffect.class).forEach((k, v) -> {
          try {
            Object effect = v.getConstructor(String.class).newInstance(k);
            effects.put(k, effect);
          } catch (InstantiationException e) {
            e.printStackTrace();
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          } catch (IllegalArgumentException e) {
            e.printStackTrace();
          } catch (InvocationTargetException e) {
            e.printStackTrace();
          } catch (NoSuchMethodException e) {
            e.printStackTrace();
          } catch (SecurityException e) {
            e.printStackTrace();
          }
        });

    //  加载默认自带RSEffect子类
    try {
      effects.put("EffectSleep", 
          EffectSleep.class.getConstructor(String.class).newInstance("EffectSleep"));
      effects.put("EffectThirst", 
          EffectThirst.class.getConstructor(String.class).newInstance("EffectThirst"));
      effects.put("EffectEnergy", 
          EffectEnergy.class.getConstructor(String.class).newInstance("EffectEnergy"));
      effects.put("EffectWeight", 
          EffectWeight.class.getConstructor(String.class).newInstance("EffectWeight"));
      effects.put("EffectTemperatureUp", 
          EffectTemperatureUp.class.getConstructor(String.class)
          .newInstance("EffectTemperatureUp"));
      effects.put("EffectTemperatureDown", 
          EffectTemperatureDown.class.getConstructor(String.class)
          .newInstance("EffectTemperatureDown"));
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    }
    
    MCPT.plugin.getLogger().info("Load RSEffect: " + effects.size());
  }
  
  /**
   * 给玩家添加效果.

   * @param player 目的玩家.
   * @param effectName 效果名.
   * @param duration 持续时间.
   * @param effectLevel 持续等级.
   */
  public static void effectPlayer(
      Player player, String effectName, StatusType type, int duration, int effectLevel) {
    if (em.effects.containsKey(effectName)) {
      try {
        em.methondEffect.invoke(em.effects.get(effectName), player, type, effectLevel, duration);
      } catch (IllegalAccessException 
          | IllegalArgumentException 
          | InvocationTargetException 
          | SecurityException e) {
        e.printStackTrace();
      }

    } else if (PotionEffectType.getByName(effectName) != null) {
      MCPT.plugin.getServer().getScheduler().runTask(MCPT.plugin, () -> {
        new MinecraftPotionEffect(effectName).effect(player, type, duration, effectLevel);
      });
    }
  }
  
  

  /**
   * 给玩家添加效果.

   * @param player 目的玩家.
   * @param effectName 效果名.
   * @param duration 持续时间.
   * @param effectLevel 持续等级.
   */
  @Deprecated
  public static void effectPlayer(Player player, String effectName, int duration, int effectLevel) {
    effectPlayer(player, effectName, null, duration, effectLevel);
  }

  /**
   * 给玩家添加效果.

   * @param player 目的玩家
   * @param effect 目的效果
   */
  @Deprecated
  public static void effectPlayer(Player player, EffectData effect) {
    if (effect != null && Math.random() * 100 < effect.getChance()) {
      effectPlayer(player, effect.getName(), null, effect.getDuration(), effect.getEffecLevel());
    }
  }
  
  /**
   * 给玩家添加效果.

   * @param player 目的玩家
   * @param effect 目的效果
   */
  public static void effectPlayer(Player player, EffectData effect, StatusType type) {
    if (effect != null && Math.random() * 100 < effect.getChance()) {
      effectPlayer(player, effect.getName(), type, effect.getDuration(), effect.getEffecLevel());
    }
  }
}
