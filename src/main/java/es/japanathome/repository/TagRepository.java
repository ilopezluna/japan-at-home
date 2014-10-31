package es.japanathome.repository;

import es.japanathome.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Tag entity.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT distinct t FROM Tag t, Restaurant r, Product p WHERE p.restaurant.id = :restaurantId AND p.tag.id = t.id")
    List<Tag> findByRestaurantId( @Param("restaurantId") Long restaurantId );
}
