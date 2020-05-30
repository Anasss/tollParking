package com.rach.tollparking.dao;

import com.rach.tollparking.entity.ParkingSlot;
import com.rach.tollparking.entity.ParkingSlotType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

/**
 * Data access layer for Entity {@link ParkingSlot}
 *
 * @author Anass RACH
 */
@Repository
@Transactional
public interface ParkingSlotDao extends JpaRepository<ParkingSlot, Long> {

  /**
   * Find ParkingSlot by slotId
   *
   * @param slotId
   * @return The associated ParkingSlot
   */
  ParkingSlot findBySlotId(long slotId);

  /**
   * Find ParkingSlot by vehicleId
   *
   * @param vehicleId
   * @return The associated ParkingSlot
   */
  ParkingSlot findByVehicleId(UUID vehicleId);

  /**
   * Find all available parking Slots order by slotID.
   *
   * @param parkingSlotType
   * @return List of available parking slots
   */
  List<ParkingSlot> findBySlotTypeAndAndIsAvailableIsTrueOrderBySlotId(ParkingSlotType parkingSlotType);
}