package com.rach.tollparking.service;

import com.rach.tollparking.entity.Bill;
import com.rach.tollparking.entity.ParkingSlot;
import com.rach.tollparking.entity.PricingPolicy;

import javax.script.ScriptException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Parking Slot service</br>
 * Responsible of parking slot check-in and check-out management.
 *
 * @author Anass RACH
 */
public class ParkingSlotService {

  /**
   * Manage the check-in of a parking slot.
   *
   * @param parkingSlot
   * @return The initiated bill
   */
  public Bill checkin(ParkingSlot parkingSlot) {
    // Update parkingSlot info
    parkingSlot.setCheckinDate(LocalDateTime.now());
    parkingSlot.setVehicleId(UUID.randomUUID());
    parkingSlot.setAvailable(false);

    // Initiate the bill
    Bill bill = new Bill(parkingSlot.getVehicleId(), parkingSlot.getCheckinDate(), null, 0.0f, "EUR");
    parkingSlot.setBill(bill);
    return bill;
  }

  /**
   * Manage the checkout of a parking slot.
   *
   * @param parkingSlot
   * @return the issued bilL
   */
  public Bill checkout(ParkingSlot parkingSlot) {
    PricingPolicyService pricingPolicyService = new PricingPolicyService();
    parkingSlot.getBill().setCheckoutDate(LocalDateTime.now());

    PricingPolicy pricingPolicy = parkingSlot.getParking().getPricingPolicy();

    // Compute the price of the bill
    pricingPolicyService.computeBillPrice(pricingPolicy, parkingSlot.getBill());
    return parkingSlot.getBill();
  }

  /**
   * Free a parking slot by resetting its parameters.
   *
   * @param parkingSlot
   */
  public void reset(ParkingSlot parkingSlot) {
    parkingSlot.setBill(null);
    parkingSlot.setCheckinDate(null);
    parkingSlot.setVehicleId(null);
    parkingSlot.setAvailable(true);
  }

  /**
   * Checks the availability of a parkingSlot.
   *
   * @param parkingSlot
   * @return true if parkingSlot is not null and available, false otherwise.
   */
  public boolean isAvailable(ParkingSlot parkingSlot) {
    return parkingSlot != null && parkingSlot.isAvailable();
  }
}