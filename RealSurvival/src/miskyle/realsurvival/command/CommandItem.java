package miskyle.realsurvival.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.github.miskyle.mcpt.i18n.I18N;

import miskyle.realsurvival.Msg;


public class CommandItem implements CommandExecutor, TabExecutor{
	protected ArrayList<Method> methods;
	protected Object itemCommands;
	private ArrayList<String[]> subCmds = new ArrayList<String[]>();
	
	public void initialization() {
		methods = new ArrayList<Method>();
		itemCommands = new ItemCommands();
		subCmds.clear();
		
		ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
		for(Method method : ItemCommands.class.getDeclaredMethods()) {
			if(!method.isAnnotationPresent(Cmd.class)) continue;
			methods.add(method);
			Cmd cmd = method.getAnnotation(Cmd.class);
			if(cmd.subCmd().length>0) {
				int index = 0;
				for(String subCmd : cmd.subCmd()) {
					if(subCmd!=null && !subCmd.isEmpty()) {
						if(temp.size()<=index) {
							ArrayList<String> temp2 = new ArrayList<String>();
							temp2.add(subCmd);
							temp.add(index,temp2);
						}else {
							ArrayList<String> temp2 = temp.get(index);
							if(!temp2.contains(subCmd))
								temp2.add(subCmd);
						}
					}
					index++;
				}
			}
		}
		for(int i = 0;i<temp.size();i++) {
			ArrayList<String> temp2 = temp.get(i);
			subCmds.add(i,temp2.toArray(new String[temp2.size()]));
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String topCmd, String[] args) {
		if(!(topCmd.equalsIgnoreCase("rsi")
				||topCmd.equalsIgnoreCase("rsItem")
				||topCmd.equalsIgnoreCase("RealSurvivalI")
				||topCmd.equalsIgnoreCase("RealSurvivalItem"))) {
			return false;
		}
		for(Method method : methods) {
			Cmd cmd = method.getAnnotation(Cmd.class);
			if(args.length < cmd.args().length)continue;
			if(!CommandManager.compareSubCommand(args, cmd.subCmd())) continue;
			if(!cmd.unlimitedLength() && args.length!=cmd.args().length)continue;
			if(cmd.needPlayer() && (sender instanceof Player)) {
				try {
					method.invoke(itemCommands, (Player)sender,args);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}else if(cmd.needPlayer()) {
				sender.sendMessage(Msg.getPrefix()+I18N.tr("cmd.warning.need-player"));
			}else {
				try {
					method.invoke(itemCommands, sender,args);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		sender.sendMessage(Msg.getPrefix()+I18N.tr("cmd.warning.wrong-command"));
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String a, String[] args) {
		if(args.length>subCmds.size())return new ArrayList<>();
		if(args.length == 0) return Arrays.asList(subCmds.get(0));
		return Arrays.stream(subCmds.get(args.length-1)).filter(
				s -> s.startsWith(args[args.length-1])).collect(Collectors.toList()); 
	}
}
