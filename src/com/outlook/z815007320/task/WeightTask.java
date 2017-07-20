package com.outlook.z815007320.task;

import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.utils.Utils;

public class WeightTask extends PluginRS implements Runnable{

	@Override
	public void run() {
		Set<UUID> players=rs.getPlayerDatas().keySet();
		for(UUID ps:players){
			Player p = rs.getServer().getPlayer(ps);
			if(p==null||p.isDead())continue;
		      if ((p.hasPermission("RealSurvival.unlimited")||p.hasPermission("RealSurvival.Admin"))&&!TestCMD.contains(p)) 
		    	  continue;
		      Inventory inv=p.getInventory();
		      double weight=0;
		      //检测背包中的物品
		      for(int i=0;i<inv.getSize();i++){
		    	  ItemStack is=inv.getItem(i);
		    	  if(is==null)continue;
		    	  
		    	  if(is.getItemMeta()==null||is.getItemMeta().getLore()==null){
		    		  weight+=rs.getWeight(is.getType().name())*is.getAmount();
		    		  continue;
		    	  }
		    	  double temp=Utils.getLore(rs.getLoreTabel("Weight"), is.getItemMeta().getLore())*is.getAmount();
		    	  if(temp>0) weight+=temp;
		      }

			     
		      //检测装备槽的物品
		      for(ItemStack is:p.getInventory().getArmorContents()){
		    	  if(is==null)continue;
		    	  if(is.getItemMeta()==null||is.getItemMeta().getLore()==null){
		    		  weight+=rs.getWeight(is.getType().name())*is.getAmount();
		    		  continue;
		    	  }
		    	  double temp=Utils.getLore(rs.getLoreTabel("Weight"), is.getItemMeta().getLore())*is.getAmount();
		    	  if(temp>0) weight+=temp;
		      }
		      
		      rs.getPlayerData(p).setWeight(weight);
		}
	}
	
}
