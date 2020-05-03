package com.rach.tollparking.controller;

import com.rach.tollparking.dao.ParkingSlotDao;
import com.rach.tollparking.entity.Bill;
import com.rach.tollparking.entity.ParkingSlot;
import com.rach.tollparking.entity.ParkingSlotType;
import com.rach.tollparking.service.ParkingSlotService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Parking Toll Controller</br>
 * Defines API Interface. For now we expose 3 Entry-points:
 * <ul>
 *   <li>GET Available parking slots: {@link #parkingSlots(String)} </li>
 *   <li>POST Check-in a parking slot: {@link #parkingSlotCheckin(int, String)}</li>
 *   <li>POST Check-out a parking slot and issue the bill: {@link #parkingSlotCheckout(UUID)}</li>
 * </ul>
 *
 * @author Anass RACH
 */
@RestController @RequestMapping(value = "/v1")

@Api(tags = { "Toll Parking API" }) @SwaggerDefinition(tags = {

    @Tag(name = "Toll Parking API", description = "Parking Slots Management: Handling slots and issuing the bill.") })
public class ParkingSlotController {

  @Autowired private ParkingSlotDao parkingSlotDao;

  @GetMapping(value = "shopping/parking-slots/{slotType}")

  @ApiOperation(value = "Retrieve list of available parking slots by slotType", response = ParkingSlot.class)

  @ApiResponses(value = { @ApiResponse(code = 200, message = "List of available parking slots retrieved successfully"),

      @ApiResponse(code = 400, message = "Bad Request on performing parking slots api call"),

      @ApiResponse(code = 204, message = "No available parking slot found") })
  /*
   * Retrieve list of available parking slots by slotType.</br>
   * slotType should match one of the values of {@link ParkingSlotType}
   *
   * @param slotType
   * @return ResponseEntity with list of free Parking Slots
   */
  public ResponseEntity<List<ParkingSlot>> parkingSlots(@PathVariable String slotType) {
    ParkingSlotType parkingSlotType = ParkingSlotType.fromValue(slotType);

    if (parkingSlotType == null) {
      return ResponseEntity.badRequest().build();
    }

    List<ParkingSlot> slots = parkingSlotDao.findBySlotTypeAndAndIsAvailableIsTrueOrderBySlotId(parkingSlotType);

    if (CollectionUtils.isEmpty(slots)) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.ok(slots);
  }

  @PostMapping(value = "/check-in/parking-slot/{slotId}/{slotType}")

  @ApiOperation(value = "Check-in a parking slot and initiate the bill.", response = Bill.class)

  @ApiResponses(value = { @ApiResponse(code = 200, message = "Parking slot successfully checked-in"),

      @ApiResponse(code = 400, message = "This parking slot doesn't exist or is not available") })
  /*
   * Parking slot check-in management
   *
   * @param slotId
   * @param slotType
   * @return ResponseEntity with the initiated bill
   */
  public ResponseEntity<Bill> parkingSlotCheckin(@PathVariable int slotId, @PathVariable String slotType) {
    ParkingSlotService parkingSlotService = new ParkingSlotService();
    ParkingSlot parkingSlot = parkingSlotDao.findBySlotId(slotId);

    if (!parkingSlotService.isAvailable(parkingSlot)) {
      return ResponseEntity.badRequest().build();
    }

    Bill bill = parkingSlotService.checkin(parkingSlot);
    parkingSlotDao.save(parkingSlot);

    return ResponseEntity.ok(bill);
  }

  @PostMapping(value = "/check-out/parking-slot/{vehicleId}")

  @ApiOperation(value = "Check out a parking slot and issue the bill", response = Bill.class)

  @ApiResponses(value = { @ApiResponse(code = 200, message = "Parking slot successfully checked-out and bill issued."),

      @ApiResponse(code = 204, message = "Vehicle not found.") })
  /**
   * Parking slot check-out management</br>
   * Handle the check-out, issue the bill and free the parking slot.
   *
   * @param vehicleId
   * @return ResponseEntity with the issued bill
   */
  public ResponseEntity<Bill> parkingSlotCheckout(@PathVariable UUID vehicleId) {
    ParkingSlotService parkingSlotService = new ParkingSlotService();
    ParkingSlot parkingSlot = parkingSlotDao.findByVehicleId(vehicleId);

    if (parkingSlot == null) {
      return ResponseEntity.badRequest().build();
    }

    Bill bill = parkingSlotService.checkout(parkingSlot);

    parkingSlotService.reset(parkingSlot);
    parkingSlotDao.save(parkingSlot);

    return ResponseEntity.ok(bill);
  }
}