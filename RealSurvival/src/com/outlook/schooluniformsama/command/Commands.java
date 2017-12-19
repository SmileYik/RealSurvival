package com.outlook.schooluniformsama.command;

import java.io.File;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipes.FurnaceRecipe;
import com.outlook.schooluniformsama.event.basic.TempData;
import com.outlook.schooluniformsama.gui.Furnace;
import com.outlook.schooluniformsama.util.Msg;

public class Commands {
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
	
	private boolean badCmd(Player p, String[] args,String needItem[],int needLength){
		if(args.length!=needLength){
			Msg.sendMsgToPlayer(p, "BadCmd", true);
			return true;
		}
		for(int i=1;i<=needItem.length;i++)
			if(!args[i].equalsIgnoreCase(needItem[i])){
				Msg.sendMsgToPlayer(p, "BadCmd", true);
				return true;
			}
		return false;
	}
	
	//TODO help
	
	//TODO create recipe
	@Command(cmd = "cr",args={"furnace","[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "CreateFuranceRecipe", type = "workbench")
	public void createFuranceRecipe(Player p,String args[]){
		if(badCmd(p,args,new String[]{"furnace"},7))return;
		if(new File(FurnaceRecipe.getRecipePath(args[2])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[2]}, true);
			return;
		}
		//if(isInt(p, args[3], 3)==null || isInt(p, args[4], 4)==null || isDouble(p,args[5],5)==null || isDouble(p,args[6],6)==null)return;
		try {
			TempData.createRecipeTemp.put(p.getName(), new FurnaceRecipe(args[2], isInt(p, args[3], 3),isInt(p, args[4], 4),isDouble(p,args[5],5),isDouble(p,args[6],6)));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Furnace.furnaceMakerGUI(args[2]));
		return;
	}
}
