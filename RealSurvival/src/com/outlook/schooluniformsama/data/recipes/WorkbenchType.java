package com.outlook.schooluniformsama.data.recipes;

public enum WorkbenchType {
	WORKBENCH("WORKBENCH"),FURNACE("FURNACE"),RAINWATER_COLLECTOR("RAINWATER_COLLECTOR");

    private String tableID ;

    private WorkbenchType(String tableID) {
       this.tableID=tableID;
    }

    @Override
    public String toString() {
        return tableID;
    }
}
