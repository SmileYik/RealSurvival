package com.outlook.z815007320.task;

import org.bukkit.block.Block;
import org.bukkit.block.Hopper;

import com.outlook.z815007320.data.PluginRS;
import com.outlook.z815007320.gui.FireCraftTableGUI;
import com.outlook.z815007320.utils.Utils;

public class WorkBenchTask extends PluginRS implements Runnable{
	@Override
	public void run() {
		//workbench
		for(String key:rs.getWorkbench().keySet()){
			Object[] obj=rs.getWorkbench(key);
			Block b=rs.getServer().getWorld(obj[3].toString()).getBlockAt((int)obj[4], (int)obj[5], (int)obj[6]);
			if(b==null || !b.getType().name().equals(rs.getWorkbenchItems()[0]))
				rs.removeWorkBench(key);
			else
				rs.resetWorkBench(key,obj[0]+","+obj[1]+","+((int)obj[2]+1)+","+obj[3]+","+obj[4]+","+obj[5]+","+obj[6]);
			if(((int)obj[2]+1)>rs.getSF(obj[1].toString()).getBuiltTime()&&((int)obj[2])<=rs.getSF(obj[1].toString()).getBuiltTime())
				Utils.sendMsgToPlayer(rs.getPlayerData(rs.getServer().getPlayer(obj[0].toString())), "BuiltOver");
		}
		//RainwaterCollector
		for(String key:rs.getRwC().keySet()){
			String value[]=rs.getRwC(key).split(",");
			if(!(rs.getServer().getWorld(value[3]).getBlockAt(Integer.parseInt(value[4]),
					Integer.parseInt(value[5]), Integer.parseInt(value[6])).getState() instanceof Hopper)){
				rs.removeRwC(key);
				continue;
			}
			if(!rs.getServer().getWorld(value[3]).hasStorm())continue;
			int tem=Integer.parseInt(value[1])+1;
			if(tem>=60&&Integer.parseInt(value[2])<14)
				rs.resetRwC(key,value[0]+","+0+","+(Integer.parseInt(value[2])+1)
						+","+value[3]+","+value[4]+","+value[5]+","+value[6]);
			else
				rs.resetRwC(key,value[0]+","+tem+","+value[2]+","+value[3]+","
						+value[4]+","+value[5]+","+value[6]);
		}
		//Fire Craft Table
		for(String key:rs.getFCT().keySet()){
			String[] obj=rs.getFCT(key);
			Block b=rs.getServer().getWorld(obj[6]).getBlockAt(Integer.parseInt(obj[7]), 
					Integer.parseInt(obj[8]), Integer.parseInt(obj[9]));
			if(b==null || !b.getType().name().equals(rs.getFireCraftTableItems()[0])){
				rs.removeFCT(key);	
				continue;
			}else {
				if(Double.parseDouble(obj[3])>0)
					obj[3]=(Double.parseDouble(obj[3])-1)+"";
				if(Double.parseDouble(obj[5])>=0&&(FireCraftTableGUI.checkHeatSource(obj)+
						Double.parseDouble(obj[3])>=Double.parseDouble(obj[5]))&&
						(Integer.parseInt(obj[2])+1)<=Integer.parseInt(obj[4]))
					rs.resetFCT(key,obj[0]+","+obj[1]+","+(Integer.parseInt(obj[2])+1)+","+obj[3]+","+
						obj[4]+","+obj[5]+","+obj[6]+","+obj[7]+","+obj[8]+","+obj[9]);
				else if(Double.parseDouble(obj[5])<0&&(FireCraftTableGUI.checkHeatSource(obj)+
						Double.parseDouble(obj[3])<=Double.parseDouble(obj[5]))&&
						(Integer.parseInt(obj[2])+1)<=Integer.parseInt(obj[4]))
					rs.resetFCT(key,obj[0]+","+obj[1]+","+(Integer.parseInt(obj[2])+1)+","+obj[3]+","+
						obj[4]+","+obj[5]+","+obj[6]+","+obj[7]+","+obj[8]+","+obj[9]);
				else
					rs.resetFCT(key,obj[0]+","+obj[1]+","+Integer.parseInt(obj[2])+","+obj[3]+","+obj[4]+
							","+obj[5]+","+obj[6]+","+obj[7]+","+obj[8]+","+obj[9]);
			}
		}
		
	}
}
