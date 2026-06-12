package com.devdad.Forma.mapper;

import static com.devdad.Forma.testutil.ProductTestData.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.devdad.Forma.model.Product;
import com.devdad.Forma.model.dto.product.ProductCreateRequestDTO;
import com.devdad.Forma.model.dto.product.ProductResponseDTO;

class ProductMapperTest {

    @Nested
    @DisplayName("toDTO")
    class ToDTO {

        @Test
        void shouldMapProductToProductResponseDTO() {
            Product product = product1();

            ProductResponseDTO dto = ProductMapper.toDTO(product);

            assertAll(
                    () -> assertEquals(product.getId(), dto.id()),
                    () -> assertEquals(product.getName(), dto.name()),
                    () -> assertEquals(product.getPrice(), dto.price()),
                    () -> assertEquals(product.getOriginalPrice(), dto.originalPrice()),
                    () -> assertEquals(product.getImage(), dto.image()),
                    () -> assertEquals(product.getHoverImage(), dto.hoverImage()),
                    () -> assertEquals(product.getDimensions(), dto.dimensions()),
                    () -> assertEquals(product.getTags(), dto.tags()),
                    () -> assertEquals(product.getInStock(), dto.inStock()),
                    () -> assertEquals(product.getIsNew(), dto.isNew()),
                    () -> assertEquals(product.getIsBestSeller(), dto.isBestSeller()),
                    () -> assertEquals(product.getCategory(), dto.category()),
                    () -> assertEquals(product.getMaterial(), dto.material()),
                    () -> assertEquals(product.getColor(), dto.color()),
                    () -> assertEquals(product.getDescription(), dto.description()));
        }
    }

    @Nested
    @DisplayName("toEntity")
    class ToEntity {

        @Test
        void shouldMapCreateRequestToProduct() {
            ProductCreateRequestDTO request = productCreateRequestDTO1();

            Product product = ProductMapper.toEntity(request);

            assertAll(
                    () -> assertEquals(request.name(), product.getName()),
                    () -> assertEquals(request.price(), product.getPrice()),
                    () -> assertEquals(request.originalPrice(), product.getOriginalPrice()),
                    () -> assertEquals(request.image(), product.getImage()),
                    () -> assertEquals(request.hoverImage(), product.getHoverImage()),
                    () -> assertEquals(request.dimensions(), product.getDimensions()),
                    () -> assertEquals(request.tags(), product.getTags()),
                    () -> assertEquals(request.inStock(), product.getInStock()),
                    () -> assertEquals(request.isNew(), product.getIsNew()),
                    () -> assertEquals(request.isBestSeller(), product.getIsBestSeller()),
                    () -> assertEquals(request.category(), product.getCategory()),
                    () -> assertEquals(request.material(), product.getMaterial()),
                    () -> assertEquals(request.color(), product.getColor()),
                    () -> assertEquals(request.description(), product.getDescription()));
        }

        @Test
        void shouldHandleNullTags() {
            ProductCreateRequestDTO request = new ProductCreateRequestDTO(
                    "Test", 10.0, null, "img.jpg", null, null,
                    null, true, null, null, "cat", null, null, "desc");

            Product product = ProductMapper.toEntity(request);

            assertNull(product.getTags());
        }
    }
}
