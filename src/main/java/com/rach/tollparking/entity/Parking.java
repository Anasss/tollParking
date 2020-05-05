package com.rach.tollparking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Entity
public class Parking {
  @Id @GeneratedValue private long id;

  @OneToOne(cascade = CascadeType.ALL) private PricingPolicy pricingPolicy;

  @JsonProperty private String name;
}