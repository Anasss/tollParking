package com.rach.tollparking.entity;

public enum ParkingSlotType {

  STD("STD"),
  EC_20KW("EC_20KW"),
  EC_50KW("EC_50KW");

  private String name;

  ParkingSlotType() {
  }

  ParkingSlotType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  /**
   * Find ParkingSlotType from a given String value.
   *
   * @param value
   * @return The associated {@link ParkingSlotType } or null if not found
   */
  public static ParkingSlotType fromValue(String value) {
    for (ParkingSlotType p : values()) {
      if (value.equals(p.getName())) {
        return p;
      }
    }
    return null;
  }
}