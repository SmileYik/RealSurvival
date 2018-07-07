package com.outlook.schooluniformsama.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import com.outlook.schooluniformsama.data.Data;

public class Update {
	//https://raw.githubusercontent.com/SchoolUniform/RealSurvival/master/update/update.properties
	//https://raw.githubusercontent.com/SchoolUniform/RealSurvival/master/update/old/[version].properties
	public final static long now_version = 207;
	public final static String now_version_show="0.2.9";
	private long version;
	private String download;
	private String version_show;
	private String update_info;
	private boolean replace_config = false;
	private boolean replace_message = false;
	
	public static Update getUpdate(String url){
		Properties prop = new Properties();
		Update ud=new Update();
        try {
			InputStream in = new URL("https://raw.githubusercontent.com/SchoolUniform/RealSurvival/master/update/"+url+".properties").openStream();
			prop.load(new BufferedReader(new InputStreamReader(in,"utf8")));
			ud.version=Long.parseLong(prop.getProperty("version"));
			ud.update_info=prop.getProperty("update-info");
			ud.version_show=prop.getProperty("version-show");
			ud.download=prop.getProperty("download");
			ud.replace_config=Boolean.parseBoolean(prop.getProperty("replace-config"));
			ud.replace_message=Boolean.parseBoolean(prop.getProperty("replace-message"));
			in.close();
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		}catch (Exception e) {
			return null;
		}
        return ud;
	}
	
	public boolean hasUpdate(){
		return now_version<version;
	}
	
	public boolean download(){
		File path=null;
		try {
			path=new File(URLDecoder.decode(Update.class.getProtectionDomain().getCodeSource().getLocation().getPath(), "utf-8"));
		} catch (Exception e) {
			return false;
		}
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			fos = new FileOutputStream(path);
			bos = new BufferedOutputStream(fos);
			bis = new BufferedInputStream(new URL(download).openStream());
			int i;
			byte[] buffer = new byte[1024];
			while((i=bis.read(buffer))!=-1){
				bos.write(buffer,0,i);
			}
			bos.flush();
		} catch (Exception e) {
			return false;
		}finally {
			if(fos!=null)
				try {
					fos.close();
				} catch (IOException e) {
					return false;
				}
			if(bos!=null)
				try {
					bos.close();
				} catch (IOException e) {
					return false;
				}
			if(bis!=null)
				try {
					bis.close();
				} catch (IOException e) {
					return false;
				}
		}
		if(replace_config)
			new File(Data.DATAFOLDER+"/config.yml").delete();
		if(replace_message)
			new File(Data.DATAFOLDER+"/messages.yml").delete();
		return true;
	}

	public long getVersion() {
		return version;
	}

	public String getDownload() {
		return download;
	}

	public String getVersion_show() {
		return version_show;
	}

	public String getUpdate_info() {
		return update_info;
	}

	public boolean isReplace_config() {
		return replace_config;
	}

	public boolean isReplace_message() {
		return replace_message;
	}
	
}
