package es.japanathome.web.rest;

import es.japanathome.Application;
import es.japanathome.domain.*;
import es.japanathome.repository.*;
import es.japanathome.service.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static es.japanathome.Constants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the OrderResource REST controller.
 *
 * @see OrderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class OrderResourceTest {

    private static final String DEFAULT_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS = "UPDATED_TEXT";
    
    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    
    private static final Order.PaymentType DEFAULT_PAYMENT_TYPE = Order.PaymentType.ONLINE;
    private static final Order.PaymentType UPDATED_PAYMENT_TYPE = Order.PaymentType.ONLINE;
    
    private static final Order.Status DEFAULT_STATUS = Order.Status.CREATED;
    private static final Order.Status UPDATED_STATUS = Order.Status.DELIVERED;
    

   @Inject
   private OrderService orderService;

    @Inject
    private RestaurantRepository restaurantRepository;

    @Inject
    private ZipRepository zipRepository;

    @Inject
    private TagRepository tagRepository;

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ItemRepository itemRepository;

   private MockMvc restOrderMockMvc;

   private Order order;
    private Product product;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderResource orderResource = new OrderResource();
        ReflectionTestUtils.setField(orderResource, "orderService", orderService);
        this.restOrderMockMvc = MockMvcBuilders.standaloneSetup(orderResource).build();
    }

    @Before
    public void initTest() {

        Zip zip = buildZip();
        zipRepository.save(zip);

        HashSet<Zip> zips = new HashSet<Zip>();
        zips.add(zip);

        Restaurant restaurant = buildRestaurant();
        restaurant.setZips(zips);
        restaurantRepository.save(restaurant);

        Tag tag = buildTag();
        tag = tagRepository.save(tag);

        product = buildProduct(restaurant, tag);
        product = productRepository.save(product);

        Item item = buildItem(product);
        item = itemRepository.save(item);

        Map<Long, Item> items = new HashMap<>();
        items.put(product.getId(), item);

        order = new Order();
        order.setRestaurant(restaurant);
        order.setAddress(DEFAULT_ADDRESS);
        order.setCode(DEFAULT_CODE);
        order.setPaymentType(DEFAULT_PAYMENT_TYPE);
        order.setStatus(DEFAULT_STATUS);
        order.setItems(items);
        order.setZip(zip);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        Item item = new Item();
        item.setProduct( product );
        item.setQuantity( ITEM_DEFAULT_QUANTITY );

        Map<Long, Item> items = new HashMap<>();
        items.put(product.getId(), item);
        order.setItems(items);
        // Validate the database is empty
        assertThat(orderService.findAll()).hasSize(0);

        // Create the Order
        restOrderMockMvc.perform(post("/app/rest/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orders = orderService.findAll();
        assertThat(orders).hasSize(1);
        Order testOrder = orders.iterator().next();
        assertThat(testOrder.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrder.getPaymentType().name()).isEqualTo(DEFAULT_PAYMENT_TYPE.name());
        assertThat(testOrder.getStatus().name()).isEqualTo(DEFAULT_STATUS.name());
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderService.saveAndFlush(order);

        // Get all the orders
        restOrderMockMvc.perform(get("/app/rest/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(order.getId().intValue()))
                .andExpect(jsonPath("$.[0].address").value(DEFAULT_ADDRESS))
                .andExpect(jsonPath("$.[0].code").value(DEFAULT_CODE))
                .andExpect(jsonPath("$.[0].paymentType").value(DEFAULT_PAYMENT_TYPE.name()))
                .andExpect(jsonPath("$.[0].status").value(DEFAULT_STATUS.name()));
    }

    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderService.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/app/rest/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.paymentType").value(DEFAULT_PAYMENT_TYPE.name()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.name()));
    }

    @Test
    @Transactional
    public void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get("/app/rest/orders/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOrder() throws Exception {
        // Initialize the database
        orderService.saveAndFlush(order);

        // Update the order
        order.setAddress(UPDATED_ADDRESS);
        order.setCode(UPDATED_CODE);
        order.setPaymentType(UPDATED_PAYMENT_TYPE);
        order.setStatus(UPDATED_STATUS);
        restOrderMockMvc.perform(post("/app/rest/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orders = orderService.findAll();
        assertThat(orders).hasSize(1);
        Order testOrder = orders.iterator().next();
        assertThat(testOrder.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrder.getPaymentType().name()).isEqualTo(UPDATED_PAYMENT_TYPE.name());
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderService.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(delete("/app/rest/orders/{id}", order.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Order> orders = orderService.findAll();
        assertThat(orders).hasSize(0);
    }
}
