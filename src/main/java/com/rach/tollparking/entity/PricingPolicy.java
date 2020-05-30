package com.rach.tollparking.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
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