package es.japanathome.web.rest;

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

import es.japanathome.Application;
import es.japanathome.domain.Product;
import es.japanathome.repository.ProductRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProductResource REST controller.
 *
 * @see ProductResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ProductResourceTest {

    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";
    
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    
    private static final BigDecimal DEFAULT_PRICE = BigDecimal.ZERO;
    private static final BigDecimal UPDATED_PRICE = BigDecimal.ONE;
    
    private static final Integer DEFAULT_STATUS = 0;
    private static final Integer UPDATED_STATUS = 1;
    

   @Inject
   private ProductRepository productRepository;

   private MockMvc restProductMockMvc;

   private Product product;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductResource productResource = new ProductResource();
        ReflectionTestUtils.setField(productResource, "productRepository", productRepository);
        this.restProductMockMvc = MockMvcBuilders.standaloneSetup(productResource).build();
    }

    @Before
    public void initTest() {
        product = new Product();
        product.setName(DEFAULT_NAME);
        product.setDescription(DEFAULT_DESCRIPTION);
        product.setPrice(DEFAULT_PRICE);
        product.setStatus(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        // Validate the database is empty
        assertThat(productRepository.findAll()).hasSize(0);

        // Create the Product
        restProductMockMvc.perform(post("/app/rest/products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(product)))
                .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        Product testProduct = products.iterator().next();
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getStatus()).isEqualTo(DEFAULT_STATUS);;
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the products
        restProductMockMvc.perform(get("/app/rest/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(product.getId().intValue()))
                .andExpect(jsonPath("$.[0].name").value(DEFAULT_NAME.toString()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.[0].price").value(DEFAULT_PRICE.intValue()))
                .andExpect(jsonPath("$.[0].status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/app/rest/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/app/rest/products/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Update the product
        product.setName(UPDATED_NAME);
        product.setDescription(UPDATED_DESCRIPTION);
        product.setPrice(UPDATED_PRICE);
        product.setStatus(UPDATED_STATUS);
        restProductMockMvc.perform(post("/app/rest/products")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(product)))
                .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1);
        Product testProduct = products.iterator().next();
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getStatus()).isEqualTo(UPDATED_STATUS);;
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(delete("/app/rest/products/{id}", product.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(0);
    }
}
