package miskyle.realsurvival.data.config;

public class EnergyBreakBlockData {
	private String BlockType;
	private String ToolLevel;
	
	public EnergyBreakBlockData(String blockType, String toolLevel) {
		super();
		BlockType = blockType;
		ToolLevel = toolLevel;
	}
	public String getBlockType() {
		return BlockType;
	}
	public void setBlockType(String blockType) {
		BlockType = blockType;
	}
	public String getToolLevel() {
		return ToolLevel;
	}
	public void setToolLevel(String toolLevel) {
		ToolLevel = toolLevel;
	}
	
	
}
