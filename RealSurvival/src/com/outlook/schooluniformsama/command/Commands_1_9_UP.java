package com.outlook.schooluniformsama.command;

import java.io.File;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.item.RSItem;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.data.recipes.*;
import com.outlook.schooluniformsama.event.basic.CraftItemEvent;
import com.outlook.schooluniformsama.event.basic.TempData;
import com.outlook.schooluniformsama.gui.*;
import com.outlook.schooluniformsama.update.Update;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

import org.bukkit.*;

public class Commands_1_9_UP {
	
	private LinkedList<UUID> stateCD = new LinkedList<>();
	private Plugin plugin;
	public Commands_1_9_UP(Plugin plugin) {
		this.plugin=plugin;
	}
	
	private Integer isInt(Player p,String num,int index){
		int n;
		try {
			n=Integer.parseInt(num);
		} catch (NumberFormatException e) {
			Msg.sendMsgToPlayer(p, "NotInteger",new String[]{"%index%"} ,new String[]{index+""}, true);
			return null;
		}
		return n;
	}
	
	private Double isDouble(Player p,String num,int index){
		double n;
		try {
			n=Double.parseDouble(num);
		} catch (NumberFormatException e) {
			Msg.sendMsgToPlayer(p, "NotDouble",new String[]{"%index%"} ,new String[]{index+""}, true);
			return null;
		}
		return n;
	}
	
	//TODO Other cmd
	@Command(cmd = "update",des="UpdateDes",type="Update",permissions = "RealSurvival.Admin")
	public void update(Player p,String args[]){
		Update u = Update.getUpdate("update");
		if(u==null){
			Msg.sendMsgToPlayer(p, "CheckUpdateFailed", true);
			return;
		}
		if(u.hasUpdate()){
			u.download();
			Msg.sendMsgToPlayer(p, "DownloadOver", new String[]{"%version%"},new String[]{u.getVersion_show()},true);
			p.sendMessage(Msg.getPrefix()+u.getUpdate_info());
			return;
		}else{
			Msg.sendMsgToPlayer(p, "IsNewVersion", true);
			return;
		}
	}
	
	@Command(cmd = "cupdate",des="CUpdateDes",type="Update",permissions = "RealSurvival.Admin")
	public void cUpdate(Player p,String args[]){
		Update u = Update.getUpdate("update");
		if(u==null){
			Msg.sendMsgToPlayer(p, "CheckUpdateFailed", true);
			return;
		}
		if(u.hasUpdate()){
			p.sendMessage(Msg.getMsg("HasNewVersion", new String[]{"%now-version%","%version%"},new String[]{Update.now_version_show,u.getVersion_show()},true)+" "
					+(u.isReplace_config()?Msg.getMsg("WillDeleteConfig", false):"")+" "
					+(u.isReplace_message()?Msg.getMsg("WillDeleteMessages", false):""));
			p.sendMessage(Msg.getPrefix()+u.getUpdate_info());
			return;
		}else{
			Msg.sendMsgToPlayer(p, "IsNewVersion", true);
			return;
		}
	}
	
	@Command(cmd = "getUpdateInfo",des="UpdateInfoDes",type="Update",permissions = "RealSurvival.Admin")
	public void getUpdateInfo(Player p,String args[]){
		Update u= Update.getUpdate("update");
		if(u==null){
			Msg.sendMsgToPlayer(p, "CheckUpdateFailed", true);
			return;
		}
		if(u.hasUpdate())
			u = Update.getUpdate("old/"+Update.now_version);
		if(u==null){
			Msg.sendMsgToPlayer(p, "CheckUpdateFailed", true);
			return;
		}
		p.sendMessage(Msg.getPrefix()+u.getUpdate_info());
		return;
	}
	
	//TODO help
	@Command(cmd = "help",des="HelpDes",type="Help")
	public void help(Player p,String args[]){}
	
	@Command(cmd = "help",args={"item"},des="ItemHelpDes",type="Help")
	public void helpItem(Player p,String args[]){}
	
	@Command(cmd="shelp",args={"[Text]"},des="HelpDes2",type="HELP",permissions = "RealSurvival.Admin")
	public void helpSearch(Player p,String args[]){
		
		if(args.length!=2){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		
		p.sendMessage(Msg.getMsg("Help2", new String[]{"%type%"}, new String[]{args[1]}, true));//以下是关于type的信息
		for(Method method:Commands_1_9_UP.class.getDeclaredMethods()){
			if(!method.isAnnotationPresent(Command.class))
				continue;
			Command cmd=method.getAnnotation(Command.class);
			if(!(cmd.type().toLowerCase().contains(args[1].toLowerCase()) || cmd.cmd().equalsIgnoreCase(args[1]) || Msg.getMsg(cmd.des(), false).toLowerCase().contains(args[1].toLowerCase())))
				continue;
			if(!(cmd.permissions()!="" && p.hasPermission(cmd.permissions())))
				continue;
			String arg="";
			if(cmd.childCmds()[0]!="")
				for(String temp:cmd.childCmds())
					arg+=temp+" ";
			if(cmd.args()[0]!="")
				for(String temp:cmd.args())
					arg+=temp+" ";
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',"  &c&l/rs &3&l"+cmd.cmd()+" &3"+arg.replace("  ", " ")+"&f- &b&l"+Msg.getMsg(cmd.des(),false)));
		}
	}
	
	@Command(cmd = "help",args={"workbench"},des="WorkbenchHelpDes",type="Help",permissions = "RealSurvival.Admin")
	public void workbenchHelp(){}
	
	//TODO create recipe
	@Command(cmd = "cr",childCmds={"furnace"},args={"[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "CreateFuranceRecipeDes", type = "workbench" ,permissions = "RealSurvival.Admin")
	public void createFuranceRecipe(Player p,String args[]){
		if(args.length!=7){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		
		if(new File(FurnaceRecipe.getRecipePath(args[2])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[2]}, true);
			return;
		}
		try {
			TempData.createRecipeTemp.put(p.getName(), new FurnaceRecipe(args[2], 
					isInt(p, args[3], 3),isInt(p, args[4], 4),isDouble(p,args[5],5),isDouble(p,args[6],6)));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Furnace.furnaceMakerGUI(args[2]));
		return;
	}
	
	@Command(cmd="cr",childCmds={"Furnace","SetDate"},args={"[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "ReplaceFuranceRecipeDataDes", type = "workbench" ,permissions = "RealSurvival.Admin")
	public void replaceFuranceRecipeData(Player p,String args[]){
		if(args.length!=8){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		
		if(new File(FurnaceRecipe.getRecipePath(args[3])).exists()==false){
			Msg.sendMsgToPlayer(p, "RecipeNotExists",new String[]{"%name%"} ,new String[]{args[3]}, true);
			return;
		}
		FurnaceRecipe fr = FurnaceRecipe.load(args[3]);
		
		try {
			fr.setNeedTime(isInt(p, args[4], 4));
			fr.setSaveTime(isInt(p, args[5], 5));
			fr.setMinTemperature(isDouble(p, args[6], 6));
			fr.setMaxTemperature(isDouble(p, args[7], 7));
		} catch (Exception e) {
			return;
		}
		
		if(fr.save())
			Msg.sendMsgToPlayer(p, "ReplaceRecipeSucceed", true);
		else
			Msg.sendMsgToPlayer(p, "ReplaceRecipeFailed", true);
		return;
	}
	
	@Command(cmd = "cr",childCmds="workbench",args={"[name]","[NeedTime]"}, des = "CreateWorkbenchRecipeDes", type = "workbench",permissions = "RealSurvival.Admin")
	public void createWorkbenceRecipe(Player p,String args[]){
		if(args.length!=4){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		
		if(new File(WorkbenchRecipe.getRecipePath(args[2])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[2]}, true);
			return;
		}
		try {
			TempData.createRecipeTemp.put(p.getName(), new WorkbenchRecipe(args[2], isInt(p, args[3], 3)));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Workbench.createWorkbenchRecipeGUI(args[2]));
		return;
	}
	
	//TODO Item
	private String getLongText(String args[],int begin,boolean hasColor){
		String text="";
		while(begin<args.length)
			text+=args[begin++]+" ";
		if(hasColor)
			return Util.setColor(text.substring(0, text.length()-1));
		else
			return text.substring(0, text.length()-1);
	}
	
	@Command(cmd = "item",childCmds = "save",args={"[FileName]"},des = "ItemSaveDes",type = "Item",permissions = "RealSurvival.Admin")
	public void saveItem(Player p, String args[]){
		if(args.length>3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		RSItem rsi =new RSItem(p.getInventory().getItemInMainHand());
		if(args.length==2){
			switch (rsi.save()) {
			case -1:
				Msg.sendMsgToPlayer(p, "ItemSaveF", true);
				return;
			case 0:
				Msg.sendMsgToPlayer(p, "ItemSaveE", true);
				return;
			case 1:
				Msg.sendMsgToPlayer(p, "ItemSaveS",new String[]{"%name%"} ,new String[]{rsi.getName()}, true);
				return;
			}
		}else{
			String name = getLongText(args, 2, false);
			switch (rsi.save(name)) {
			case -1:
				Msg.sendMsgToPlayer(p, "ItemSaveF", true);
				return;
			case 0:
				Msg.sendMsgToPlayer(p, "ItemSaveE", true);
				return;
			case 1:
				Msg.sendMsgToPlayer(p, "ItemSaveS",new String[]{"%name%"} ,new String[]{name}, true);
				return;
			}
		}
	}
	
	@Command(cmd = "item",childCmds = "load",args={"[FileName]"},des = "ItemLoadDes",type = "Item",permissions = "RealSurvival.Admin")
	public void loadItem(Player p, String args[]){
		if(args.length<3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		String name = getLongText(args, 2, false);
		RSItem rsi=RSItem.loadItem(name);
		if(rsi==null){
			Msg.sendMsgToPlayer(p, "NotFindItem", true);
			return;
		}
		CraftItemEvent.givePlayerItem(p, rsi.getItem());
	}
	
	@Command(cmd = "item",childCmds = "delete",args={"[FileName]"},des = "ItemDeleteDes",type = "Item",permissions = "RealSurvival.Admin")
	public void deleteItem(Player p, String args[]){
		if(args.length!=3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		String name = getLongText(args, 2, false);
		File item=new File(RSItem.getItemPath(name));
		if(!item.exists()){
			Msg.sendMsgToPlayer(p, "NotFindItem", true);
			return;
		}
		if(!item.delete())Msg.sendMsgToPlayer(p, "DeleteFailed", true);
	}
	
	@Command(cmd = "item",childCmds = "setName",args={"[name]"},des = "ItemSetNameDes",type = "Item",permissions = "RealSurvival.Admin")
	public void setItemName(Player p, String args[]){
		if(args.length<3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		String name = getLongText(args, 2, true);
		ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
		im.setDisplayName(name);
		p.getInventory().getItemInMainHand().setItemMeta(im);
	}
	
	@Command(cmd = "item",childCmds = "lore",args={"[lore]"},des = "ItemAddLoreDes",type = "Item",permissions = "RealSurvival.Admin")
	public void addItemLore(Player p, String args[]){
		if(args.length<3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		String lore = getLongText(args, 2, true);
		for(Entry<String, String> str:Data.label.entrySet())
			lore=lore.replaceAll("%"+str.getKey()+"%", str.getValue());
		ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
		List<String> Lore;
		if(im.hasLore())
			Lore=im.getLore();
		else
			Lore=new LinkedList<>();
		Lore.add(lore.replaceAll("%split%", Data.split));
		im.setLore(Lore);
		p.getInventory().getItemInMainHand().setItemMeta(im);
	}
	
	@Command(cmd = "item",childCmds = "setNBT",args={"[NBTFileName]"},des = "ItemSetNBTDes",type = "Item",permissions = "RealSurvival.Admin")
	public void setItemNBT(Player p, String args[]){
		if(args.length<3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		String lore = getLongText(args, 2, false);
		p.getInventory().setItemInMainHand(Data.nbtitem.addNBT(p.getInventory().getItemInMainHand(), "RealSurvival", lore));
	}
	
	@Command(cmd = "item",childCmds = "setNBT",args={"[Key]","[Value]"},des = "ItemSetNBT2Des",type = "Item",permissions = "RealSurvival.Admin")
	public void setItemNBT2(Player p, String args[]){
		if(args.length<3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		String lore = getLongText(args, 2, false);
		p.getInventory().setItemInMainHand(Data.nbtitem.addNBT(p.getInventory().getItemInMainHand(), "RealSurvival", lore));
	}
	
	@Command(cmd = "item",childCmds = "setlore",args={"[line]","[lore]"},des = "ItemSetLoreDes",type = "Item",permissions = "RealSurvival.Admin")
	public void setItemLore(Player p, String args[]){
		if(args.length<4){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		String lore = getLongText(args, 3, true);
		for(Entry<String, String> str:Data.label.entrySet())
			lore=lore.replaceAll("%"+str.getKey()+"%", str.getValue());
		ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
		Integer line=isInt(p, args[2], 2);
		if(line==null)return;
		List<String> Lore;
		if(im.hasLore())
			Lore=im.getLore();
		else
			Lore=new LinkedList<>();
		
		if(line>Lore.size())
			Lore.add(lore.replaceAll("%split%", Data.split));
		else
			Lore.set(line-1, lore.replaceAll("%split%", Data.split));
		
		im.setLore(Lore);
		p.getInventory().getItemInMainHand().setItemMeta(im);
	}
	
	@Command(cmd = "item",childCmds = "rLore",des = "ItemRemoveLoreDes",type = "Item",permissions = "RealSurvival.Admin")
	public void removeItemLore(Player p, String args[]){
		if(args.length!=2){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
		im.setLore(new LinkedList<>());
		p.getInventory().getItemInMainHand().setItemMeta(im);
	}
	
	@Command(cmd = "item",childCmds = "dLore",args={"[line]"},des = "ItemDeleteLoreDes",type = "Item",permissions = "RealSurvival.Admin")
	public void deleteItemLore(Player p, String args[]){
		if(args.length!=3){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		ItemMeta im = p.getInventory().getItemInMainHand().getItemMeta();
		Integer line=isInt(p, args[2], 2);
		if(line==null)return;
		if(!im.hasLore())
			return;
		
		List<String> Lore=im.getLore();
		
		if(line>Lore.size())
			Lore.remove(Lore.size()-1);
		else
			Lore.remove(line-1);
		
		im.setLore(Lore);
		p.getInventory().getItemInMainHand().setItemMeta(im);
	}
	
	//state command
	@Command(cmd = "states",args={"[PlayerName]"},des = "StatePlayerDes",type = "state",permissions = "RealSurvival.Admin")
	public void statePlayer(Player p, String args[]){
		if(args.length!=2){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		Player temp = plugin.getServer().getPlayer(args[1]);
		if(temp.isOnline()){
			Data.playerData.get(temp.getUniqueId()).sendData(p);
		}else{
		    Msg.sendMsgToPlayer(p, "player-is-offline", new String[]{"%player%"},new String[]{temp.getName()},true);
		}
	}
	
	@Command(cmd = "states",childCmds = "unlimited",args={"[PlayerName]","[true | false]"},des = "StateUnlimitedDes",type = "state",permissions = "RealSurvival.Admin")
	public void stateUnlimited(Player p, String args[]){
		if(args.length!=4){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return;
		}
		boolean bool = Boolean.parseBoolean(args[3]);
		
		Player temp = plugin.getServer().getPlayer(args[2]);
		PlayerData pd;
		if(Data.playerData.containsKey(temp.getUniqueId())){
			pd = Data.playerData.get(temp.getUniqueId());
			Data.playerData.remove(temp.getUniqueId());
		}else{
			pd = PlayerData.load(temp.getUniqueId());
		}
		pd.setUnlimited(bool);
		pd.save();
	}
	
	
	//player command
	@Command(cmd = "state",des = "StateDes",type = "HELP",permissions="")
	public void getState(Player p, String args[]){
		if(stateCD.contains(p.getUniqueId())){
			Msg.sendMsgToPlayer(p, "state-command-cooldown", true);
			return;
		}
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		pd.sendData(p);
		if(!p.isOp()){
			stateCD.addLast(p.getUniqueId());
			Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
				public void run() {
					stateCD.removeFirst();
				}
			}, Data.stateCD);
		}
	}
}
