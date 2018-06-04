package com.outlook.schooluniformsama.data.effect;

public enum EffectType {
	SLEEP_P("SLEEP_PP"),
	SLEEP_L("SLEEP_PL"),
	THIRST_P("THIRST_PP"),
	THIRST_L("THIRST_PL"),
	ENERGY_P("ENERGY_PP"),
	ENERGY_L("ENERGY_PL"),
	WEIGHT("WEIGHT"),
	TEMPERATURE("TEMPERATURE"),
	IMMUNE("IMMUNE"),
	CURESPEED("CURESPEED")
;

    private String tableID ;

    private EffectType(String tableID) {
       this.tableID=tableID;
    }

    @Override
    public String toString() {
        return tableID;
    }
    
    public static EffectType getByName(String name){
    	try {
			return EffectType.valueOf(name.toUpperCase());
		} catch (Exception e) {
			return null;
		}
    }
}
