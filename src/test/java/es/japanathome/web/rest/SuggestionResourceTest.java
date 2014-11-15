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
import org.joda.time.LocalDate;
import java.util.List;

import es.japanathome.Application;
import es.japanathome.domain.Suggestion;
import es.japanathome.repository.SuggestionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SuggestionResource REST controller.
 *
 * @see SuggestionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class SuggestionResourceTest {

    private static final String DEFAULT_EMAIL = "SAMPLE_TEXT";
    private static final String UPDATED_EMAIL = "UPDATED_TEXT";
    
    private static final String DEFAULT_DESCRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESCRIPTION = "UPDATED_TEXT";
    
    private static final Integer DEFAULT_STATUS = 0;
    private static final Integer UPDATED_STATUS = 1;
    
    private static final LocalDate DEFAULT_CREATED_AT = new LocalDate(0L);
    private static final LocalDate UPDATED_CREATED_AT = new LocalDate();
    

    @Inject
    private SuggestionRepository suggestionRepository;

    private MockMvc restSuggestionMockMvc;

    private Suggestion suggestion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SuggestionResource suggestionResource = new SuggestionResource();
        ReflectionTestUtils.setField(suggestionResource, "suggestionRepository", suggestionRepository);
        this.restSuggestionMockMvc = MockMvcBuilders.standaloneSetup(suggestionResource).build();
    }

    @Before
    public void initTest() {
        suggestion = new Suggestion();
        suggestion.setEmail(DEFAULT_EMAIL);
        suggestion.setDescription(DEFAULT_DESCRIPTION);
        suggestion.setStatus(DEFAULT_STATUS);
        suggestion.setCreatedAt(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createSuggestion() throws Exception {
        // Validate the database is empty
        assertThat(suggestionRepository.findAll()).hasSize(0);

        // Create the Suggestion
        restSuggestionMockMvc.perform(post("/app/rest/suggestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(suggestion)))
                .andExpect(status().isOk());

        // Validate the Suggestion in the database
        List<Suggestion> suggestions = suggestionRepository.findAll();
        assertThat(suggestions).hasSize(1);
        Suggestion testSuggestion = suggestions.iterator().next();
        assertThat(testSuggestion.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testSuggestion.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSuggestion.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testSuggestion.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllSuggestions() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get all the suggestions
        restSuggestionMockMvc.perform(get("/app/rest/suggestions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(suggestion.getId().intValue()))
                .andExpect(jsonPath("$.[0].email").value(DEFAULT_EMAIL.toString()))
                .andExpect(jsonPath("$.[0].description").value(DEFAULT_DESCRIPTION.toString()))
                .andExpect(jsonPath("$.[0].status").value(DEFAULT_STATUS))
                .andExpect(jsonPath("$.[0].createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getSuggestion() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get the suggestion
        restSuggestionMockMvc.perform(get("/app/rest/suggestions/{id}", suggestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(suggestion.getId().intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSuggestion() throws Exception {
        // Get the suggestion
        restSuggestionMockMvc.perform(get("/app/rest/suggestions/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSuggestion() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Update the suggestion
        suggestion.setEmail(UPDATED_EMAIL);
        suggestion.setDescription(UPDATED_DESCRIPTION);
        suggestion.setStatus(UPDATED_STATUS);
        suggestion.setCreatedAt(UPDATED_CREATED_AT);
        restSuggestionMockMvc.perform(post("/app/rest/suggestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(suggestion)))
                .andExpect(status().isOk());

        // Validate the Suggestion in the database
        List<Suggestion> suggestions = suggestionRepository.findAll();
        assertThat(suggestions).hasSize(1);
        Suggestion testSuggestion = suggestions.iterator().next();
        assertThat(testSuggestion.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testSuggestion.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSuggestion.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testSuggestion.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);;
    }

    @Test
    @Transactional
    public void deleteSuggestion() throws Exception {
        // Initialize the database
        suggestionRepository.saveAndFlush(suggestion);

        // Get the suggestion
        restSuggestionMockMvc.perform(delete("/app/rest/suggestions/{id}", suggestion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Suggestion> suggestions = suggestionRepository.findAll();
        assertThat(suggestions).hasSize(0);
    }
}
