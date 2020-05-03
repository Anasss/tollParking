package com.rach.tollparking.service;

import com.rach.tollparking.entity.Bill;
import com.rach.tollparking.entity.PricingPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Pricing Policy Service</br>
 * Responsible of computing the price of the bill of a parking slot.
 *
 * @author Anass RACH
 */
public class PricingPolicyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(PricingPolicyService.class);

  private static final String UNABLE_TO_COMPUTE_BILL_PRICE = "Unable to compute Bill Price using ScriptEngine: %s";

  /**
   * Compute the price of the bill using the checkoutDate and the pricingPolicy prices and formula.
   *
   * @param pricingPolicy
   * @param bill
   */
  public void computeBillPrice(PricingPolicy pricingPolicy, Bill bill) {
    ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
    ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("JavaScript");

    Map<String, Float> prices = pricingPolicy.getPrices();

    // Prepare the scriptEngine with formula parameters
    prices.forEach((name, value) -> scriptEngine.put(name, value));
    scriptEngine.put("Hours", bill.getCheckinDate().until(bill.getCheckoutDate(), ChronoUnit.HOURS) + 1);

    try {
      float computedPrice = Float.valueOf(scriptEngine.eval(pricingPolicy.getFormula()).toString());
      bill.setPrice(computedPrice);
    } catch (ScriptException e) {
      LOGGER.error(String.format(UNABLE_TO_COMPUTE_BILL_PRICE, e.getMessage()));
    }
  }
}