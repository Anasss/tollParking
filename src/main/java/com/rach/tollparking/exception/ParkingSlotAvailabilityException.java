package com.rach.tollparking.exception;

public class ParkingSlotAvailabilityException extends RuntimeException{
  private static final long serialVersionUID = 1L;

  public ParkingSlotAvailabilityException(String message) {
    super(message);
  }
}
