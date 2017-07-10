package com.outlook.z815007320.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.data.PluginRS;

public class TestCMD extends PluginRS implements Listener{
	private static Listener test;
	private static List<UUID> canUsePeople=new LinkedList<UUID>();
	private static List<UUID> valueOn=new LinkedList<UUID>();

	public static boolean openTestEvent(Player p){
		p.sendMessage("§9[RealSurvival] §3§l测试功能已经打开, 目前测试功能如下: "
				+ "单机方块获取方块名并且获取手上的物品名及此方块的最终温度, "
				+ "还有目标方块所在的生物群系.输入§c§l/rs test close§3§l可以关闭");
		if(test==null){
			test=new TestCMD();
			rs.getServer().getPluginManager().registerEvents(test, rs);
		}
		if(canUsePeople.contains(p.getUniqueId()))return true;
		canUsePeople.add(p.getUniqueId());
		return true;
	}
	
	public static boolean closeTestEvent(Player p){
		p.sendMessage("§9[RealSurvival] §3§l测试功能已经关闭.");
		canUsePeople.remove(p.getUniqueId());
		if(canUsePeople.size()==0){
			PlayerInteractEvent.getHandlerList().unregister(test);
			test=null;			
		}
		return true;
	}
	
	public static boolean  onValue(Player p){
		if(!valueOn.contains(p.getUniqueId()))
			valueOn.add(p.getUniqueId());
		p.sendMessage("§9[RealSurvival] §3§l恩,现在你的属性值会开始流动,直到服务器关闭或者使用 "
				+ "§c§l/rs test off §3§l指令后即可停止");
		return true;
	}
	
	public static boolean  offValue(Player p){
		valueOn.remove(p.getUniqueId());
		p.sendMessage("§9[RealSurvival] §3§l属性值流动已经停止.");
		return true;
	}
	
	public static boolean contains(Player p){
		return valueOn.contains(p.getUniqueId());
	}
	
	@EventHandler
	private void getDatas(PlayerInteractEvent e){
		if(!(e.getAction()==Action.LEFT_CLICK_BLOCK||e.getAction()==Action.LEFT_CLICK_AIR))return;
		if(!canUsePeople.contains(e.getPlayer().getUniqueId()))return;
		e.setCancelled(true);
		Block block=e.getPlayer().getWorld().getBlockAt(e.getPlayer().getTargetBlock((Set<Material>)null, 6).getLocation()); 
		if(block!=null)
			e.getPlayer().sendMessage("§b方块名: §3§l"+block.getType());
		if(e.getPlayer().getInventory().getItemInMainHand()!=null)
			e.getPlayer().sendMessage("§b物品名: §3§l"+e.getPlayer().getInventory().getItemInMainHand().getType());
		double _temper1=getTemper(e.getPlayer(),e.getPlayer().getWorld().getTemperature(e.getPlayer().getLocation().getBlockX(), e.getPlayer().getLocation().getBlockZ()));
		e.getPlayer().sendMessage("§b脚下温度: §3§l"+_temper1+"℃");
		if(block!=null){
			double _temper2=getTemper(e.getPlayer(),e.getPlayer().getWorld().getTemperature(block.getX(), block.getZ()));
			e.getPlayer().sendMessage("§b目标方块温度: §3§l"+_temper2+"℃");
			e.getPlayer().sendMessage("§b目标方块生物群系: §3§l"+block.getBiome());
		}
		
		
	}
	
	private double getTemper(Player p, double _temper){
	    long time=p.getWorld().getTime();
	      //Material.
	      if(_temper>=0.5)
	    	  _temper-=0.5;
	      else _temper-=0.5;
	      if(time>=12000L&&time<22200L)
	    	  _temper-=Math.random()*0.5;
	      else
	    	  _temper+=Math.random()*0.5;
	      if(p.getWorld().hasStorm())
	    	  _temper-=Math.random()*0.5;
	      _temper+=getBlocks(p);
	      _temper+=getInv(p);
	      if(p.getWorld().getBlockAt(p.getLocation()).getLightLevel()<=5)
	    	  _temper-=0.5*(5-p.getWorld().getBlockAt(p.getLocation()).getLightLevel()*0.1);
	      if(p.isSprinting())
	    	  _temper+=Math.random()*0.02;
	      
		return _temper;
	}
	
	private double getBlocks(Player p){
		double _tem=0;
		int x=p.getLocation().getBlockX()+2;
		int y=p.getLocation().getBlockY()+2;
		int z=p.getLocation().getBlockZ()+2;
		
		for(int sx=x-5;sx<x;sx++)
			for(int sy=y-5;sy<y;sy++)
				for(int sz=z-5;sz<z;sz++){
					Block temp=p.getWorld().getBlockAt(sx, sy, sz);
					if(rs.getHeatSource().containsKey(temp.getType().name()))
						_tem+=rs.getHeatSource().get(temp.getType().name());
					//System.out.println(sx+" "+sy+" "+sz+" "+temp.getType().name());
				}
		return _tem;
	}
	
	private double getInv(Player p){
		double aTem=0;
		  for(int i=0;i<p.getInventory().getSize();i++)
			  if(p.getInventory().getItem(i)!=null&&rs.getHeatSource().containsKey(p.getInventory().getItem(i).getType().name()))
					aTem+=rs.getHeatSource().get(p.getInventory().getItem(i).getType().name());
		  
		  for(ItemStack is:p.getInventory().getArmorContents())
			  if(is!=null&&rs.getHeatSource().containsKey(is.getType().name()))
					aTem+=rs.getHeatSource().get(is.getType().name());
	      return aTem;
	}
}
