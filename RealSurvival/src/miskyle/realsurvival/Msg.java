package miskyle.realsurvival;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.*;

import org.bukkit.configuration.file.YamlConfiguration;

import com.github.miskyle.mcpt.MCPT;

public class Msg {
	private static Msg msg;
	private String playerState1="",playerState2="";
	private String prefix;
	private final HashMap<String, String[]> defualt;
	
	public Msg(RealSurvival plugin){
		checkMessagesYml(plugin);
		
		HashMap<String, String[]> defualt = new HashMap<>();
		YamlConfiguration config = YamlConfiguration.loadConfiguration(
				new File(MCPT.plugin.getDataFolder()+"/messages.yml"));
		for(String key:config.getKeys(true)){
			if(key.equals("player-state-1")) {
				List<String> msgs = config.getStringList(key);
				String[] msgsString = msgs.toArray(new String[msgs.size()]);
				for(String str:msgsString) playerState1 += str+"\n";
				playerState1 = playerState1.substring(0, playerState1.length()-2);
			}else if(key.equals("player-state-2")){
				List<String> msgs = config.getStringList(key);
				String[] msgsString = msgs.toArray(new String[msgs.size()]);
				for(String str:msgsString) playerState2 += str+"\n";
				playerState2 = playerState2.substring(0, playerState2.length()-2);
			}else if(key.equals("prefix")){
				prefix = config.getString(key);
			}else{
				List<String> msgs = config.getStringList(key);
				defualt.put(key, msgs.toArray(new String[msgs.size()]));
			}
		}
		this.defualt = defualt;
		msg = this;
	}
	
	private String getMessages(String key){
		if(!defualt.containsKey(key))return "";
		String[] msgs = defualt.get(key);
		return msgs[(int)(Math.random()*msgs.length)];
	}
	
	/**
	 * 获取玩家的状态
	 * @param objects
	 * @return
	 */
	public static String getPlayerState1(Object ...objects ){
		return MessageFormat.format(msg.playerState1,objects);
	}
	public static String getPlayerState2(Object ...objects ){
		return MessageFormat.format(msg.playerState2,objects);
	}
	public static String getPrefix(){
		return msg.prefix;
	}
	
	public static String tr(String key,Object ... objs){
		return MessageFormat.format(msg.getMessages(key), objs);
	}
	
	public static String tr(String key){
		return msg.getMessages(key);
	}
	
	public static String trp(String key,Object ... objs){
		return msg.prefix+MessageFormat.format(msg.getMessages(key), objs);
	}
	
	public static String trp(String key){
		return msg.prefix+msg.getMessages(key);
	}
	
	private void checkMessagesYml(RealSurvival rs){
		if(!new File(rs.getDataFolder()+File.separator+"messages.yml").exists()){
			writerYml("messages_"+rs.getConfig().getString("language"));
		}else if(YamlConfiguration.loadConfiguration(
				new File(rs.getDataFolder()+File.separator+"messages.yml")).getString("language",null)==null 
				|| !YamlConfiguration.loadConfiguration(
						new File(rs.getDataFolder()+File.separator+"messages.yml")).getString("language",null)
							.equalsIgnoreCase(rs.getConfig().getString("language"))){
			writerYml("messages_"+rs.getConfig().getString("language"));
		}
	}
	
	private void writerYml(String name){
		InputStream is=null;
		OutputStream os=null;
		try {
			is=Msg.class.getResourceAsStream("/lang/"+name+".yml");
			os=new FileOutputStream(new File(MCPT.plugin.getDataFolder()+"/messages.yml"));
			int i;
			byte[] buffer = new byte[1024];
			while((i=is.read(buffer))!=-1)
				os.write(buffer,0,i);
			os.flush();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
	}
}