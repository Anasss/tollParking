package com.rach.tollparking.dao;
import com.rach.tollparking.entity.ParkingSlot;
import com.rach.tollparking.entity.ParkingSlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Data access layer for Entity {@link ParkingSlot}
 *
 * @author Anass RACH
 */
@Repository public interface ParkingSlotDao extends JpaRepository<ParkingSlot, Long> {

  ParkingSlot findBySlotId(long slotId);

  ParkingSlot findByVehicleId(UUID vehicleId);

  /**
   * Find all available parking Slots order by slotID.
   *
   * @param parkingSlotType
   * @return List of available parking slots
   */
  List<ParkingSlot> findBySlotTypeAndAndIsAvailableIsTrueOrderBySlotId(ParkingSlotType parkingSlotType);
}