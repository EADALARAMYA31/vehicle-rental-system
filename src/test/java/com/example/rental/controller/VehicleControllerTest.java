package com.example.rental.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createVehicle_returns201AndAppearsInAvailableList() throws Exception {
        String json = """
                {"registrationNumber":"AP39XY1234","make":"Toyota","model":"Innova",
                 "type":"SUV","dailyRate":2500.00}
                """;

        mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.available").value(true));

        mockMvc.perform(get("/api/vehicles/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.registrationNumber=='AP39XY1234')]").exists());
    }

    @Test
    void createVehicle_withBlankMake_returns400() throws Exception {
        String json = """
                {"registrationNumber":"AP39ZZ0001","make":"","model":"X","type":"CAR","dailyRate":100}
                """;

        mockMvc.perform(post("/api/vehicles").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.make").exists());
    }
}
