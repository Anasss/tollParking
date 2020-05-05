package com.rach.tollparking.exception;

public class ParkingSlotCheckoutException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ParkingSlotCheckoutException(String message) {
    super(message);
  }
}
