package com.example.rental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String registrationNumber;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleType type;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal dailyRate;

    private boolean available = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRegistrationNumber() { return registrationNumber; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public VehicleType getType() { return type; }
    public void setType(VehicleType type) { this.type = type; }
    public BigDecimal getDailyRate() { return dailyRate; }
    public void setDailyRate(BigDecimal dailyRate) { this.dailyRate = dailyRate; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
