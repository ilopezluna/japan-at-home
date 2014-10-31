package es.japanathome.repository;

import es.japanathome.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Order entity.
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

}
