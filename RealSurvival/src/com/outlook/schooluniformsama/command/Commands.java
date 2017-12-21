package com.outlook.schooluniformsama.command;

import java.io.File;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import com.outlook.schooluniformsama.data.recipes.*;
import com.outlook.schooluniformsama.event.basic.TempData;
import com.outlook.schooluniformsama.gui.*;
import com.outlook.schooluniformsama.update.Update;
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
	
	@Command(cmd="help",args={"search","[Text]"},des="HelpDes2",type="HELP",permissions = "RealSurvival.Admin")
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
	@Command(cmd = "crf",args={"[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "CreateFuranceRecipeDes", type = "workbench" ,permissions = "RealSurvival.Admin")
	public void createFuranceRecipe(Player p,String args[]){
		if(badCmd(p,args,new String[]{},6))return;
		if(new File(FurnaceRecipe.getRecipePath(args[1])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[1]}, true);
			return;
		}
		try {
			TempData.createRecipeTemp.put(p.getName(), new FurnaceRecipe(args[1], isInt(p, args[2], 2),isInt(p, args[3], 3),isDouble(p,args[4],4),isDouble(p,args[5],5)));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Furnace.furnaceMakerGUI(args[1]));
		return;
	}
	
	@Command(cmd="crfsd",args={"[name]","[NeedTime]","[BadTime]","[MinTemperature]","[MaxTemperature]"}, des = "ReplaceFuranceRecipeDataDes", type = "workbench" ,permissions = "RealSurvival.Admin")
	public void replaceFuranceRecipeData(Player p,String args[]){
		if(badCmd(p,args,new String[]{},6))return;
		if(new File(FurnaceRecipe.getRecipePath(args[1])).exists()==false){
			Msg.sendMsgToPlayer(p, "RecipeNotExists",new String[]{"%name%"} ,new String[]{args[1]}, true);
			return;
		}
		FurnaceRecipe fr = FurnaceRecipe.load(args[1]);
		
		try {
			fr.setNeedTime(isInt(p, args[2], 2));
			fr.setSaveTime(isInt(p, args[3], 3));
			fr.setMinTemperature(isDouble(p, args[4], 4));
			fr.setMaxTemperature(isDouble(p, args[5], 5));
		} catch (Exception e) {
			return;
		}
		
		if(fr.save())
			Msg.sendMsgToPlayer(p, "ReplaceRecipeSucceed", true);
		else
			Msg.sendMsgToPlayer(p, "ReplaceRecipeFailed", true);
		return;
	}
	
	@Command(cmd = "crw",args={"[name]","[NeedTime]"}, des = "CreateWorkbenchRecipeDes", type = "workbench",permissions = "RealSurvival.Admin")
	public void createWorkbenceRecipe(Player p,String args[]){
		if(badCmd(p,args,new String[]{},3))return;
		if(new File(WorkbenchRecipe.getRecipePath(args[1])).exists()){
			Msg.sendMsgToPlayer(p, "RecipeExists",new String[]{"%name%"} ,new String[]{args[1]}, true);
			return;
		}
		try {
			TempData.createRecipeTemp.put(p.getName(), new WorkbenchRecipe(args[1], isInt(p, args[2], 2)));
		} catch (Exception e) {
			return;
		}
	     p.openInventory(Workbench.createWorkbenchRecipeGUI(args[1]));
		return;
	}
	
}
