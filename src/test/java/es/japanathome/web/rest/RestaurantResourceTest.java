package es.japanathome.web.rest;

import es.japanathome.Application;
import es.japanathome.domain.Restaurant;
import es.japanathome.repository.RestaurantRepository;
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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RestaurantResource REST controller.
 *
 * @see RestaurantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class RestaurantResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    
    private static final String DEFAULT_SHORT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_SHORT_NAME = "UPDATED_TEXT";
    
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    
    private static final String DEFAULT_ADDRESS = "SAMPLE_TEXT";
    private static final String UPDATED_ADDRESS = "UPDATED_TEXT";
    
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_PRICE = BigDecimal.ONE;
    
    private static final Integer DEFAULT_STATUS = 0;
    private static final Integer UPDATED_STATUS = 1;
    
    private static final String DEFAULT_TELEPHONE = "SAMPLE_TEXT";
    private static final String UPDATED_TELEPHONE = "UPDATED_TEXT";
    
    private static final String DEFAULT_CLOSE_AT = "SAMPLE_TEXT";
    private static final String UPDATED_CLOSE_AT = "UPDATED_TEXT";
    
    private static final String DEFAULT_OPEN_AT = "SAMPLE_TEXT";
    private static final String UPDATED_OPEN_AT = "UPDATED_TEXT";
    
    private static final String DEFAULT_CLOSED_AT_DAY = "SAMPLE_TEXT";
    private static final String UPDATED_CLOSED_AT_DAY = "UPDATED_TEXT";
    
    private static final String DEFAULT_AVERAGE_DELIVERY_TIME = "SAMPLE_TEXT";
    private static final String UPDATED_AVERAGE_DELIVERY_TIME = "UPDATED_TEXT";
    
    private static final String DEFAULT_LOGO = "SAMPLE_TEXT";
    private static final String UPDATED_LOGO = "UPDATED_TEXT";
    

    @Inject
    private RestaurantRepository restaurantRepository;

    private MockMvc restRestaurantMockMvc;

    private Restaurant restaurant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RestaurantResource restaurantResource = new RestaurantResource();
        ReflectionTestUtils.setField(restaurantResource, "restaurantRepository", restaurantRepository);
        this.restRestaurantMockMvc = MockMvcBuilders.standaloneSetup(restaurantResource).build();
    }

    @Before
    public void initTest() {
        restaurant = new Restaurant();
        restaurant.setName(DEFAULT_NAME);
        restaurant.setShortName(DEFAULT_SHORT_NAME);
        restaurant.setDescription(DEFAULT_DESCRIPTION);
        restaurant.setAddress(DEFAULT_ADDRESS);
        restaurant.setPrice(DEFAULT_PRICE);
        restaurant.setStatus(DEFAULT_STATUS);
        restaurant.setTelephone(DEFAULT_TELEPHONE);
        restaurant.setCloseAt(DEFAULT_CLOSE_AT);
        restaurant.setOpenAt(DEFAULT_OPEN_AT);
        restaurant.setClosedAtDay(DEFAULT_CLOSED_AT_DAY);
        restaurant.setAverageDeliveryTime(DEFAULT_AVERAGE_DELIVERY_TIME);
        restaurant.setLogo(DEFAULT_LOGO);
    }

    @Test
    @Transactional
    public void createRestaurant() throws Exception {
        // Validate the database is empty
        assertThat(restaurantRepository.findAll()).hasSize(0);

        // Create the Restaurant
        restRestaurantMockMvc.perform(post("/app/rest/restaurants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restaurant)))
                .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertThat(restaurants).hasSize(1);
        Restaurant testRestaurant = restaurants.iterator().next();
        assertThat(testRestaurant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurant.getShortName()).isEqualTo(DEFAULT_SHORT_NAME);
        assertThat(testRestaurant.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRestaurant.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testRestaurant.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRestaurant.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testRestaurant.getTelephone()).isEqualTo(DEFAULT_TELEPHONE);
        assertThat(testRestaurant.getCloseAt()).isEqualTo(DEFAULT_CLOSE_AT);
        assertThat(testRestaurant.getOpenAt()).isEqualTo(DEFAULT_OPEN_AT);
        assertThat(testRestaurant.getClosedAtDay()).isEqualTo(DEFAULT_CLOSED_AT_DAY);
        assertThat(testRestaurant.getAverageDeliveryTime()).isEqualTo(DEFAULT_AVERAGE_DELIVERY_TIME);
        assertThat(testRestaurant.getLogo()).isEqualTo(DEFAULT_LOGO);
    }

    @Test
    @Transactional
    public void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurants
        restRestaurantMockMvc.perform(get("/app/rest/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(restaurant.getId().intValue()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.[0].shortName").value(DEFAULT_SHORT_NAME.toString()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.[0].address").value(DEFAULT_ADDRESS.toString()))
                .andExpect(jsonPath("$.[0].price").value(DEFAULT_PRICE.intValue()))
                .andExpect(jsonPath("$.[0].status").value(DEFAULT_STATUS))
                .andExpect(jsonPath("$.[0].telephone").value(DEFAULT_TELEPHONE.toString()))
                .andExpect(jsonPath("$.[0].closeAt").value(DEFAULT_CLOSE_AT.toString()))
                .andExpect(jsonPath("$.[0].openAt").value(DEFAULT_OPEN_AT.toString()))
                .andExpect(jsonPath("$.[0].closedAtDay").value(DEFAULT_CLOSED_AT_DAY.toString()))
                .andExpect(jsonPath("$.[0].averageDeliveryTime").value(DEFAULT_AVERAGE_DELIVERY_TIME.toString()))
                .andExpect(jsonPath("$.[0].logo").value(DEFAULT_LOGO.toString()));
    }

    @Test
    @Transactional
    public void getRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get the restaurant
        restRestaurantMockMvc.perform(get("/app/rest/restaurants/{id}", restaurant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(restaurant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.shortName").value(DEFAULT_SHORT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE.toString()))
            .andExpect(jsonPath("$.closeAt").value(DEFAULT_CLOSE_AT.toString()))
            .andExpect(jsonPath("$.openAt").value(DEFAULT_OPEN_AT.toString()))
            .andExpect(jsonPath("$.closedAtDay").value(DEFAULT_CLOSED_AT_DAY.toString()))
            .andExpect(jsonPath("$.averageDeliveryTime").value(DEFAULT_AVERAGE_DELIVERY_TIME.toString()))
            .andExpect(jsonPath("$.logo").value(DEFAULT_LOGO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRestaurant() throws Exception {
        // Get the restaurant
        restRestaurantMockMvc.perform(get("/app/rest/restaurants/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Update the restaurant
        restaurant.setName(UPDATED_NAME);
        restaurant.setShortName(UPDATED_SHORT_NAME);
        restaurant.setDescription(UPDATED_DESCRIPTION);
        restaurant.setAddress(UPDATED_ADDRESS);
        restaurant.setPrice(UPDATED_PRICE);
        restaurant.setStatus(UPDATED_STATUS);
        restaurant.setTelephone(UPDATED_TELEPHONE);
        restaurant.setCloseAt(UPDATED_CLOSE_AT);
        restaurant.setOpenAt(UPDATED_OPEN_AT);
        restaurant.setClosedAtDay(UPDATED_CLOSED_AT_DAY);
        restaurant.setAverageDeliveryTime(UPDATED_AVERAGE_DELIVERY_TIME);
        restaurant.setLogo(UPDATED_LOGO);
        restRestaurantMockMvc.perform(post("/app/rest/restaurants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(restaurant)))
                .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertThat(restaurants).hasSize(1);
        Restaurant testRestaurant = restaurants.iterator().next();
        assertThat(testRestaurant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurant.getShortName()).isEqualTo(UPDATED_SHORT_NAME);
        assertThat(testRestaurant.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRestaurant.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testRestaurant.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRestaurant.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testRestaurant.getTelephone()).isEqualTo(UPDATED_TELEPHONE);
        assertThat(testRestaurant.getCloseAt()).isEqualTo(UPDATED_CLOSE_AT);
        assertThat(testRestaurant.getOpenAt()).isEqualTo(UPDATED_OPEN_AT);
        assertThat(testRestaurant.getClosedAtDay()).isEqualTo(UPDATED_CLOSED_AT_DAY);
        assertThat(testRestaurant.getAverageDeliveryTime()).isEqualTo(UPDATED_AVERAGE_DELIVERY_TIME);
        assertThat(testRestaurant.getLogo()).isEqualTo(UPDATED_LOGO);;
    }

    @Test
    @Transactional
    public void deleteRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get the restaurant
        restRestaurantMockMvc.perform(delete("/app/rest/restaurants/{id}", restaurant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Restaurant> restaurants = restaurantRepository.findAll();
        assertThat(restaurants).hasSize(0);
    }
}
