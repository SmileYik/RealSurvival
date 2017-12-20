package com.outlook.schooluniformsama.command;

import java.io.File;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipes.*;
import com.outlook.schooluniformsama.event.basic.TempData;
import com.outlook.schooluniformsama.gui.*;
import com.outlook.schooluniformsama.util.Msg;
import org.bukkit.*;

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
		for(int i=0;i<needItem.length;i++)
			if(!args[i+1].equalsIgnoreCase(needItem[i])){
				Msg.sendMsgToPlayer(p, "BadCmd", true);
				return true;
			}
		return false;
	}
	
	//TODO help
	@Command(cmd = "help",des="HelpDes",type="Help")
	public void help(Player p,String args[]){}
	
	@Command(cmd = "help",args={"[Text]"},des="HelpDes2",type="Help")
	public void help2(Player p,String args[]){}
	
	@Command(cmd="help",args={"search","[Text]"},des="HelpDes3",type="HELP")
	public void helpSearch(Player p,String args[]){
		if(badCmd(p,args,new String[]{"search"},3))return;
		p.sendMessage(Msg.getMsg("Help2", new String[]{"%type%"}, new String[]{args[2]}, true));//以下是关于type的信息
		for(Method method:Commands.class.getDeclaredMethods()){
			if(!method.isAnnotationPresent(Command.class))
				continue;
			Command cmd=method.getAnnotation(Command.class);
			if(!(cmd.type().toLowerCase().contains(args[2].toLowerCase()) || cmd.cmd().equalsIgnoreCase(args[2]) || Msg.getMsg(cmd.des(), false).toLowerCase().contains(args[2].toLowerCase())))
				continue;
			if(!(cmd.permissions()!="" && p.hasPermission(cmd.permissions())))
				continue;
			String arg="";
			if(cmd.args()!=null||cmd.args().length!=0)
				for(String temp:cmd.args())
					arg+=temp+" ";
			p.sendMessage(ChatColor.translateAlternateColorCodes('&',"  &c&l/rs &3&l"+cmd.cmd()+" &3"+arg+"&f- &b&l"+Msg.getMsg(cmd.des(),false)));
		}
	}
	
	@Command(cmd = "help",args={"workbench"},des="WorkbenchHelpDes",type="Help")
	public void workbenchHelp(){}
	
	//TODO create recipe
	@Command(cmd = "cr",args={"furnace","[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "CreateFuranceRecipe", type = "workbench" ,permissions = "RealSurvival.Admin")
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
	
	@Command(cmd="cr",args={"furnace","setData","[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "ReplaceFuranceRecipeData", type = "workbench" ,permissions = "RealSurvival.Admin")
	public void replaceFuranceRecipeData(Player p,String args[]){
		if(badCmd(p,args,new String[]{"furnace","setData"},8))return;
		if(new File(FurnaceRecipe.getRecipePath(args[3])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeNotExists",new String[]{"%name%"} ,new String[]{args[3]}, true);
			return;
		}
		FurnaceRecipe fr = FurnaceRecipe.load(args[3]);
		
		try {
			fr.setNeedTime(isInt(p, args[4], 4));
			fr.setSaveTime(isInt(p, args[5], 5));
			fr.setMinTemperature(isDouble(p, args[6], 6));
			fr.setMinTemperature(isDouble(p, args[7], 7));
		} catch (Exception e) {
			return;
		}
		
		if(fr.save())
			Msg.sendMsgToPlayer(p, "ReplaceRecipeSucceed", true);
		else
			Msg.sendMsgToPlayer(p, "ReplaceRecipeFailed", true);
		return;
	}
	
	@Command(cmd = "cr",args={"workbench","[name]","[NeedTime]"}, des = "CreateWorkbenchRecipe", type = "workbench",permissions = "RealSurvival.Admin")
	public void createWorkbenceRecipe(Player p,String args[]){
		if(badCmd(p,args,new String[]{"workbench"},4))return;
		if(new File(WorkbenchRecipe.getRecipePath(args[2])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[2]}, true);
			return;
		}
		//if(isInt(p, args[3], 3)==null || isInt(p, args[4], 4)==null || isDouble(p,args[5],5)==null || isDouble(p,args[6],6)==null)return;
		try {
			TempData.createRecipeTemp.put(p.getName(), new WorkbenchRecipe(args[2], isInt(p, args[3], 3)));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Workbench.createWorkbenchRecipeGUI(args[2]));
		return;
	}
	
}
