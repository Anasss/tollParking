package com.rach.tollparking.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Entity
public class Bill {
  @Id private UUID vehicleId;

  private LocalDateTime checkinDate;

  private LocalDateTime checkoutDate;

  private Float price;

  private String currency;
}