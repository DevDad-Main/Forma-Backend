package com.devdad.Forma.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.devdad.Forma.model.User;
import com.devdad.Forma.model.Wishlist;
import com.devdad.Forma.model.dto.wishlist.WishlistResponseDTO;
import static com.devdad.Forma.testutil.ProductTestData.product1;

class WishlistMapperTest {

    @Nested
    @DisplayName("toDTO")
    class ToDTO {

        @Test
        void shouldMapWishlistToWishlistResponseDTO() {
            User user = new User();
            user.setId(1);

            Wishlist wishlist = new Wishlist();
            wishlist.setId(10);
            wishlist.setProducts(List.of(product1()));
            wishlist.setUser(user);

            WishlistResponseDTO dto = WishlistMapper.toDTO(wishlist);

            assertAll(
                    () -> assertEquals(10, dto.id()),
                    () -> assertEquals(1, dto.products().size()),
                    () -> assertEquals(product1().getName(), dto.products().getFirst().name()),
                    () -> assertEquals(1, dto.userId()));
        }

        @Test
        void shouldMapEmptyWishlist() {
            User user = new User();
            user.setId(2);

            Wishlist wishlist = new Wishlist();
            wishlist.setId(0);
            wishlist.setProducts(List.of());
            wishlist.setUser(user);

            WishlistResponseDTO dto = WishlistMapper.toDTO(wishlist);

            assertAll(
                    () -> assertEquals(0, dto.id()),
                    () -> assertTrue(dto.products().isEmpty()),
                    () -> assertEquals(2, dto.userId()));
        }
    }
}
