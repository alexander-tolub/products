package ua.shop.products.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.shop.products.dao.Product;
import ua.shop.products.dto.ProductDto;
import ua.shop.products.service.ProductService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
@Slf4j
public class Shop {

    @Autowired
    ProductService productService;

    @Autowired
    ConversionService conversionService;



    @GetMapping(value = "/shop/productsbykeyset")
    public void getProductsByKeyset(@RequestParam(value = "nameFilter") String nameFilter,
                            HttpServletResponse response) {
        response.addHeader("Content-Type", "application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        Pattern pattern = Pattern.compile(nameFilter);
        try {
            Iterator<List<Product>> products = productService.findAllProductsByKeySet();
            PrintWriter out = response.getWriter();
            out.write("{ \"productDtos\": [");
            boolean match = false;
            while (products.hasNext()) {
                for (Product value : products.next()) {
                    // Get current size of heap in bytes
                    long heapSize = Runtime.getRuntime().totalMemory();
                    // Get maximum size of heap in bytes. The heap cannot grow beyond this size.
                    // Any attempt will result in an OutOfMemoryException.
                    long heapMaxSize = Runtime.getRuntime().maxMemory();

                    // Get amount of free memory within the heap in bytes. This size will increase
                    // after garbage collection and decrease as new objects are created.
                    long heapFreeSize = Runtime.getRuntime().freeMemory();
                    log.info("[heapSize: {}], [heapMaxSize: {}], [heapFreeSize: {}]", heapSize, heapMaxSize, heapFreeSize);
                    ProductDto dto = null;
                    if (!pattern.matcher(value.getName()).matches()) {
                        if (match)
                            out.write(",");
                        match = true;
                        dto = conversionService.convert(value, ProductDto.class);
                    }
                    if (dto != null) {
                        try {
                            out.write(mapper.writeValueAsString(dto));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                }
                out.flush();
            }
            out.write("]}");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
