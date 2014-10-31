package es.japanathome.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.japanathome.domain.Item;
import es.japanathome.repository.ItemRepository;
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
 * REST controller for managing Item.
 */
@RestController
@RequestMapping("/app")
public class ItemResource {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);

    @Inject
    private ItemRepository itemRepository;

    /**
     * POST  /rest/items -> Create a new item.
     */
    @RequestMapping(value = "/rest/items",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Item item) {
        log.debug("REST request to save Item : {}", item);
        itemRepository.save(item);
    }

    /**
     * GET  /rest/items -> get all the items.
     */
    @RequestMapping(value = "/rest/items",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Item> getAll() {
        log.debug("REST request to get all Items");
        return itemRepository.findAll();
    }

    /**
     * GET  /rest/items/:id -> get the "id" item.
     */
    @RequestMapping(value = "/rest/items/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> get(@PathVariable Long id) {
        log.debug("REST request to get Item : {}", id);
        return Optional.ofNullable(itemRepository.findOne(id))
            .map(item -> new ResponseEntity<>(
                item,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/items/:id -> delete the "id" item.
     */
    @RequestMapping(value = "/rest/items/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Item : {}", id);
        itemRepository.delete(id);
    }
}
