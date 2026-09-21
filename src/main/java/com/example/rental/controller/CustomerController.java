package com.example.rental.controller;

import com.example.rental.model.Customer;
import com.example.rental.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer create(@Valid @RequestBody Customer customer) {
        return customerService.create(customer);
    }

    @GetMapping
    public List<Customer> all() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer get(@PathVariable Long id) {
        return customerService.findById(id);
    }
}
