package com.rach.tollparking.exception;

public class ParkingSlotCheckinException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ParkingSlotCheckinException(String message) {
    super(message);
  }
}
