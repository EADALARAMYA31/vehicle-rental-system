package com.example.rental.service;

import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.exception.VehicleNotAvailableException;
import com.example.rental.model.Customer;
import com.example.rental.model.Rental;
import com.example.rental.model.RentalStatus;
import com.example.rental.model.Vehicle;
import com.example.rental.repository.CustomerRepository;
import com.example.rental.repository.RentalRepository;
import com.example.rental.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository,
                         VehicleRepository vehicleRepository,
                         CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Rental rentVehicle(Long vehicleId, Long customerId, LocalDate start, LocalDate end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End date must not be before start date");
        }
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + vehicleId));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + customerId));

        if (!vehicle.isAvailable()) {
            throw new VehicleNotAvailableException("Vehicle " + vehicleId + " is already rented");
        }

        long days = ChronoUnit.DAYS.between(start, end) + 1; // inclusive of both dates
        BigDecimal total = vehicle.getDailyRate().multiply(BigDecimal.valueOf(days));

        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        Rental rental = new Rental();
        rental.setVehicle(vehicle);
        rental.setCustomer(customer);
        rental.setStartDate(start);
        rental.setEndDate(end);
        rental.setTotalCost(total);
        rental.setStatus(RentalStatus.ACTIVE);
        return rentalRepository.save(rental);
    }

    @Transactional
    public Rental returnVehicle(Long rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new ResourceNotFoundException("Rental not found: " + rentalId));
        if (rental.getStatus() == RentalStatus.RETURNED) {
            throw new IllegalArgumentException("Rental " + rentalId + " is already returned");
        }
        rental.setStatus(RentalStatus.RETURNED);
        Vehicle vehicle = rental.getVehicle();
        vehicle.setAvailable(true);
        vehicleRepository.save(vehicle);
        return rentalRepository.save(rental);
    }

    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }
}
