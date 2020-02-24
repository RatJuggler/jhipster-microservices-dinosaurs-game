package com.rj.dinosaurs.game.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.rj.dinosaurs.game.web.rest.TestUtil;

public class HighScoreTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HighScore.class);
        HighScore highScore1 = new HighScore();
        highScore1.setId(1L);
        HighScore highScore2 = new HighScore();
        highScore2.setId(highScore1.getId());
        assertThat(highScore1).isEqualTo(highScore2);
        highScore2.setId(2L);
        assertThat(highScore1).isNotEqualTo(highScore2);
        highScore1.setId(null);
        assertThat(highScore1).isNotEqualTo(highScore2);
    }
}
