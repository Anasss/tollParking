package com.rach.tollparking.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Map;

@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Setter(value = AccessLevel.PUBLIC)
@Getter
@Entity
public class PricingPolicy {
  @Id @GeneratedValue private long id;

  @ElementCollection
  @MapKeyColumn(name = "NAME")
  @Column(name = "VALUE")
  @CollectionTable(name = "PRICING_POLICY_PRICES", joinColumns = @JoinColumn(name = "PRICING_POLICY_ID"))
  private Map<String, Float> prices;

  private String formula;
}