package com.outlook.schooluniformsama.command;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.TempData;
import com.outlook.schooluniformsama.data.item.Items;
import com.outlook.schooluniformsama.data.item.RSItem;
import com.outlook.schooluniformsama.data.player.PlayerData;
import com.outlook.schooluniformsama.data.recipes.*;
import com.outlook.schooluniformsama.event.basic.CraftItemEvent;
import com.outlook.schooluniformsama.gui.*;
import com.outlook.schooluniformsama.update.Update;
import com.outlook.schooluniformsama.util.Msg;
import com.outlook.schooluniformsama.util.Util;

import app.RealSurvivalMaker;

import org.bukkit.*;
import org.bukkit.command.CommandSender;

public class Commands {
	
	private LinkedList<UUID> stateCD = new LinkedList<>();
	private Plugin plugin;
	public Commands(Plugin plugin) {
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
	//		
	@Command(cmd = "reload",des="ReloadDes",type="Update",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 1, hasChildCmds = false)
	public void reload(CommandSender p,String args[]){
		plugin.getServer().getPluginManager().disablePlugin(plugin);
		plugin.getServer().getPluginManager().enablePlugin(plugin);
	}
	
	@Command(cmd = "update",des="UpdateDes",type="Update",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 1, hasChildCmds = false)
	public void update(CommandSender p,String args[]){
		Update u = Update.getUpdate("update");
		if(u==null){
			p.sendMessage(Msg.getMsg( "CheckUpdateFailed", true));
			return;
		}
		if(u.hasUpdate()){
			if(u.getDownload()==null)return;
			if(u.getDownload().contains("{null-by-school_uniform}")){
				p.sendMessage(Msg.getPrefix()+u.getDownload().replace("{null-by-school_uniform}", ""));
				return;
			}
			u.download();
			p.sendMessage(Msg.getMsg("DownloadOver", new String[]{"%version%"},new String[]{u.getVersion_show()},true));
			p.sendMessage(Msg.getPrefix()+u.getUpdate_info());
			return;
		}else{
			p.sendMessage(Msg.getMsg( "IsNewVersion", true));
			return;
		}
	}
	
	@Command(cmd = "cupdate",des="CUpdateDes",type="Update",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 1, hasChildCmds = false)
	public void cUpdate(CommandSender p,String args[]){
		Update u = Update.getUpdate("update");
		if(u==null){
			p.sendMessage(Msg.getMsg( "CheckUpdateFailed", true));
			return;
		}
		if(u.hasUpdate()){
			p.sendMessage(Msg.getMsg("HasNewVersion", new String[]{"%now-version%","%version%"},new String[]{Update.now_version_show,u.getVersion_show()},true)+" "
					+(u.isReplace_config()?Msg.getMsg("WillDeleteConfig", false):"")+" "
					+(u.isReplace_message()?Msg.getMsg("WillDeleteMessages", false):""));
			p.sendMessage(Msg.getPrefix()+u.getUpdate_info());
			return;
		}else{
			p.sendMessage(Msg.getMsg( "IsNewVersion", true));
			return;
		}
	}
	
	@Command(cmd = "getUpdateInfo",des="UpdateInfoDes",type="Update",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 1, hasChildCmds = false)
	public void getUpdateInfo(CommandSender p,String args[]){
		Update u= Update.getUpdate("update");
		if(u==null){
			p.sendMessage(Msg.getMsg( "CheckUpdateFailed", true));
			return;
		}
		if(u.hasUpdate())
			u = Update.getUpdate("old/"+Update.now_version);
		if(u==null){
			p.sendMessage(Msg.getMsg( "CheckUpdateFailed", true));
			return;
		}
		p.sendMessage(Msg.getPrefix()+u.getUpdate_info());
		return;
	}
	
	@Command(cmd = "rsm",des="RealSurvivalMakerDes",type="help",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 1, hasChildCmds = false)
	public void openRealSurvivalMaker(CommandSender p,String args[]){
		RealSurvivalMaker.showGUI();
	}
	
	//TODO help
	@Command(cmd = "help",des="HelpDes",type="Help",needPlayer = false,argsLenght = 1, hasChildCmds = false)
	public void help(Player p,String args[]){}
	
	@Command(cmd = "help",childCmds="item",des="ItemHelpDes",type="Help",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 2, hasChildCmds = true)
	public void helpItem(Player p,String args[]){}
	
	@Command(cmd="shelp",args={"[Text]"},des="HelpDes2",type="HELP",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 2, hasChildCmds = false)
	public void helpSearch(CommandSender p,String args[]){
		
		if(args.length!=2){
			p.sendMessage(Msg.getMsg( "BadCmd", true));
			return;
		}
		
		p.sendMessage(Msg.getMsg("Help2", new String[]{"%type%"}, new String[]{args[1]}, true));
		for(Method method:Commands.class.getDeclaredMethods()){
			if(!method.isAnnotationPresent(Command.class))
				continue;
			Command cmd=method.getAnnotation(Command.class);
			if(!(cmd.type().toLowerCase().contains(args[1].toLowerCase()) || cmd.cmd().equalsIgnoreCase(args[1]) || Msg.getMsg(cmd.des(), false).toLowerCase().contains(args[1].toLowerCase())))
				continue;
			if(!(cmd.permissions()!="" && p.hasPermission(cmd.permissions())))
				continue;
			String arg="";
			if(cmd.hasChildCmds()){
				for(String cc : cmd.childCmds())
					arg+=cc+" ";
			}
			if(cmd.args()[0]!="")
				for(String temp:cmd.args())
					arg+=temp+" ";
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',"  &c&l/rs &3&l"+cmd.cmd()+" &3"+arg.replace("  ", " ")+"&f- &b&l"+Msg.getMsg(cmd.des(),false)));
		}
	}
	
	@Command(cmd = "help",childCmds="workbench",des="WorkbenchHelpDes",type="Help",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 2, hasChildCmds = true)
	public void workbenchHelp(){}
	
	@Command(cmd = "help",childCmds="update",des="Update2Des",type="Help",permissions = "RealSurvival.Admin",needPlayer = false, argsLenght = 2, hasChildCmds = true)
	public void update(){}
	
	//TODO create recipe
	@Command(cmd = "cr",childCmds="furnace",args={"[name]","[TableType]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "CreateFuranceRecipeDes", type = "workbench" ,permissions = "RealSurvival.Admin", argsLenght = 8, hasChildCmds = true)
	public void createFuranceRecipe(Player p,String args[]){
		
		if(new File(FurnaceRecipe.getRecipePath(args[2])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[2]}, true);
			return;
		}
		try {
			if(args[3].equalsIgnoreCase("null"))args[3] = null;
			TempData.createRecipeTemp.put(p.getName(), new FurnaceRecipe(args[2], 
					isInt(p, args[4], 4),isInt(p, args[5], 5),isDouble(p,args[6],6),isDouble(p,args[7],7),args[3]));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Furnace.furnaceMakerGUI(args[2]));
		return;
	}
	
	@Command(cmd="cr",childCmds="SetFurnace",args={"[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "ReplaceFuranceRecipeDataDes", type = "workbench" ,permissions = "RealSurvival.Admin", argsLenght = 7, hasChildCmds = true)
	public void replaceFuranceRecipeData(Player p,String args[]){
		
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
	
	@Command(cmd = "cr",childCmds="workbench",args={"[name]","[TableType]","[NeedTime]"}, des = "CreateWorkbenchRecipeDes", type = "workbench",permissions = "RealSurvival.Admin", argsLenght = 5, hasChildCmds = true)
	public void createWorkbenceRecipe(Player p,String args[]){
		
		if(new File(WorkbenchRecipe.getRecipePath(args[2])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[2]}, true);
			return;
		}
		try {
			if(args[3].equalsIgnoreCase("null"))args[3] = null;
			TempData.createRecipeTemp.put(p.getName(), new WorkbenchRecipe(args[2], isInt(p, args[4], 4),args[3]));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Workbench.createWorkbenchRecipeGUI(args[2]));
		return;
	}
	
	@Command(cmd = "cr",childCmds="view",args={"[workbenchType]","[RecipeName]"},des = "RecipeViewerDes",type = "workbench",permissions = "RealSurvival.Admin", argsLenght = 4, hasChildCmds = true)
	public void openRecipeViewer(Player p, String args[]){
		Inventory inv = FeatureGUI.openRecipeViewer(args[1], args[2]);
		if(inv==null){
			p.sendMessage(Msg.getMsg("recipe-not-find", true));
			return;
		}
		p.openInventory(inv);
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
	
	@Command(cmd = "item",childCmds = "save",args={"[FileName]"},des = "ItemSaveDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = -3, hasChildCmds = true)
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
	
	@Command(cmd = "item",childCmds = "load",args={"[FileName]"},des = "ItemLoadDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = -3, hasChildCmds = true)
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
	
	@Command(cmd = "item",childCmds = "delete",args={"[FileName]"},des = "ItemDeleteDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = -3, hasChildCmds = true)
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
	
	@Command(cmd = "item",childCmds = "setName",args={"[name]"},des = "ItemSetNameDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = -3, hasChildCmds = true)
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
	
	@Command(cmd = "item",childCmds = "lore",args={"[lore]"},des = "ItemAddLoreDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = -3, hasChildCmds = true)
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
	
	@Command(cmd = "item",childCmds = {"water","get"},args={"[name]"},des = "GetWaterDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = 4, hasChildCmds = true)
	public void getWater(Player p, String args[]){
		switch (args[3].toLowerCase()) {
		case "seawater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("Seawater"));
			break;
		case "iceseawater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("IceSeawater"));
			break;
		case "swampwater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("SwampWater"));
			break;
		case "lakewater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("LakeWater"));
			break;
		case "icelakewater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("IceLakeWater"));
			break;
		case "hotlakewater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("HotLakeWater"));
			break;
		case "freshwater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("Freshwater"));
			break;
		case "hotwater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("HotWater"));
			break;
		case "icewater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("IceWater"));
			break;
		case "rainwater":
			CraftItemEvent.givePlayerItem(p, Items.getWater("Rainwater"));
			break;
		default:
			Msg.sendMsgToPlayer(p, "NotFindItem", true);
			break;
		}
/*		
		Seawater:
		IceSeawater:
		SwampWater:
		LakeWater:
		IceLakeWater:
		HotLakeWater:
		Freshwater:
		HotWater:
		IceWater:
		Rainwater:*/
			
	}
	
	@Command(cmd = "item",childCmds = {"water","set"},args={"[name]"},des = "SetWaterDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = 4, hasChildCmds = true)
	public void setWater(Player p, String args[]){
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		try {
			switch (args[3].toLowerCase()) {
			case "seawater":
				Items.setWater("Seawater",p.getInventory().getItemInMainHand());
				break;
			case "iceseawater":
				Items.setWater("IceSeawater",p.getInventory().getItemInMainHand());
				break;
			case "swampwater":
				Items.setWater("SwampWater",p.getInventory().getItemInMainHand());
				break;
			case "lakewater":
				Items.setWater("LakeWater",p.getInventory().getItemInMainHand());
				break;
			case "icelakewater":
				 Items.setWater("IceLakeWater",p.getInventory().getItemInMainHand());
				break;
			case "hotlakewater":
				Items.setWater("HotLakeWater",p.getInventory().getItemInMainHand());
				break;
			case "freshwater":
				 Items.setWater("Freshwater",p.getInventory().getItemInMainHand());
				break;
			case "hotwater":
				Items.setWater("HotWater",p.getInventory().getItemInMainHand());
				break;
			case "icewater":
				 Items.setWater("IceWater",p.getInventory().getItemInMainHand());
				break;
			case "rainwater":
				Items.setWater("Rainwater",p.getInventory().getItemInMainHand());
				break;
			default:
				Msg.sendMsgToPlayer(p, "ItemSaveF", true);
				return;
			}
		} catch (IOException e) {
			Msg.sendMsgToPlayer(p, "ItemSaveF", true);
			return;
		}
		Msg.sendMsgToPlayer(p, "ItemSaveS",new String[]{"%name%"} ,new String[]{args[3]}, true);
/*		
		Seawater:
		IceSeawater:
		SwampWater:
		LakeWater:
		IceLakeWater:
		HotLakeWater:
		Freshwater:
		HotWater:
		IceWater:
		Rainwater:*/
	}
	
	
	@Command(cmd = "item",childCmds = {"water","getTypes"},des = "GetWaterTypeDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = 3, hasChildCmds = true,needPlayer = false)
	public void getWaterType(CommandSender p, String args[]){
		String types[] = {
				"=================RealSurvival=================",
				"Seawater: "+Msg.getMsg("Seawater", false),
				"IceSeawater: "+Msg.getMsg("IceSeawater", false),
				"SwampWater: "+Msg.getMsg("SwampWater", false),
				"LakeWater: "+Msg.getMsg("LakeWater", false),
				"IceLakeWater: "+Msg.getMsg("IceLakeWater", false),
				"HotLakeWater: "+Msg.getMsg("HotLakeWater", false),
				"Freshwater: "+Msg.getMsg("Freshwater", false),
				"HotWater: "+Msg.getMsg("HotWater", false),
				"IceWater: "+Msg.getMsg("IceWater", false),
				"Rainwater: "+Msg.getMsg("Rainwater", false),
				"=================Water Types================="
		};
		p.sendMessage(types);
	}
	
	@Command(cmd = "item",childCmds = "setNBT",args={"[NBTFileName]"},des = "ItemSetNBTDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = -3, hasChildCmds = true)
	public void setItemNBT(Player p, String args[]){
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		String lore = getLongText(args, 2, false);
		p.getInventory().setItemInMainHand(Data.nbtitem.addNBT(p.getInventory().getItemInMainHand(), "RealSurvival", lore));
	}
	
	@Command(cmd = "item",childCmds = "setNBT",args={"[Key]","[Value]"},des = "ItemSetNBT2Des",type = "Item",permissions = "RealSurvival.Admin", argsLenght = 4, hasChildCmds = true)
	public void setItemNBT2(Player p, String args[]){
		if(p.getInventory().getItemInMainHand()==null || p.getInventory().getItemInMainHand().getType()==Material.AIR){
			Msg.sendMsgToPlayer(p, "HandIsNull", true);
			return;
		}
		String lore = getLongText(args, 2, false);
		p.getInventory().setItemInMainHand(Data.nbtitem.addNBT(p.getInventory().getItemInMainHand(), args[2], lore));
	}
	
	@Command(cmd = "item",childCmds = "setlore",args={"[line]","[lore]"},des = "ItemSetLoreDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = -4, hasChildCmds = true)
	public void setItemLore(Player p, String args[]){
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
	
	@Command(cmd = "item",childCmds = "rLore",des = "ItemRemoveLoreDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = 2, hasChildCmds = true)
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
	
	@Command(cmd = "item",childCmds = "dLore",args={"[line]"},des = "ItemDeleteLoreDes",type = "Item",permissions = "RealSurvival.Admin", argsLenght = 3, hasChildCmds = true)
	public void deleteItemLore(Player p, String args[]){
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
	@Command(cmd = "states",args={"[PlayerName]"},des = "StatePlayerDes",type = "state",permissions = "RealSurvival.Admin", argsLenght = 2, hasChildCmds = false)
	public void statePlayer(Player p, String args[]){
		Player temp = plugin.getServer().getPlayer(args[1]);
		if(temp.isOnline()){
			Data.playerData.get(temp.getUniqueId()).sendData(false);
		}else{
		    Msg.sendMsgToPlayer(p, "player-is-offline", new String[]{"%player%"},new String[]{temp.getName()},true);
		}
	}
	
	@Command(cmd = "states",childCmds = "unlimited",args={"[PlayerName]","[true | false]"},des = "StateUnlimitedDes",type = "state",permissions = "RealSurvival.Admin", argsLenght = 4, hasChildCmds = true)
	public void stateUnlimited(Player p, String args[]){
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
		Data.addPlayer(temp);
	}
	
	//player command
	@Command(cmd = "state",des = "StateDes",type = "HELP", argsLenght = 1, hasChildCmds = false)
	public void getState(Player p, String args[]){
		PlayerData pd = Data.playerData.get(p.getUniqueId());
		if(pd==null){
			Msg.sendMsgToPlayer(p, "state-unlimited", true);
			return;
		}
		pd.sendData(stateCD.contains(p.getUniqueId()));
		
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
