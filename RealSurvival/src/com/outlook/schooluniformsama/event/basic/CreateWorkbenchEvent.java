package com.outlook.schooluniformsama.event.basic;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Hopper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.WorkbenchShape;
import com.outlook.schooluniformsama.data.recipes.WorkbenchType;
import com.outlook.schooluniformsama.data.timer.FurnaceTimer;
import com.outlook.schooluniformsama.data.timer.RainwaterCollectorTimer;
import com.outlook.schooluniformsama.data.timer.Timer;
import com.outlook.schooluniformsama.data.timer.WorkbenchTimer;
import com.outlook.schooluniformsama.gui.FeatureGUI;
import com.outlook.schooluniformsama.gui.Furnace;
import com.outlook.schooluniformsama.gui.Workbench;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;
public class CreateWorkbenchEvent implements Listener{
	@EventHandler
	public void openTable(PlayerInteractEvent e){
		if(e.getItem()!=null)return;
		Block block=e.getClickedBlock();
		if(!(e.getAction()==Action.RIGHT_CLICK_AIR || e.getAction()==Action.RIGHT_CLICK_BLOCK) || block==null || block.getType()==Material.AIR)
			return;
		if(!Data.playerData.containsKey(e.getPlayer().getUniqueId()))return;
		Player p = e.getPlayer();
		
		//RainwaterCollector
		if((block.getType()==Material.CAULDRON&&(block.getRelative(BlockFace.DOWN).getState() instanceof Hopper)) || 
				block.getState() instanceof Hopper &&(block.getRelative(BlockFace.UP).getType()==Material.CAULDRON)){
			if(Data.timer.containsKey(Util.getWorkbenchID(block)))
			e.setCancelled(true);
			openRainwaterCollector(p, block);
			return;
		}
		
		//PurificationDevice
		if((block.getType()==Material.CARPET&&(block.getRelative(BlockFace.DOWN).getType()==Material.CAULDRON)|| 
				block.getType()==Material.CAULDRON&&(block.getRelative(BlockFace.UP).getType()==Material.CARPET))){
			e.getPlayer().openInventory(FeatureGUI.openPurificationDevice());
			return;
		}
		
		for(Map.Entry<String,WorkbenchShape> entry:Data.workbenchs.entrySet()){
			if(block.getType().name().equalsIgnoreCase(entry.getValue().getMain()) && compare(block,BlockFace.UP,entry.getValue().getUp()) && compare(block,BlockFace.DOWN,entry.getValue().getDown())){
				if((compare(block,BlockFace.NORTH,entry.getValue().getLeft())&&compare(block,BlockFace.SOUTH,entry.getValue().getRight())&&compare(block,BlockFace.EAST,entry.getValue().getBehind())&&compare(block,BlockFace.WEST,entry.getValue().getFront())) || 
						(compare(block,BlockFace.NORTH,entry.getValue().getBehind())&&compare(block,BlockFace.SOUTH,entry.getValue().getFront())&&compare(block,BlockFace.EAST,entry.getValue().getRight())&&compare(block,BlockFace.WEST,entry.getValue().getLeft())) || 
						(compare(block,BlockFace.NORTH,entry.getValue().getRight())&&compare(block,BlockFace.SOUTH,entry.getValue().getLeft())&&compare(block,BlockFace.EAST,entry.getValue().getFront())&&compare(block,BlockFace.WEST,entry.getValue().getBehind())) || 
						(compare(block,BlockFace.NORTH,entry.getValue().getFront())&&compare(block,BlockFace.SOUTH,entry.getValue().getBehind())&&compare(block,BlockFace.EAST,entry.getValue().getLeft())&&compare(block,BlockFace.WEST,entry.getValue().getRight()))){
					e.setCancelled(true);
					if(entry.getValue().getType()==WorkbenchType.WORKBENCH)
						openWorkbenchGUI(p, block,entry.getKey());
					else if(entry.getValue().getType()==WorkbenchType.FURNACE)
						openFurnaceGUI(p, block,entry.getKey());
				}
			}
		}
		
	}
	private void openRainwaterCollector(Player p,Block b){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(FeatureGUI.openRainwaterCollector((RainwaterCollectorTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else Msg.sendMsgToPlayer(p, "RainwaterCollectorError1",true);
		}else{	
			RainwaterCollectorTimer rct = new RainwaterCollectorTimer(p.getName(),b.getWorld().getName(), b.getX(), b.getY(), b.getZ());
			rct.start();
			Data.timer.put(Util.getWorkbenchID(b), rct);
			p.openInventory(FeatureGUI.openRainwaterCollector((RainwaterCollectorTimer) Data.timer.get(Util.getWorkbenchID(b))));
		}
	}
	
	private void openFurnaceGUI(Player p,Block b,String ws){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(Furnace.openFurnaceGUI((FurnaceTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else Msg.sendMsgToPlayer(p, "WorkbenchIsUsing", new String[]{"%player%"},
					new String[]{Data.timer.get(Util.getWorkbenchID(b)).getPlayerName()},true);
		}else{			
			TempData.createTimerTemp.put(p.getName(), new Timer(ws,p.getName(), WorkbenchType.FURNACE, 
					b.getWorld().getName(), b.getX(), b.getY(), b.getZ()));
			p.openInventory(Furnace.createFurnaceGUI(Data.workbenchs.get(ws).getTitle()));
		}
	}
	
	private void openWorkbenchGUI(Player p,Block b,String ws){
		if(Data.timer.containsKey(Util.getWorkbenchID(b))){
			if(Data.timer.get(Util.getWorkbenchID(b)).getPlayerName().equals(p.getName())){
				TempData.openingWorkbench.put(p.getName(), Util.getWorkbenchID(b));
				p.openInventory(Workbench.openWorkbenchGUI((WorkbenchTimer) Data.timer.get(Util.getWorkbenchID(b))));
			}else Msg.sendMsgToPlayer(p, "WorkbenchIsUsing", new String[]{"%player%"},
					new String[]{Data.timer.get(Util.getWorkbenchID(b)).getPlayerName()},true);
		}else{			
			TempData.createTimerTemp.put(p.getName(), new Timer(ws,p.getName(), WorkbenchType.WORKBENCH, 
					b.getWorld().getName(), b.getX(), b.getY(), b.getZ()));
			p.openInventory(Workbench.createWorkbenchGUI(Data.workbenchs.get(ws).getTitle()));
		}
	}
	
/*	private boolean compare(Material type, String name){
		if(name==null||name.equalsIgnoreCase("null"))return true;
		return type.name().equalsIgnoreCase(name);
	}*/
	
	private boolean compare(Block b,BlockFace face,String name){
		if(name==null || name.equalsIgnoreCase("null"))
			return true;
		return b.getRelative(face).getType().name().equalsIgnoreCase(name);
	}

}
