package es.japanathome.repository;

import es.japanathome.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Product entity.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

}
