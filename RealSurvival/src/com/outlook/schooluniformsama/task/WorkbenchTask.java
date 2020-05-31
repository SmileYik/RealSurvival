package com.outlook.schooluniformsama.task;

import com.outlook.schooluniformsama.data.Data;
import com.outlook.schooluniformsama.data.recipe.timer.FurnaceTimer;
import com.outlook.schooluniformsama.data.recipe.timer.RainwaterCollectorTimer;
import com.outlook.schooluniformsama.data.recipe.timer.Timer;
import com.outlook.schooluniformsama.data.recipe.timer.WorkbenchTimer;

public class WorkbenchTask implements Runnable{
	@Override
	public void run() {
		for(Timer tt:Data.timer.values()){
			switch(tt.getType()){
				case FURNACE:
					((FurnaceTimer)tt).subTime();
					break;
				case WORKBENCH:
					((WorkbenchTimer)tt).subTime();
					break;
				case RAINWATER_COLLECTOR:
					((RainwaterCollectorTimer)tt).subTime();
					break;
			}
		}
	}
}
