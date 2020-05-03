package com.rach.tollparking.entity;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;

@Entity
public class PricingPolicy {
  @Id @GeneratedValue private long id;

  @ElementCollection
  @MapKeyColumn(name="NAME")
  @Column(name="VALUE")
  @CollectionTable(name="PRICING_POLICY_PRICES", joinColumns=@JoinColumn(name="PRICING_POLICY_ID"))
  private Map<String, Float> prices;

  private String formula;

  public PricingPolicy() {
  }

  public PricingPolicy(long id, Map<String, Float> prices, String formula) {
    this.id = id;
    this.prices = prices;
    this.formula = formula;
  }

  public long getId() {
    return id;
  }

  public Map<String, Float> getPrices() {
    return prices;
  }

  public String getFormula() {
    return formula;
  }

  @Override public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    PricingPolicy that = (PricingPolicy)o;
    return id == that.id && Objects.equals(prices, that.prices) && Objects.equals(formula, that.formula);
  }

  @Override public int hashCode() {
    return Objects.hash(id, prices, formula);
  }
}