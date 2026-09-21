package com.example.rental.controller;

import com.example.rental.dto.RentalRequest;
import com.example.rental.model.Rental;
import com.example.rental.service.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rental rent(@Valid @RequestBody RentalRequest request) {
        return rentalService.rentVehicle(
                request.vehicleId(), request.customerId(), request.startDate(), request.endDate());
    }

    @PutMapping("/{id}/return")
    public Rental returnVehicle(@PathVariable Long id) {
        return rentalService.returnVehicle(id);
    }

    @GetMapping
    public List<Rental> all() {
        return rentalService.findAll();
    }
}
