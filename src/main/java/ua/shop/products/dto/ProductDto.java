package ua.shop.products.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private String description;

}
