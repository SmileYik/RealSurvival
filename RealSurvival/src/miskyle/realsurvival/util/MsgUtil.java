package miskyle.realsurvival.util;

import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.i18n.I18N;
import com.github.miskyle.mcpt.nms.actionbar.NMSActionBar;
import com.github.miskyle.mcpt.nms.title.NMSTitle;
import com.github.miskyle.mcpt.nms.title.TitleAction;

import miskyle.realsurvival.Msg;
import miskyle.realsurvival.RealSurvival;

public class MsgUtil {
  private static final NMSActionBar actionBar;
  private static final NMSTitle          title;
  
  static {
    actionBar = NMSActionBar.getActionBar(RealSurvival.getVersion());
    title = NMSTitle.getTitle(RealSurvival.getVersion());
  }
  
  public static void sendMsgI18N(Player p,String key) {
    sendMessage(p, I18N.tr(key));
  }
  public static void sendMsg(Player p,String key) {
    sendMessage(p, Msg.tr(key));
  }
  public static void sendMessage(Player p,String msg) {
    if(msg==null) {
      return;
    }
    if(msg.length()<2) {
      p.sendMessage(msg);
    }else {
      char mode = msg.charAt(0);
      if(mode == '1' && actionBar != null) {
        actionBar.sendActionBar(p, msg.substring(1));
      }else if(mode == '2' && title != null) {
        title.sendTitle(p, TitleAction.TITLE, msg.substring(1));
      }else if(mode == '3' && title!=null) {
        title.sendTitle(p, TitleAction.SUBTITLE, msg.substring(1));
      }else {
        p.sendMessage(msg);
      }
    }
  }
  
  
}
