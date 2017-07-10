package com.outlook.z815007320;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.outlook.z815007320.commands.HelpText;
import com.outlook.z815007320.commands.ItemCMD;
import com.outlook.z815007320.commands.StateCMD;
import com.outlook.z815007320.commands.TestCMD;
import com.outlook.z815007320.commands.WorkbenchCMD;

public class Commands{
	///rs item lore xxx xxx
	///rs item setlore int xxx xxx
	public static boolean onCommand(RealSurvival plug,CommandSender cmd, Command cmds, String label, String[] args) {
		if (!(cmd instanceof Player))
	    { 
			if(args.length==0){
	    	cmd.sendMessage("只有玩家才能执行此命令!");
	    	return true;
			}
			if(args[0].equalsIgnoreCase("Reload")){
				plug.reloadConfigs();
				cmd.sendMessage("§9[RealSurvival] §b配置文件重载成功!...");
				return true;
			}
	    }
		Player player = (Player)cmd;
		if(label.equalsIgnoreCase("rs")){
			if (args.length == 1)
		    {
				if(args[0].equalsIgnoreCase("state")){
					player.sendMessage(plug.getPlayerData(player).getData());
					return true;
				}else if(args[0].equalsIgnoreCase("help")){
					if(player.hasPermission("RealSurvival.Admin")){
						player.sendMessage(HelpText.getMainHelpText());
						return true;
					}else{
						player.sendMessage("§9===================§bRealSurvival§9===================\n"
															+ "§b/rs help - 查看帮助\n"
															+ "§b/rs state - 查看自身状态\n"
															+ "§9===================§b 玩法说明 §9===================\n"
															+ "§b你需要按时§9§l睡觉,喝水§b以保证身体健康\n"
															+ "§b你有可能§9§l生病§b,所以要随时准备好§9§l药\n"
															+ "§b当你§9§l跑步/潜行§b的时候,要注意§9§l体力§b是否充足\n"
															+ "§b别什么都往书包里塞, 你会被压得喘不过气的!\n"
															+ "§9==================================================");
						return true;
					}
				}else if(args[0].equalsIgnoreCase("Reload")&&player.hasPermission("RealSurvival.Admin")){
					plug.reloadConfigs();
					player.sendMessage("§9[RealSurvival] §b配置文件重载成功!...");
					return true;
				}
		    }else if(args.length == 2){
		    	if(args[0].equalsIgnoreCase("test")&&player.hasPermission("RealSurvival.Admin")){
		    		if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getTestHelp());
		    			return true;
		    		}
		    		if(args[1].equalsIgnoreCase("open"))
		    			return TestCMD.openTestEvent(player);
		    		if(args[1].equalsIgnoreCase("close"))
		    			return TestCMD.closeTestEvent(player);
		    		if(args[1].equalsIgnoreCase("on"))
		    			return TestCMD.onValue(player);
		    		if(args[1].equalsIgnoreCase("off"))
		    			return TestCMD.offValue(player);
		    	}
		    }
			
			if(args.length >= 2){
				//item 指令集
		    	if(args[0].equalsIgnoreCase("item")&&player.hasPermission("RealSurvival.Admin")){
		    		if(args[1].equalsIgnoreCase("lore")){
		    			ItemCMD.addLore(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("setlore")){
		    			ItemCMD.setLore(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("removelore")){
		    			ItemCMD.removeLore(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("setName")){
		    			ItemCMD.setName(player, args);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("save")){
		    			ItemCMD.saveItem(player, args[2]);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("list")){
		    			player.sendMessage(ItemCMD.getItemList());
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("get")){
		    			ItemStack is=ItemCMD.getItem(player, args[2]);
		    			if(is==null)return false;
		    			int amount=1;
		    			if(args.length==4)
		    				try {
								amount=Integer.parseInt(args[3]);
							} catch (Exception e) {
								player.sendMessage("§9[RealSurvival] §b数量错误!...");
							}
		    			for(int i=0;i<amount;i++)
		    				player.getInventory().addItem(is);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("give")){
		    			Player p=plug.getServer().getPlayer(args[2]);
		    			if(p==null||(!p.isOnline())){
		    				player.sendMessage("§9[RealSurvival] §b指定玩家不在线,,,");
		    				return false;
		    			}
		    			ItemStack is=ItemCMD.getItem(player, args[3]);
		    			if(is==null)return false;
		    			int amount=1;
		    			if(args.length==5)
		    				try {
								amount=Integer.parseInt(args[4]);
							} catch (Exception e) {
								player.sendMessage("§9[RealSurvival] §b数量错误!...");
							}
		    			for(int i=0;i<amount;i++)
	    					p.getInventory().addItem(is);
		    			return true;
		    		}else if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getItemHelp());
		    			return true;
		    		}
		    	}else if(args[0].equalsIgnoreCase("wb")&&player.hasPermission("RealSurvival.Admin")){
		    		//workbench指令集
		    		if(args[1].equalsIgnoreCase("csf")){
		    			if(args.length!=4)return false;
						 return WorkbenchCMD.createSF(player, args[2], args[3]);
						//重新设定配方
					}else if(args[1].equalsIgnoreCase("resetsf")){
						if(args.length!=4)return false;
						return WorkbenchCMD.resetSF(player, args[2], args[3]);
					}else if(args[1].equalsIgnoreCase("openwb")){
						if(args.length!=3)return false;
						WorkbenchCMD.openTestWorkbench(player, args[2]);
						return true;
					}else if(args[1].equalsIgnoreCase("openSFList")){
						WorkbenchCMD.openSFList(player);
						return true;
					}else if(args[1].equalsIgnoreCase("setBlock")){
						if(args.length!=4)return false;
						return WorkbenchCMD.setWorkbench(args[2], args[3]);
					}else if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getWorkbenchHelp());
		    			return true;
					}else if(args[1].equalsIgnoreCase("csff")){
						if(args.length!=6)return false;
						 return WorkbenchCMD.createFCTR(player, args[2], args[3],args[4],args[5]);
					}
		    	}else if(args[0].equalsIgnoreCase("setSwitch")&&player.hasPermission("RealSurvival.Admin")){
		    		//插件功能开关指令集
		    		if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getSetSwitchHelp());
		    			return true;
		    		}
		    		if(args.length<3)return false;
		    		if(!(args[1].equals("Sleep")||args[1].equals("Thirst")||
		    				args[1].equals("PhysicalStrength")||args[1].equals("Temperature")||
		    				args[1].equals("Weight")||args[1].equals("Sick")||
		    				args[2].equalsIgnoreCase("true")||args[2].equalsIgnoreCase("false"))){
		    			player.sendMessage(HelpText.getSetSwitchHelp());
		    			return false;
		    		}
		    		if(args[2].equalsIgnoreCase("true"))
		    			plug.getConfig().set("Switch."+args[1], true);
		    		else
		    			plug.getConfig().set("Switch."+args[1], false);
		    		try {
		    			plug.getConfig().save(plug.getDataFolder()+File.separator+"config.yml");
		    		} catch (IOException e) {
		    			return false;
		    		}
		    		return true;
		    	}else if(args[0].equalsIgnoreCase("state")&&player.hasPermission("RealSurvival.Admin")){
		    		if(args[1].equalsIgnoreCase("help")){
		    			player.sendMessage(HelpText.getStateHelp());
		    			return true;
		    		}else 
		    			return StateCMD.state(args, player, plug);
		    	}
		    }
			
			
		}
		
		return false;
	}
}
