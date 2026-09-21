package com.example.rental.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record RentalRequest(
        @NotNull Long vehicleId,
        @NotNull Long customerId,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate) {
}
