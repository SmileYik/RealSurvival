package com.outlook.schooluniformsama.data.effect;

public enum EffectType {
	SLEEP("SLEEP"),THIRST("THIRST"),WEIGHT("WEIGHT"),TEMPERATURE("TEMPERATURE"),IMMUNE("IMMUNE"),ENERGY("ENERGY");

    private String tableID ;

    private EffectType(String tableID) {
       this.tableID=tableID;
    }

    @Override
    public String toString() {
        return tableID;
    }
}
