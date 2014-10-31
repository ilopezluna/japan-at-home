package es.japanathome.web.rest;

import es.japanathome.Application;
import es.japanathome.domain.Order;
import es.japanathome.repository.OrderRepository;
import org.joda.time.LocalDate;
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
import java.util.List;

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

    private static final LocalDate DEFAULT_CREATED_ON = new LocalDate(0L);
    private static final LocalDate UPDATED_CREATED_ON = new LocalDate();
    
    private static final String DEFAULT_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS = "UPDATED_TEXT";
    
    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    
    private static final Order.PaymentType DEFAULT_PAYMENT_TYPE = Order.PaymentType.CASH;
    private static final Order.PaymentType UPDATED_PAYMENT_TYPE = Order.PaymentType.ONLINE;
    
    private static final Order.Status DEFAULT_STATUS = Order.Status.CREATED;
    private static final Order.Status UPDATED_STATUS = Order.Status.DELIVERED;
    

   @Inject
   private OrderRepository orderRepository;

   private MockMvc restOrderMockMvc;

   private Order order;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OrderResource orderResource = new OrderResource();
        ReflectionTestUtils.setField(orderResource, "orderRepository", orderRepository);
        this.restOrderMockMvc = MockMvcBuilders.standaloneSetup(orderResource).build();
    }

    @Before
    public void initTest() {
        order = new Order();
        order.setCreatedOn(DEFAULT_CREATED_ON);
        order.setAddress(DEFAULT_ADDRESS);
        order.setCode(DEFAULT_CODE);
        order.setPaymentType(DEFAULT_PAYMENT_TYPE);
        order.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createOrder() throws Exception {
        // Validate the database is empty
        assertThat(orderRepository.findAll()).hasSize(0);

        // Create the Order
        restOrderMockMvc.perform(post("/app/rest/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);
        Order testOrder = orders.iterator().next();
        assertThat(testOrder.getCreatedOn()).isEqualTo(DEFAULT_CREATED_ON);
        assertThat(testOrder.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOrder.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testOrder.getPaymentType().name()).isEqualTo(DEFAULT_PAYMENT_TYPE.name());
        assertThat(testOrder.getStatus().name()).isEqualTo(DEFAULT_STATUS.name());
    }

    @Test
    @Transactional
    public void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orders
        restOrderMockMvc.perform(get("/app/rest/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(order.getId().intValue()))
                .andExpect(jsonPath("$.[0].createdOn").value(DEFAULT_CREATED_ON.toString()))
                .andExpect(jsonPath("$.[0].address").value(DEFAULT_ADDRESS))
                .andExpect(jsonPath("$.[0].code").value(DEFAULT_CODE))
                .andExpect(jsonPath("$.[0].paymentType").value(DEFAULT_PAYMENT_TYPE.name()))
                .andExpect(jsonPath("$.[0].status").value(DEFAULT_STATUS.name()));
    }

    @Test
    @Transactional
    public void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(get("/app/rest/orders/{id}", order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.createdOn").value(DEFAULT_CREATED_ON.toString()))
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
        orderRepository.saveAndFlush(order);

        // Update the order
        order.setCreatedOn(UPDATED_CREATED_ON);
        order.setAddress(UPDATED_ADDRESS);
        order.setCode(UPDATED_CODE);
        order.setPaymentType(UPDATED_PAYMENT_TYPE);
        order.setStatus(UPDATED_STATUS);
        restOrderMockMvc.perform(post("/app/rest/orders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(order)))
                .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(1);
        Order testOrder = orders.iterator().next();
        assertThat(testOrder.getCreatedOn()).isEqualTo(UPDATED_CREATED_ON);
        assertThat(testOrder.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOrder.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testOrder.getPaymentType().name()).isEqualTo(UPDATED_PAYMENT_TYPE.name());
        assertThat(testOrder.getStatus().name()).isEqualTo(UPDATED_STATUS.name());
    }

    @Test
    @Transactional
    public void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc.perform(delete("/app/rest/orders/{id}", order.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Order> orders = orderRepository.findAll();
        assertThat(orders).hasSize(0);
    }
}
