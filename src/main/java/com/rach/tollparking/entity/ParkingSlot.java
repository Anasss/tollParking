package com.rach.tollparking.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity public class ParkingSlot {
  @Id @GeneratedValue private long slotId;

  private LocalDateTime checkinDate;

  private UUID vehicleId;

  private boolean isAvailable;

  @ManyToOne(cascade=CascadeType.ALL) private Parking parking;

  @OneToOne(cascade=CascadeType.ALL) protected Bill bill;

  @Enumerated(EnumType.STRING) private ParkingSlotType slotType;

  public ParkingSlot() {
  }

  public ParkingSlot(long slotId, LocalDateTime checkinDate, boolean isAvailable, Parking parking, Bill bill,
      ParkingSlotType slotType) {
    this.slotId = slotId;
    this.checkinDate = checkinDate;
    this.isAvailable = isAvailable;
    this.parking = parking;
    this.bill = bill;
    this.slotType = slotType;
  }

  public boolean isAvailable() {
    return isAvailable;
  }

  public void setAvailable(boolean available) {
    isAvailable = available;
  }

  public long getSlotId() {
    return slotId;
  }

  public void setSlotId(long slotId) {
    this.slotId = slotId;
  }

  public LocalDateTime getCheckinDate() {
    return checkinDate;
  }

  public void setCheckinDate(LocalDateTime checkinDate) {
    this.checkinDate = checkinDate;
  }

  public UUID getVehicleId() {
    return vehicleId;
  }

  public void setVehicleId(UUID vehicleId) {
    this.vehicleId = vehicleId;
  }

  public Parking getParking() {
    return parking;
  }

  public Bill getBill() {
    return bill;
  }

  public void setBill(Bill bill) {
    this.bill = bill;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    ParkingSlot that = (ParkingSlot)o;
    return slotId == that.slotId && Objects.equals(checkinDate, that.checkinDate) && Objects.equals(vehicleId,
        that.vehicleId) && Objects.equals(parking, that.parking) && Objects.equals(bill, that.bill) && Objects.equals(
        slotType, that.slotType);
  }

  @Override public int hashCode() {
    return Objects.hash(slotId, checkinDate, vehicleId, parking, bill, slotType);
  }
}