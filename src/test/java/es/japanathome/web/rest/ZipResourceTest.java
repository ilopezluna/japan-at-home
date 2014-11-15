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
import java.util.List;

import es.japanathome.Application;
import es.japanathome.domain.Zip;
import es.japanathome.repository.ZipRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ZipResource REST controller.
 *
 * @see ZipResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ZipResourceTest {

    private static final String DEFAULT_CODE = "SAMPLE_TEXT";
    private static final String UPDATED_CODE = "UPDATED_TEXT";
    

    @Inject
    private ZipRepository zipRepository;

    private MockMvc restZipMockMvc;

    private Zip zip;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ZipResource zipResource = new ZipResource();
        ReflectionTestUtils.setField(zipResource, "zipRepository", zipRepository);
        this.restZipMockMvc = MockMvcBuilders.standaloneSetup(zipResource).build();
    }

    @Before
    public void initTest() {
        zip = new Zip();
        zip.setCode(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void createZip() throws Exception {
        // Validate the database is empty
        assertThat(zipRepository.findAll()).hasSize(0);

        // Create the Zip
        restZipMockMvc.perform(post("/app/rest/zips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(zip)))
                .andExpect(status().isOk());

        // Validate the Zip in the database
        List<Zip> zips = zipRepository.findAll();
        assertThat(zips).hasSize(1);
        Zip testZip = zips.iterator().next();
        assertThat(testZip.getCode()).isEqualTo(DEFAULT_CODE);
    }

    @Test
    @Transactional
    public void getAllZips() throws Exception {
        // Initialize the database
        zipRepository.saveAndFlush(zip);

        // Get all the zips
        restZipMockMvc.perform(get("/app/rest/zips"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(zip.getId().intValue()))
                .andExpect(jsonPath("$.[0].code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getZip() throws Exception {
        // Initialize the database
        zipRepository.saveAndFlush(zip);

        // Get the zip
        restZipMockMvc.perform(get("/app/rest/zips/{id}", zip.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(zip.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingZip() throws Exception {
        // Get the zip
        restZipMockMvc.perform(get("/app/rest/zips/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateZip() throws Exception {
        // Initialize the database
        zipRepository.saveAndFlush(zip);

        // Update the zip
        zip.setCode(UPDATED_CODE);
        restZipMockMvc.perform(post("/app/rest/zips")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(zip)))
                .andExpect(status().isOk());

        // Validate the Zip in the database
        List<Zip> zips = zipRepository.findAll();
        assertThat(zips).hasSize(1);
        Zip testZip = zips.iterator().next();
        assertThat(testZip.getCode()).isEqualTo(UPDATED_CODE);;
    }

    @Test
    @Transactional
    public void deleteZip() throws Exception {
        // Initialize the database
        zipRepository.saveAndFlush(zip);

        // Get the zip
        restZipMockMvc.perform(delete("/app/rest/zips/{id}", zip.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Zip> zips = zipRepository.findAll();
        assertThat(zips).hasSize(0);
    }
}
