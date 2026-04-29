package com.devdad.Forma.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.Wishlist;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {

    Wishlist findWishlistByUser(User user);
}
