package com.outlook.schooluniformsama.data.player;

public enum EffectType {
	SLEEP("SLEEP"),THIRST("THIRST"),WEIGHT("WEIGHT"),TEMPERATURE("TEMPERATURE"),ENERGY("ENERGY");

    private String tableID ;

    private EffectType(String tableID) {
       this.tableID=tableID;
    }

    @Override
    public String toString() {
        return tableID;
    }
}
