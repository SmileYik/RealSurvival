package miskyle.realsurvival.api.status;

public enum StatusType {
  SLEEP, THIRST, WEIGHT, ENERGY,
  /**
   * API方法没写包括则为不包括.
   */
  DISEASE, 
  /**
   * API方法没写包括则为不包括.
   */
  TEMPERATURE;
}
