package com.example.rental.controller;

import com.example.rental.model.Vehicle;
import com.example.rental.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Vehicle create(@Valid @RequestBody Vehicle vehicle) {
        return vehicleService.create(vehicle);
    }

    @GetMapping
    public List<Vehicle> all() {
        return vehicleService.findAll();
    }

    @GetMapping("/available")
    public List<Vehicle> available() {
        return vehicleService.findAvailable();
    }

    @GetMapping("/{id}")
    public Vehicle get(@PathVariable Long id) {
        return vehicleService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        vehicleService.delete(id);
    }
}
