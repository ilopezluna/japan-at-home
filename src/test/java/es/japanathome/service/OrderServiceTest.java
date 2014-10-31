package es.japanathome.service;

import es.japanathome.Application;
import es.japanathome.domain.*;
import es.japanathome.repository.ProductRepository;
import es.japanathome.repository.RestaurantRepository;
import es.japanathome.repository.TagRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static es.japanathome.Constants.*;
import static es.japanathome.Constants.DEFAULT_ADDRESS;
import static es.japanathome.Constants.ITEM_DEFAULT_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
public class OrderServiceTest
{
    @Inject
    private OrderService orderService;

    @Inject
    private RestaurantRepository restaurantRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private TagRepository tagRepository;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Restaurant restaurant;

    private Product product;

    @Before
    public void setup() {

        restaurant = buildRestaurant();
        restaurant = restaurantRepository.save(restaurant);

        Tag tag = buildTag();
        tag = tagRepository.save(tag);

        product = buildProduct(restaurant, tag);
        productRepository.save(product);
    }

    @Test
    public void testCreateOrder() {

        Item item = new Item();
        item.setProduct( product );
        item.setQuantity( ITEM_DEFAULT_QUANTITY );

        Map<Long, Item> items = new HashMap<>();
        items.put(product.getId(), item);

        Order order = new Order();
        order.setRestaurant(restaurant);
        order.setItems(items);
        order = orderService.create(order);

        Map<Long, Item> itemsOfOrder = order.getItems();
        BigDecimal orderPrice = orderService.getPrice( order );

        assertThat(itemsOfOrder).isNotEmpty();
        assertThat(itemsOfOrder.size()).isEqualTo(1);
        assertThat(orderPrice).isEqualTo(new BigDecimal("1.1"));
    }

    @Test
    public void testValidateOrder() {

        Order order = new Order();
        exception.expect(ValidationException.class);
        orderService.validate(order);

        Item item = new Item();
        item.setProduct(product);
        item.setQuantity( -1 );
        Map<Long, Item> items = new HashMap<>();
        items.put(product.getId(), item);

        order.setItems(items);
        exception.expect(ValidationException.class);
        orderService.validate(order);

        item.setQuantity(ITEM_DEFAULT_QUANTITY);
        exception.expect(ValidationException.class);
        orderService.validate(order);

        order.setRestaurant(restaurant);
        exception.expect(ValidationException.class);
        orderService.validate(order);

        order.setAddress(DEFAULT_ADDRESS);
        exception.expect(ValidationException.class);
        orderService.validate(order);

        order.setPaymentType(Order.PaymentType.CASH);
        orderService.validate(order);
    }

}