package es.japanathome.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.japanathome.domain.Order;
import es.japanathome.repository.OrderRepository;
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
 * REST controller for managing Order.
 */
@RestController
@RequestMapping("/app")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    private OrderRepository orderRepository;

    /**
     * POST  /rest/orders -> Create a new order.
     */
    @RequestMapping(value = "/rest/orders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Order order) {
        log.debug("REST request to save Order : {}", order);
        orderRepository.save(order);
    }

    /**
     * GET  /rest/orders -> get all the orders.
     */
    @RequestMapping(value = "/rest/orders",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Order> getAll() {
        log.debug("REST request to get all Orders");
        return orderRepository.findAll();
    }

    /**
     * GET  /rest/orders/:id -> get the "id" order.
     */
    @RequestMapping(value = "/rest/orders/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Order> get(@PathVariable Long id) {
        log.debug("REST request to get Order : {}", id);
        return Optional.ofNullable(orderRepository.findOne(id))
            .map(order -> new ResponseEntity<>(
                order,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/orders/:id -> delete the "id" order.
     */
    @RequestMapping(value = "/rest/orders/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Order : {}", id);
        orderRepository.delete(id);
    }
}
