package com.devdad.Forma.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.model.Address;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
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

    public Address createAddress(Address address) {
        User currentUser = getCurrentUser();
        List<Address> existingAddresses = addressRepository.findAllByUserId(String.valueOf(currentUser.getId()));

        if (existingAddresses.size() >= MAX_ADDRESSES) {
            throw new IllegalStateException("Maximum of " + MAX_ADDRESSES + " addresses allowed");
        }

        address.setUser(currentUser);

        if (address.isDefault() || existingAddresses.isEmpty()) {
            for (Address addr : existingAddresses) {
                addr.setDefault(false);
            }
            addressRepository.saveAll(existingAddresses);
            address.setDefault(true);
        }

        return addressRepository.save(address);
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
