package es.japanathome.repository;

import es.japanathome.domain.Zip;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Zip entity.
 */
public interface ZipRepository extends JpaRepository<Zip, Long>
{
    Zip findByCode(String code);
}
