package miskyle.realsurvival.data.blockarray;

public class BlockArrayData {
  private String north,northWest,west,southWest,south,southEast,east,northEast,main;

  public BlockArrayData(String north, String northWest, String west, String southWest, String south, String southEast,
      String east, String northEast, String main) {
    this.north = north;
    this.northWest = northWest;
    this.west = west;
    this.southWest = southWest;
    this.south = south;
    this.southEast = southEast;
    this.east = east;
    this.northEast = northEast;
    this.main = main;
  }

  public String getNorth() {
    return north;
  }

  public void setNorth(String north) {
    this.north = north;
  }

  public String getNorthWest() {
    return northWest;
  }

  public void setNorthWest(String northWest) {
    this.northWest = northWest;
  }

  public String getWest() {
    return west;
  }

  public void setWest(String west) {
    this.west = west;
  }

  public String getSouthWest() {
    return southWest;
  }

  public void setSouthWest(String southWest) {
    this.southWest = southWest;
  }

  public String getSouth() {
    return south;
  }

  public void setSouth(String south) {
    this.south = south;
  }

  public String getSouthEast() {
    return southEast;
  }

  public void setSouthEast(String southEast) {
    this.southEast = southEast;
  }

  public String getEast() {
    return east;
  }

  public void setEast(String east) {
    this.east = east;
  }

  public String getNorthEast() {
    return northEast;
  }

  public void setNorthEast(String northEast) {
    this.northEast = northEast;
  }

  public String getMain() {
    return main;
  }

  public void setMain(String main) {
    this.main = main;
  }
  
}
