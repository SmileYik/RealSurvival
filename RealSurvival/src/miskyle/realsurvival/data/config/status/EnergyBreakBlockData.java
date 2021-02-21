package miskyle.realsurvival.data.config.status;

public class EnergyBreakBlockData {
  
  private String blockType;
  private String toolLevel;

  /**
   * 初始化.

   * @param blockType 方块类型
   * @param toolLevel 工具等级
   */
  public EnergyBreakBlockData(String blockType, String toolLevel) {
    super();
    this.blockType = blockType;
    this.toolLevel = toolLevel;
  }

  public String getBlockType() {
    return blockType;
  }

  public void setBlockType(String blockType) {
    this.blockType = blockType;
  }

  public String getToolLevel() {
    return toolLevel;
  }

  public void setToolLevel(String toolLevel) {
    this.toolLevel = toolLevel;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((blockType == null) ? 0 : blockType.hashCode());
    result = prime * result + ((toolLevel == null) ? 0 : toolLevel.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;      
    }
    if (obj == null) {
      return false;      
    }
    if (getClass() != obj.getClass()) {
      return false;      
    }
    EnergyBreakBlockData other = (EnergyBreakBlockData) obj;
    if (blockType == null) {
      if (other.blockType != null) {
        return false;        
      }
    } else if (!blockType.equals(other.blockType)) {
      return false;      
    }
    if (toolLevel == null) {
      if (other.toolLevel != null) {
        return false;        
      }
    } else if (!toolLevel.equals(other.toolLevel)) {
      return false;      
    }
    return true;
  }

  @Override
  public String toString() {
    return "EnergyBreakBlockData [BlockType=" + blockType + ", ToolLevel=" + toolLevel + "]";
  }

}
