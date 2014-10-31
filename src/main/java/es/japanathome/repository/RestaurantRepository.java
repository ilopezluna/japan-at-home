package es.japanathome.repository;

import es.japanathome.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data JPA repository for the Restaurant entity.
 */
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("select restaurant from Restaurant restaurant left join fetch restaurant.zips where restaurant.id = :id")
    Restaurant findOneWithEagerRelationships(@Param("id") Long id);

}
