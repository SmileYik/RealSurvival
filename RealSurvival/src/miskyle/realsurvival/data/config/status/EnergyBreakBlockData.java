package miskyle.realsurvival.data.config.status;

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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((BlockType == null) ? 0 : BlockType.hashCode());
		result = prime * result + ((ToolLevel == null) ? 0 : ToolLevel.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnergyBreakBlockData other = (EnergyBreakBlockData) obj;
		if (BlockType == null) {
			if (other.BlockType != null)
				return false;
		} else if (!BlockType.equals(other.BlockType))
			return false;
		if (ToolLevel == null) {
			if (other.ToolLevel != null)
				return false;
		} else if (!ToolLevel.equals(other.ToolLevel))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "EnergyBreakBlockData [BlockType=" + BlockType + ", ToolLevel=" + ToolLevel + "]";
	}
	
	
}
