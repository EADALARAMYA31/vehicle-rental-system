package com.example.rental.service;

import com.example.rental.exception.VehicleNotAvailableException;
import com.example.rental.model.Customer;
import com.example.rental.model.Rental;
import com.example.rental.model.RentalStatus;
import com.example.rental.model.Vehicle;
import com.example.rental.repository.CustomerRepository;
import com.example.rental.repository.RentalRepository;
import com.example.rental.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock private RentalRepository rentalRepository;
    @Mock private VehicleRepository vehicleRepository;
    @Mock private CustomerRepository customerRepository;
    @InjectMocks private RentalService rentalService;

    private Vehicle vehicle;
    private Customer customer;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setDailyRate(new BigDecimal("50.00"));
        vehicle.setAvailable(true);

        customer = new Customer();
        customer.setId(1L);
    }

    @Test
    void rentVehicle_calculatesCostAndMarksVehicleUnavailable() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(i -> i.getArgument(0));

        Rental rental = rentalService.rentVehicle(1L, 1L,
                LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 3));

        assertEquals(0, new BigDecimal("150.00").compareTo(rental.getTotalCost())); // 3 days x 50
        assertEquals(RentalStatus.ACTIVE, rental.getStatus());
        assertFalse(vehicle.isAvailable());
    }

    @Test
    void rentVehicle_throwsWhenVehicleAlreadyRented() {
        vehicle.setAvailable(false);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertThrows(VehicleNotAvailableException.class, () ->
                rentalService.rentVehicle(1L, 1L, LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2)));
    }

    @Test
    void rentVehicle_throwsWhenEndBeforeStart() {
        assertThrows(IllegalArgumentException.class, () ->
                rentalService.rentVehicle(1L, 1L, LocalDate.of(2026, 1, 5), LocalDate.of(2026, 1, 1)));
    }

    @Test
    void returnVehicle_marksRentalReturnedAndVehicleAvailable() {
        vehicle.setAvailable(false);
        Rental rental = new Rental();
        rental.setId(10L);
        rental.setVehicle(vehicle);
        rental.setStatus(RentalStatus.ACTIVE);

        when(rentalRepository.findById(10L)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(i -> i.getArgument(0));

        Rental result = rentalService.returnVehicle(10L);

        assertEquals(RentalStatus.RETURNED, result.getStatus());
        assertTrue(vehicle.isAvailable());
    }
}
