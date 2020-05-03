package com.rach.tollparking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rach.tollparking.controller.ParkingSlotController;
import com.rach.tollparking.dao.ParkingSlotDao;
import com.rach.tollparking.entity.*;
import com.rach.tollparking.service.ParkingSlotService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingSlotController.class)
public class ParkingSlotControllerTests {

  private static final String FIXED_AMOUNT = "FixedAmount";

  private static final float FIXED_AMOUNT_VALUE = 6.50f;

  private static final String PARKING_NICE_ETOILE = "Parking Nice Etoile";

  private static final UUID billUuid = UUID.randomUUID();

  private static final UUID vehicleUuid = UUID.randomUUID();

  private static final String GET_AVAILABLE_STD_SLOTS_ENDPOINT = "/v1/shopping/parking-slots/{slotType}";

  private static final String POST_CHECKIN_PARKING_SLOT = "/v1/check-in/parking-slot/{slotId}/{slotType}";

  private static final String POST_CHECKOUT_PARKING_SLOT = "/v1/check-out/parking-slot/{vehicleId}";

  private static final String PARKING_SLOT_PATH_AVAILABLE = "$[0].available";

  @Autowired MockMvc mockMvc;

  @Autowired ObjectMapper mapper;

  @MockBean ParkingSlotDao parkingSlotDao;

  @MockBean ParkingSlotService parkingSlotService;

  @Test public void testGetAvailableSlotsByType() throws Exception {
    // Nominal cases
    testGetAvailableParkingSlots(ParkingSlotType.STD);
    testGetAvailableParkingSlots(ParkingSlotType.EC_20KW);
    testGetAvailableParkingSlots(ParkingSlotType.EC_50KW);

    // Invalid Type
    mockMvc.perform(
        MockMvcRequestBuilders.get(GET_AVAILABLE_STD_SLOTS_ENDPOINT, "invalidType").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    Mockito.when(parkingSlotDao.findBySlotTypeAndAndIsAvailableIsTrueOrderBySlotId(ParkingSlotType.STD))
        .thenReturn(null);

    // No parking slot found: 204 No content
    mockMvc.perform(MockMvcRequestBuilders.get(GET_AVAILABLE_STD_SLOTS_ENDPOINT, ParkingSlotType.STD.getName())
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
  }

  @Test public void testPostCheckin() throws Exception {
    // Nominal cases
    testParkingSlotCheckin(1, ParkingSlotType.STD);
    testParkingSlotCheckin(1, ParkingSlotType.EC_50KW);
    testParkingSlotCheckin(1, ParkingSlotType.EC_20KW);

    // Invalid Type
    mockMvc.perform(
        MockMvcRequestBuilders.post(POST_CHECKIN_PARKING_SLOT, 1, "invalidType").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    Mockito.when(parkingSlotDao.findBySlotId(1)).thenReturn(null);

    // Bad request
    mockMvc.perform(MockMvcRequestBuilders.post(POST_CHECKIN_PARKING_SLOT, 1, ParkingSlotType.STD.getName())
        .accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
  }

  @Test public void testPostCheckout() throws Exception {
    // Nominal case
    testParkingSlotCheckout(vehicleUuid);

    Mockito.when(parkingSlotDao.findByVehicleId(vehicleUuid)).thenReturn(null);

    // Bad request
    mockMvc.perform(
        MockMvcRequestBuilders.post(POST_CHECKOUT_PARKING_SLOT, vehicleUuid).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  public void testGetAvailableParkingSlots(ParkingSlotType parkingSlotType) throws Exception {
    List<ParkingSlot> slots = createParkingSlots(parkingSlotType);

    Mockito.when(parkingSlotDao.findBySlotTypeAndAndIsAvailableIsTrueOrderBySlotId(parkingSlotType)).thenReturn(slots);

    checkParkingSlots(parkingSlotType);
  }

  private void checkParkingSlots(ParkingSlotType parkingSlotType) throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get(GET_AVAILABLE_STD_SLOTS_ENDPOINT, parkingSlotType.getName())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)))
        .andExpect(MockMvcResultMatchers.jsonPath(PARKING_SLOT_PATH_AVAILABLE).exists())
        .andExpect(MockMvcResultMatchers.jsonPath(PARKING_SLOT_PATH_AVAILABLE).isBoolean())
        .andExpect(MockMvcResultMatchers.jsonPath(PARKING_SLOT_PATH_AVAILABLE).value(equalTo(true)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].slotId").value(equalTo(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[1].slotId").value(equalTo(2)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[2].slotId").value(equalTo(3)))

        .andExpect(MockMvcResultMatchers.jsonPath("$[0].parking").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].parking.name").value(equalTo(PARKING_NICE_ETOILE)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].parking.pricingPolicy").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].parking.pricingPolicy.id").value(equalTo(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].parking.pricingPolicy.prices").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].parking.pricingPolicy.formula").isNotEmpty())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0].parking.pricingPolicy.formula").value(equalTo(FIXED_AMOUNT)));
  }

  private List<ParkingSlot> createParkingSlots(ParkingSlotType parkingSlotType) {
    Parking parking = createParking();

    List<ParkingSlot> slots = new ArrayList<>();
    slots.add(new ParkingSlot(1, null, true, parking, null, parkingSlotType));
    slots.add(new ParkingSlot(2, null, true, parking, null, parkingSlotType));
    slots.add(new ParkingSlot(3, null, true, parking, null, parkingSlotType));
    return slots;
  }

  private Parking createParking() {
    Map<String, Float> prices = new HashMap<>();
    prices.put(FIXED_AMOUNT, FIXED_AMOUNT_VALUE);

    PricingPolicy simplePolicy = new PricingPolicy(1, prices, FIXED_AMOUNT);
    return new Parking(simplePolicy, PARKING_NICE_ETOILE);
  }

  public void testParkingSlotCheckin(long slotId, ParkingSlotType parkingSlotType) throws Exception {
    LocalDateTime checkinDateTime = LocalDateTime.of(2020, 05, 01, 15, 0, 0);
    Bill initialBill = new Bill(billUuid, checkinDateTime, null, 0.0f, "EUR");

    Parking parking = createParking();
    ParkingSlot parkSlot = new ParkingSlot(slotId, checkinDateTime, true, parking, initialBill, parkingSlotType);

    Mockito.when(parkingSlotDao.findBySlotId(slotId)).thenReturn(parkSlot);
    Mockito.when(parkingSlotService.checkin(parkSlot)).thenReturn(initialBill);

    mockMvc.perform(MockMvcRequestBuilders.post(POST_CHECKIN_PARKING_SLOT, slotId, parkingSlotType.getName())
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.vehicleId").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.checkinDate").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(equalTo(0.0)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value(equalTo("EUR")));
  }

  public void testParkingSlotCheckout(UUID vehicleId) throws Exception {
    LocalDateTime checkinDateTime = LocalDateTime.of(2020, 05, 01, 15, 0, 0);
    LocalDateTime checkoutDateTime = LocalDateTime.of(2020, 05, 01, 17, 0, 0);
    Bill finalBill = new Bill(billUuid, checkinDateTime, checkoutDateTime, 13.0f, "EUR");

    Parking parking = createParking();
    ParkingSlot parkSlot = new ParkingSlot(1, checkinDateTime, true, parking, finalBill, ParkingSlotType.STD);

    Mockito.when(parkingSlotDao.findByVehicleId(vehicleId)).thenReturn(parkSlot);

    mockMvc.perform(
        MockMvcRequestBuilders.post(POST_CHECKOUT_PARKING_SLOT, vehicleId).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.vehicleId").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.checkinDate").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.checkoutDate").exists())
        .andExpect(MockMvcResultMatchers.jsonPath("$.price").value(equalTo(6.5)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value(equalTo("EUR")));
  }
}