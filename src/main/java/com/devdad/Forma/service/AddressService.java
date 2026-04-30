package com.devdad.Forma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.devdad.Forma.model.Address;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.dto.address.AddressCreateRequestDTO;
import com.devdad.Forma.model.dto.address.AddressResponseDTO;
import com.devdad.Forma.repository.AddressRepository;

@Service
public class AddressService {

    private static final int MAX_ADDRESSES = 3;

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getCurrentUserAddresses() {
        User currentUser = getCurrentUser();
        return addressRepository.findAllByUserId(String.valueOf(currentUser.getId()));
    }

    public AddressResponseDTO createAddress(AddressCreateRequestDTO dto) {
        User currentUser = getCurrentUser();
        List<Address> existingAddresses = addressRepository.findAllByUserId(String.valueOf(currentUser.getId()));

        if (existingAddresses.size() >= MAX_ADDRESSES) {
            throw new ResponseStatusException(HttpStatus.valueOf(422),
                    "Maximum of " + MAX_ADDRESSES + " addresses allowed");
        }

        Address address = new Address();
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setCountry(dto.country());
        address.setZipCode(dto.zipCode());
        address.setUser(currentUser);

        if (dto.isDefault() || existingAddresses.isEmpty()) {
            for (Address addr : existingAddresses) {
                addr.setDefault(false);
            }
            addressRepository.saveAll(existingAddresses);
            address.setDefault(true);
        }

        addressRepository.save(address);

        return new AddressResponseDTO(address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getCountry(),
                address.getZipCode(),
                address.isDefault(),
                address.getUser().getId());
    }

    public Address updateAddress(Address address) {
        User currentUser = getCurrentUser();
        List<Address> existingAddresses = addressRepository.findAllByUserId(String.valueOf(currentUser.getId()));

        Address addressToUpdate = existingAddresses.stream()
                .filter(a -> a.getId() == address.getId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        addressToUpdate.setStreet(address.getStreet());
        addressToUpdate.setCity(address.getCity());
        addressToUpdate.setState(address.getState());
        addressToUpdate.setZipCode(address.getZipCode());
        addressToUpdate.setCountry(address.getCountry());

        if (address.isDefault() && !addressToUpdate.isDefault()) {
            for (Address addr : existingAddresses) {
                if (addr.getId() == address.getId()) {
                    addr.setDefault(false);
                }
            }
            addressRepository.saveAll(existingAddresses);
            addressToUpdate.setDefault(true);
        }

        return addressRepository.save(addressToUpdate);
    }

    public boolean deleteAddress(String addressId) {
        User currentUser = getCurrentUser();
        List<Address> userAddresses = addressRepository.findAllByUserId(String.valueOf(currentUser.getId()));

        Address addressToDelete = userAddresses.stream()
                .filter(a -> a.getId() == Integer.valueOf(addressId))
                .findFirst()
                .orElse(null);

        if (addressToDelete != null) {
            addressRepository.delete(addressToDelete);
            return true;
        }
        return false;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
        return principle.getUser();
    }
}
