package es.japanathome.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.japanathome.domain.Restaurant;
import es.japanathome.repository.RestaurantRepository;
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
 * REST controller for managing Restaurant.
 */
@RestController
@RequestMapping("/app")
public class RestaurantResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantResource.class);

    @Inject
    private RestaurantRepository restaurantRepository;

    /**
     * POST  /rest/restaurants -> Create a new restaurant.
     */
    @RequestMapping(value = "/rest/restaurants",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Restaurant restaurant) {
        log.debug("REST request to save Restaurant : {}", restaurant);
        restaurantRepository.save(restaurant);
    }

    /**
     * GET  /rest/restaurants -> get all the restaurants.
     */
    @RequestMapping(value = "/rest/restaurants",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Restaurant> getAll() {
        log.debug("REST request to get all Restaurants");
        return restaurantRepository.findAll();
    }

    /**
     * GET  /rest/restaurants/:id -> get the "id" restaurant.
     */
    @RequestMapping(value = "/rest/restaurants/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Restaurant> get(@PathVariable Long id) {
        log.debug("REST request to get Restaurant : {}", id);
        return Optional.ofNullable(restaurantRepository.findOneWithEagerRelationships(id))
            .map(restaurant -> new ResponseEntity<>(
                restaurant,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/restaurants/:id -> delete the "id" restaurant.
     */
    @RequestMapping(value = "/rest/restaurants/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Restaurant : {}", id);
        restaurantRepository.delete(id);
    }
}
