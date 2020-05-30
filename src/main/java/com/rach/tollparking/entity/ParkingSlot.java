package com.rach.tollparking.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Entity
public class ParkingSlot {
  @Id @GeneratedValue private long slotId;

  private LocalDateTime checkinDate;

  private UUID vehicleId;

  private boolean isAvailable;

  @ManyToOne(cascade = CascadeType.ALL) private Parking parking;

  @OneToOne(cascade = CascadeType.ALL) protected Bill bill;

  @Enumerated(EnumType.STRING) private ParkingSlotType slotType;
}