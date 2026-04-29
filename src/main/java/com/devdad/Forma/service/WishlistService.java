package com.devdad.Forma.service;

import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.User;
import com.devdad.Forma.model.UserPrinciple;
import com.devdad.Forma.model.Wishlist;
import com.devdad.Forma.repository.ProductRepository;
import com.devdad.Forma.repository.UserRepository;
import com.devdad.Forma.repository.WishlistRepository;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Wishlist addToWishlist(String id) {
        User user = getCurrentUser();
        Wishlist wishlist = user.getWishlist();

        if (wishlist == null) {
            wishlist = new Wishlist();
            wishlist.setUser(user);
            user.setWishlist(wishlist);
            // Ensure we save the user so that we don't have a null field
            userRepository.save(user);
        }

        boolean exists = wishlist.getProducts().size() > 0 ? wishlist.getProducts().stream()
                .anyMatch(p -> p.getId() == Integer.valueOf(id)) : false;

        if (!exists) {
            Product productToAddToWishlist = productRepository.findById(Integer.valueOf(id)).orElseThrow();
            wishlist.getProducts().add(productToAddToWishlist);
            wishlist = wishlistRepository.save(wishlist);
        }

        return wishlist;
    }

    public List<Product> getUserWishlist() {
        List<Product> products = wishlistRepository
                .findWishlistByUser(getCurrentUser())
                .getProducts();

        return products;
    }

    public boolean removeProductFromWishlist(String id) {
        try {
            int productId = Integer.parseInt(id);
            Wishlist wishlist = wishlistRepository.findWishlistByUser(getCurrentUser());
            if (wishlist == null) {
                return false;
            }
            List<Product> products = wishlist.getProducts();
            Optional<Product> productToRemove = products.stream()
                    .filter(p -> p.getId() == productId) // Use .equals() if Product ID is Integer type
                    .findFirst();
            if (productToRemove.isPresent()) {
                products.remove(productToRemove.get());
                wishlistRepository.save(wishlist); // Persist the association removal
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false; // Invalid ID format
        }
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple principle = (UserPrinciple) auth.getPrincipal();
        return principle.getUser();
    }

}
