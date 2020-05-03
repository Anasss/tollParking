package com.rach.tollparking.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity public class Bill {
  @Id private UUID vehicleId;

  private LocalDateTime checkinDate;

  private LocalDateTime checkoutDate;

  private Float price;

  private String currency;

  public Bill() {
  }

  public Bill(UUID vehicleId, LocalDateTime checkinDate, LocalDateTime checkoutDate, Float price, String currency) {
    this.vehicleId = vehicleId;
    this.checkinDate = checkinDate;
    this.checkoutDate = checkoutDate;
    this.price = price;
    this.currency = currency;
  }

  public UUID getVehicleId() {
    return vehicleId;
  }

  public void setVehicleId(UUID vehicleId) {
    this.vehicleId = vehicleId;
  }

  public LocalDateTime getCheckinDate() {
    return checkinDate;
  }

  public void setCheckinDate(LocalDateTime checkinDate) {
    this.checkinDate = checkinDate;
  }

  public LocalDateTime getCheckoutDate() {
    return checkoutDate;
  }

  public void setCheckoutDate(LocalDateTime checkoutDate) {
    this.checkoutDate = checkoutDate;
  }

  public Float getPrice() {
    return price;
  }

  public void setPrice(Float price) {
    this.price = price;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }
}