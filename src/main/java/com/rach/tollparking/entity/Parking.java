package com.rach.tollparking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity public class Parking {
  @Id @GeneratedValue private long id;

  @OneToOne(cascade=CascadeType.ALL) private PricingPolicy pricingPolicy;

  @JsonProperty
  private String name;

  public Parking(PricingPolicy pricingPolicy, String name) {
    this.pricingPolicy = pricingPolicy;
    this.name = name;
  }

  public Parking() {
  }

  public PricingPolicy getPricingPolicy() {
    return pricingPolicy;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Parking parking = (Parking)o;
    return id == parking.id && Objects.equals(pricingPolicy, parking.pricingPolicy) && Objects.equals(name,
        parking.name);
  }

  @Override public int hashCode() {
    return Objects.hash(id, pricingPolicy, name);
  }
}