package ua.shop.products.service;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import org.hibernate.jpa.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.shop.products.dao.Product;
import ua.shop.products.repository.ProductRepository;


import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ProductService {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CriteriaBuilderFactory criteriaBuilderFactory;

    @Transactional(readOnly = true)
    public Stream<Product> findAllProductsByStream() {
        return null;
    }

    public Iterator<List<Product>> findAllProductsByKeySet() {

        return new Iterator<List<Product>>() {

            private KeysetPage previous = null;
            private PagedList<Product> currentPage = null;
            @Override
            public boolean hasNext() {
                if(currentPage != null)
                    return !currentPage.isEmpty();
                currentPage = loadNext(previous);
                return !currentPage.isEmpty();
            }

            @Override
            public List<Product> next() {
                PagedList<Product> products = loadNext(previous);
                currentPage = products;
                previous = products.getKeysetPage();
                return products;
            }

            private PagedList<Product> loadNext(KeysetPage previous) {
                if(previous != null) {
                    long previousId = (long) previous.getHighest().getTuple()[0];
                    return criteriaBuilderFactory.create(entityManager, Product.class)
                            .orderByAsc("id")
                            .page(previous, (int) previousId, 10000)
                            .getResultList();
                } else {
                    return criteriaBuilderFactory.create(entityManager, Product.class)
                            .orderByAsc("id")
                            .page(null, 0, 10000)
                            .getResultList();
                }
            }
        };
    }

}
