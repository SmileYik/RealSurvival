package com.outlook.schooluniformsama.data;

import java.util.HashMap;

import com.outlook.schooluniformsama.data.recipes.Recipe;
import com.outlook.schooluniformsama.data.timer.Timer;

public class TempData {
	public static HashMap<String,Recipe> createRecipeTemp =new HashMap<>();
	public static HashMap<String,String> openingWorkbench=new HashMap<>();
	public static HashMap<String, Timer>createTimerTemp=new HashMap<>();
}
