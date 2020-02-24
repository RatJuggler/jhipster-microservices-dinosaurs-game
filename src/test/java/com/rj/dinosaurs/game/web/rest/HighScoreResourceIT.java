package com.rj.dinosaurs.game.web.rest;

import com.rj.dinosaurs.game.GameApp;
import com.rj.dinosaurs.game.domain.HighScore;
import com.rj.dinosaurs.game.domain.Player;
import com.rj.dinosaurs.game.domain.Level;
import com.rj.dinosaurs.game.repository.HighScoreRepository;
import com.rj.dinosaurs.game.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.rj.dinosaurs.game.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HighScoreResource} REST controller.
 */
@SpringBootTest(classes = GameApp.class)
public class HighScoreResourceIT {

    private static final Integer DEFAULT_SCORE = 1;
    private static final Integer UPDATED_SCORE = 2;

    private static final Instant DEFAULT_ACHIEVED_DT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACHIEVED_DT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private HighScoreRepository highScoreRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restHighScoreMockMvc;

    private HighScore highScore;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HighScoreResource highScoreResource = new HighScoreResource(highScoreRepository);
        this.restHighScoreMockMvc = MockMvcBuilders.standaloneSetup(highScoreResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HighScore createEntity(EntityManager em) {
        HighScore highScore = new HighScore()
            .score(DEFAULT_SCORE)
            .achievedDt(DEFAULT_ACHIEVED_DT);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        highScore.setPlayer(player);
        // Add required entity
        Level level;
        if (TestUtil.findAll(em, Level.class).isEmpty()) {
            level = LevelResourceIT.createEntity(em);
            em.persist(level);
            em.flush();
        } else {
            level = TestUtil.findAll(em, Level.class).get(0);
        }
        highScore.setLevel(level);
        return highScore;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HighScore createUpdatedEntity(EntityManager em) {
        HighScore highScore = new HighScore()
            .score(UPDATED_SCORE)
            .achievedDt(UPDATED_ACHIEVED_DT);
        // Add required entity
        Player player;
        if (TestUtil.findAll(em, Player.class).isEmpty()) {
            player = PlayerResourceIT.createUpdatedEntity(em);
            em.persist(player);
            em.flush();
        } else {
            player = TestUtil.findAll(em, Player.class).get(0);
        }
        highScore.setPlayer(player);
        // Add required entity
        Level level;
        if (TestUtil.findAll(em, Level.class).isEmpty()) {
            level = LevelResourceIT.createUpdatedEntity(em);
            em.persist(level);
            em.flush();
        } else {
            level = TestUtil.findAll(em, Level.class).get(0);
        }
        highScore.setLevel(level);
        return highScore;
    }

    @BeforeEach
    public void initTest() {
        highScore = createEntity(em);
    }

    @Test
    @Transactional
    public void createHighScore() throws Exception {
        int databaseSizeBeforeCreate = highScoreRepository.findAll().size();

        // Create the HighScore
        restHighScoreMockMvc.perform(post("/api/high-scores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(highScore)))
            .andExpect(status().isCreated());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeCreate + 1);
        HighScore testHighScore = highScoreList.get(highScoreList.size() - 1);
        assertThat(testHighScore.getScore()).isEqualTo(DEFAULT_SCORE);
        assertThat(testHighScore.getAchievedDt()).isEqualTo(DEFAULT_ACHIEVED_DT);
    }

    @Test
    @Transactional
    public void createHighScoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = highScoreRepository.findAll().size();

        // Create the HighScore with an existing ID
        highScore.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHighScoreMockMvc.perform(post("/api/high-scores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(highScore)))
            .andExpect(status().isBadRequest());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkScoreIsRequired() throws Exception {
        int databaseSizeBeforeTest = highScoreRepository.findAll().size();
        // set the field null
        highScore.setScore(null);

        // Create the HighScore, which fails.

        restHighScoreMockMvc.perform(post("/api/high-scores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(highScore)))
            .andExpect(status().isBadRequest());

        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAchievedDtIsRequired() throws Exception {
        int databaseSizeBeforeTest = highScoreRepository.findAll().size();
        // set the field null
        highScore.setAchievedDt(null);

        // Create the HighScore, which fails.

        restHighScoreMockMvc.perform(post("/api/high-scores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(highScore)))
            .andExpect(status().isBadRequest());

        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHighScores() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        // Get all the highScoreList
        restHighScoreMockMvc.perform(get("/api/high-scores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(highScore.getId().intValue())))
            .andExpect(jsonPath("$.[*].score").value(hasItem(DEFAULT_SCORE)))
            .andExpect(jsonPath("$.[*].achievedDt").value(hasItem(DEFAULT_ACHIEVED_DT.toString())));
    }
    
    @Test
    @Transactional
    public void getHighScore() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        // Get the highScore
        restHighScoreMockMvc.perform(get("/api/high-scores/{id}", highScore.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(highScore.getId().intValue()))
            .andExpect(jsonPath("$.score").value(DEFAULT_SCORE))
            .andExpect(jsonPath("$.achievedDt").value(DEFAULT_ACHIEVED_DT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHighScore() throws Exception {
        // Get the highScore
        restHighScoreMockMvc.perform(get("/api/high-scores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHighScore() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();

        // Update the highScore
        HighScore updatedHighScore = highScoreRepository.findById(highScore.getId()).get();
        // Disconnect from session so that the updates on updatedHighScore are not directly saved in db
        em.detach(updatedHighScore);
        updatedHighScore
            .score(UPDATED_SCORE)
            .achievedDt(UPDATED_ACHIEVED_DT);

        restHighScoreMockMvc.perform(put("/api/high-scores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedHighScore)))
            .andExpect(status().isOk());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
        HighScore testHighScore = highScoreList.get(highScoreList.size() - 1);
        assertThat(testHighScore.getScore()).isEqualTo(UPDATED_SCORE);
        assertThat(testHighScore.getAchievedDt()).isEqualTo(UPDATED_ACHIEVED_DT);
    }

    @Test
    @Transactional
    public void updateNonExistingHighScore() throws Exception {
        int databaseSizeBeforeUpdate = highScoreRepository.findAll().size();

        // Create the HighScore

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHighScoreMockMvc.perform(put("/api/high-scores")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(highScore)))
            .andExpect(status().isBadRequest());

        // Validate the HighScore in the database
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHighScore() throws Exception {
        // Initialize the database
        highScoreRepository.saveAndFlush(highScore);

        int databaseSizeBeforeDelete = highScoreRepository.findAll().size();

        // Delete the highScore
        restHighScoreMockMvc.perform(delete("/api/high-scores/{id}", highScore.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<HighScore> highScoreList = highScoreRepository.findAll();
        assertThat(highScoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
