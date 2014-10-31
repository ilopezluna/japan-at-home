package es.japanathome.repository;

import es.japanathome.domain.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Suggestion entity.
 */
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {

}
