package ua.shop.products.converters;

import org.springframework.core.convert.converter.Converter;
import ua.shop.products.dao.Product;
import ua.shop.products.dto.ProductDto;

public class ProductToProductDto implements Converter<Product, ProductDto> {
    @Override
    public ProductDto convert(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .build();
    }
}
