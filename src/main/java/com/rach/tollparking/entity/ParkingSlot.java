package com.rach.tollparking.entity;

import lombok.*;

import javax.persistence.*;
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