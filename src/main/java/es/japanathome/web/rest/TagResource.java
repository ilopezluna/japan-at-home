package es.japanathome.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.japanathome.domain.Tag;
import es.japanathome.repository.TagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tag.
 */
@RestController
@RequestMapping("/app")
public class TagResource {

    private final Logger log = LoggerFactory.getLogger(TagResource.class);

    @Inject
    private TagRepository tagRepository;

    /**
     * POST  /rest/tags -> Create a new tag.
     */
    @RequestMapping(value = "/rest/tags",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Tag tag) {
        log.debug("REST request to save Tag : {}", tag);
        tagRepository.save(tag);
    }

    /**
     * GET  /rest/tags -> get all the tags.
     */
    @RequestMapping(value = "/rest/tags",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tag> getAll(final WebRequest webRequest) {
        log.debug("REST request to get all Tags");
        String restaurantId = webRequest.getParameter("restaurantId");
        if (restaurantId == null)
        {
            return tagRepository.findAll();
        }
        else
        {
            Long id = Long.valueOf(restaurantId);
            List<Tag> tagsOfRestaurant = tagRepository.findByRestaurantId(id);
            Collections.sort(tagsOfRestaurant);
            return tagsOfRestaurant;
        }
    }

    /**
     * GET  /rest/tags/:id -> get the "id" tag.
     */
    @RequestMapping(value = "/rest/tags/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tag> get(@PathVariable Long id) {
        log.debug("REST request to get Tag : {}", id);
        return Optional.ofNullable(tagRepository.findOne(id))
            .map(tag -> new ResponseEntity<Tag>(
                tag,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/tags/:id -> delete the "id" tag.
     */
    @RequestMapping(value = "/rest/tags/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Tag : {}", id);
        tagRepository.delete(id);
    }
}
