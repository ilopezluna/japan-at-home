package es.japanathome.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.japanathome.domain.Suggestion;
import es.japanathome.repository.SuggestionRepository;
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
 * REST controller for managing Suggestion.
 */
@RestController
@RequestMapping("/app")
public class SuggestionResource {

    private final Logger log = LoggerFactory.getLogger(SuggestionResource.class);

    @Inject
    private SuggestionRepository suggestionRepository;

    /**
     * POST  /rest/suggestions -> Create a new suggestion.
     */
    @RequestMapping(value = "/rest/suggestions",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Suggestion suggestion) {
        log.debug("REST request to save Suggestion : {}", suggestion);
        suggestionRepository.save(suggestion);
    }

    /**
     * GET  /rest/suggestions -> get all the suggestions.
     */
    @RequestMapping(value = "/rest/suggestions",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Suggestion> getAll() {
        log.debug("REST request to get all Suggestions");
        return suggestionRepository.findAll();
    }

    /**
     * GET  /rest/suggestions/:id -> get the "id" suggestion.
     */
    @RequestMapping(value = "/rest/suggestions/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Suggestion> get(@PathVariable Long id) {
        log.debug("REST request to get Suggestion : {}", id);
        return Optional.ofNullable(suggestionRepository.findOne(id))
            .map(suggestion -> new ResponseEntity<>(
                suggestion,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/suggestions/:id -> delete the "id" suggestion.
     */
    @RequestMapping(value = "/rest/suggestions/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Suggestion : {}", id);
        suggestionRepository.delete(id);
    }
}
