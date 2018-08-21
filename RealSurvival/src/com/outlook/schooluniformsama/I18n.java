package com.outlook.schooluniformsama;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.outlook.schooluniformsama.util.Msg;

public class I18n{
	
	private static I18n i18n;
	private final ResourceBundle defual;
	private ResourceBundle custum,local;
/*	private Locale defualLanguage =  Locale.CHINESE;
	private Locale currentLocale = Locale.getDefault();
	private Locale custom*/
	
	public I18n(RealSurvival plugin,String language){
		defual = ResourceBundle.getBundle("lang/messages", Locale.CHINESE);
		local = ResourceBundle.getBundle("lang/messages", Locale.getDefault());
		try{
			custum = ResourceBundle.getBundle("lang/messages", new Locale(language));
		}catch (Exception e) {
			custum = null;
			plugin.getLogger().log(Level.WARNING,"Missing Locale: "+language+"! Using Defual Locale: "+Locale.CHINESE);
		}
		i18n  = this;
	}
	
	private String tran(String key){
		if(key==null || key.isEmpty())return "";
		try {
			return custum.getString(key);
		} catch (Exception e) {
			try {
				return local.getString(key);
			} catch (Exception e2) {
				try {
					return defual.getString(key);									
				} catch (Exception e3) {
					return "Missing Key: "+key+". Please Notification op.";
				}
			}
		}
	}
	
	public static String tr(String key,Object ... objs){
		if(objs==null||objs.length==0)return i18n.tran(key);
		else return MessageFormat.format(i18n.tran(key), objs);
	}
	
	public static String tr(String key){
		return i18n.tran(key);
	}
	
	public static String trp(String key,Object ... objs){
		return Msg.getPrefix()+tr(key,objs);
	}
	
	public static String trp(String key){
		return Msg.getPrefix()+tr(key);
	}
	
	/*
	private static String lang = "zh_CN";
	private static String prefix;
	private static HashMap<String, String> msgLine = new HashMap<>();
	private static HashMap<String, String[]> msgList = new HashMap<>();
	
	@Test
	public void test(){
		//ResourceBundle rb = ResourceBundle.getBundle("messages", Locale.ENGLISH);
		System.out.println(Locale.getDefault());
	}
	
	
	public static void init(String lang){
		I18N.lang = lang;
		checkMessagesFile();
		loadMessagesFile();
		loadDefaultLanguage();
	}
	
	private static void checkMessagesFile(){
		
		if(!new File(Data.DATAFOLDER+"/messages.yml").exists())
			writeMessagesFile();
		String lang = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/messages.yml")).getString("language");
		if(lang == null || !lang.equals(I18N.lang)){
			I18N.lang = lang;
			writeMessagesFile();
		}
	}
	
	private static void loadMessagesFile(){
		YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(Data.DATAFOLDER+"/messages.yml"));
		msgList.put("player-state", config.getStringList("player-state").toArray(new String[config.getStringList("player-state").size()]));
		prefix = config.getString("prefix");
		for(String key:config.getKeys(true)){
			if(key.split(".")[0].equalsIgnoreCase("list")){
				List<String> list = config.getStringList(key);
				msgList.put(key, list.toArray(new String[list.size()]));
			}else{
				msgLine.put(key, config.getString(key));
			}
		}
	}
	
	private static void loadDefaultLanguage(){
		if(lang.equals("zh_CN"))return;
		InputStream is=null;
		try {
			is=Msg.class.getResourceAsStream("/lang/zh_CN.yml");
			YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
			if(prefix == null)prefix = config.getString("prefix");
			if(!msgList.containsKey("player-state"))config.getStringList("player-state").toArray(new String[config.getStringList("player-state").size()]);
			for(String key:config.getKeys(true)){
				if(key.split(".")[0].equalsIgnoreCase("list")){
					if(msgList.containsKey(key))continue;
					List<String> list = config.getStringList(key);
					msgList.put(key, list.toArray(new String[list.size()]));
				}else{
					if(msgLine.containsKey(key))continue;
					msgLine.put(key, config.getString(key));
				}
			}
			
			
			
		} catch (Exception e) {
			System.out.println("[ERROR | RealSurvival] > Load messages.yml failed!");
			e.printStackTrace();
		}finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	private static void writeMessagesFile(){
		InputStream is=null;
		OutputStream os=null;
		try {
			is=Msg.class.getResourceAsStream("/lang/"+I18N.lang+".yml");
			os=new FileOutputStream(new File(Data.DATAFOLDER+"/messages.yml"));
			int i;
			byte[] buffer = new byte[1024];
			while((i=is.read(buffer))!=-1)
				os.write(buffer,0,i);
			os.flush();
		} catch (Exception e) {
			System.out.println("[ERROR | RealSurvival] > Load messages.yml failed!");
			e.printStackTrace();
		}finally {
			if(is!=null)
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(os!=null)
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}*/
	

}
