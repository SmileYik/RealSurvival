package com.outlook.schooluniformsama.data;

import java.util.HashMap;

import com.outlook.schooluniformsama.data.recipe.Recipe;
import com.outlook.schooluniformsama.data.recipe.timer.Timer;

public class TempData {
	public static HashMap<String,Recipe> createRecipeTemp =new HashMap<>();
	public static HashMap<String,String> openingWorkbench=new HashMap<>();
	public static HashMap<String, Timer>createTimerTemp=new HashMap<>();
	public static HashMap<String,String>openRecipeViewer = new HashMap<>();
	
	public static void clear(){
		createRecipeTemp.clear();
		openingWorkbench.clear();
		openRecipeViewer.clear();
		createTimerTemp.clear();
	}
}
