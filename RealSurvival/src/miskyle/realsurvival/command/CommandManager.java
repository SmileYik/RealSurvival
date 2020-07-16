package miskyle.realsurvival.command;

public class CommandManager {
	
	
	public static boolean compareSubCommand(String[] args,String[] subCmd) {
		for(int i=0;i<subCmd.length;i++) {
			if(!subCmd[i].equalsIgnoreCase(args[i]))
				return false;
		}
		return true;
	}
}
