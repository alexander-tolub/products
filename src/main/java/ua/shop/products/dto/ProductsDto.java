package ua.shop.products.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductsDto {

    private List<ProductDto> productDtos;

}
