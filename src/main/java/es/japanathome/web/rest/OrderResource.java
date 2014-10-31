package es.japanathome.web.rest;

import com.codahale.metrics.annotation.Timed;
import es.japanathome.domain.Order;
import es.japanathome.dto.Merchant;
import es.japanathome.security.SecurityUtils;
import es.japanathome.service.OrderService;
import es.japanathome.service.util.SermepaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static es.japanathome.domain.Order.PaymentType.CASH;
import static org.springframework.http.HttpStatus.OK;

/**
 * REST controller for managing Order.
 */
@RestController
@RequestMapping("/app")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);

    @Inject
    private OrderService orderService;

    @Inject
    private Environment env;

    private String paymentUrl;
    private String url;
    private String code;
    private String key;

    @PostConstruct
    public void init()
    {
        this.paymentUrl = env.getProperty("payment.url");
        this.url        = env.getProperty("merchant.url");
        this.code       = env.getProperty("merchant.code");
        this.key        = env.getProperty("merchant.key");
    }

    /**
     * POST  /rest/orders -> Create a new order.
     */
    @RequestMapping(value = "/rest/orders",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public Merchant create(@RequestBody Order order) {
        log.debug("REST request to save Order : {}", order);
        orderService.validate(order);

        if ( order.getPaymentType().equals( CASH ) &&  !SecurityUtils.isAuthenticated() )
        {
            log.debug("REST user must be logged");
            throw new AuthorizationServiceException("needs be logged");
        }
        orderService.create(order);
        Merchant merchant = SermepaUtils.getMerchantForRequest(url, code, key, order.getCode(), orderService.getPrice(order));
        log.debug("TPV : {}" + merchant);
        return merchant;
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
        return orderService.findAll();
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
        return Optional.ofNullable(orderService.findOne(id))
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
        orderService.delete(id);
    }

    @RequestMapping(value = "/rest/orders/payment/url",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String getMerchantUrl() {
        return paymentUrl;
    }

    @RequestMapping( value = "/rest/order", method = RequestMethod.POST )
    @ResponseStatus( value = OK )
    public void result(
            @RequestParam("Ds_Response")            String response,
            @RequestParam("Ds_SecurePayment")       String securePayment,
            @RequestParam("Ds_Signature")           String signature,
            @RequestParam("Ds_AuthorisationCode")   String authorisationCode,
            @RequestParam("Ds_Order")               String order,
            @RequestParam("Ds_MerchantCode")        String merchantCode,
            @RequestParam("Ds_Currency")            String currency,
            @RequestParam("Ds_Amount")              String amount,
            @RequestParam("Ds_Card_Country")        String cardCountry

    )
    {
        Merchant merchant = SermepaUtils.getMerchantForResponse(response, url, code, key, order, amount);
        log.info("Response: " + response);
        orderService.confirm( merchant, Integer.valueOf( response ), signature );
    }
}
