package ua.shop.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.shop.products.dao.Product;

import javax.persistence.QueryHint;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    String FETCH_OFFSET = "4000";

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = FETCH_OFFSET))
    @Query(value = "select p from Product p")
    Stream<Product> streamAll();
}
