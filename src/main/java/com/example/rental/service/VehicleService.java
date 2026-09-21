package com.example.rental.service;

import com.example.rental.exception.ResourceNotFoundException;
import com.example.rental.model.Vehicle;
import com.example.rental.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public Vehicle create(Vehicle vehicle) {
        vehicle.setId(null);
        vehicle.setAvailable(true);
        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> findAvailable() {
        return vehicleRepository.findByAvailableTrue();
    }

    public Vehicle findById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + id));
    }

    public void delete(Long id) {
        vehicleRepository.delete(findById(id));
    }
}
