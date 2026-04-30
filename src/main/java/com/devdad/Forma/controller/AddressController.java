package com.devdad.Forma.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devdad.Forma.model.Address;
import com.devdad.Forma.model.dto.address.AddressCreateRequestDTO;
import com.devdad.Forma.model.dto.address.AddressResponseDTO;
import com.devdad.Forma.service.AddressService;

@RestController
@RequestMapping("/api/auth/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @GetMapping
    public ResponseEntity<List<Address>> getCurrentUserAddresses() {
        return ResponseEntity.ok(addressService.getCurrentUserAddresses());
    }

    @PostMapping
    public ResponseEntity<AddressResponseDTO> createUserAddress(@RequestBody AddressCreateRequestDTO addressDTO) {
        return ResponseEntity.ok(addressService.createAddress(addressDTO));
    }

    @PutMapping
    public ResponseEntity<Address> updateUserAddress(@RequestBody Address address) {
        return ResponseEntity.ok(addressService.updateAddress(address));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Boolean> deleteUserAddress(@PathVariable String id) {
        return ResponseEntity.ok(addressService.deleteAddress(id));
    }
}
