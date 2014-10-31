package es.japanathome.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.japanathome.domain.Zip;
import es.japanathome.repository.ZipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Zip.
 */
@RestController
@RequestMapping("/app")
public class ZipResource {

    private final Logger log = LoggerFactory.getLogger(ZipResource.class);

    @Inject
    private ZipRepository zipRepository;

    /**
     * POST  /rest/zips -> Create a new zip.
     */
    @RequestMapping(value = "/rest/zips",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Zip zip) {
        log.debug("REST request to save Zip : {}", zip);
        zipRepository.save(zip);
    }

    /**
     * GET  /rest/zips -> get all the zips.
     */
    @RequestMapping(value = "/rest/zips",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Zip> getAll() {
        log.debug("REST request to get all Zips");
        return zipRepository.findAll();
    }

    /**
     * GET  /rest/zips/:id -> get the "id" zip.
     */
    @RequestMapping(value = "/rest/zips/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Zip> get(@PathVariable Long id) {
        log.debug("REST request to get Zip : {}", id);
        return Optional.ofNullable(zipRepository.findOne(id))
            .map(zip -> new ResponseEntity<>(
                zip,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/zips/:id -> delete the "id" zip.
     */
    @RequestMapping(value = "/rest/zips/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Zip : {}", id);
        zipRepository.delete(id);
    }
}
