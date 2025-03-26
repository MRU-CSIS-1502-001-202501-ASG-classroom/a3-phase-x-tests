package test.phase4plus;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import main.building.Building;
import main.building.Die;
import main.building.Material;
import main.scorers.MaterialScore;
import main.scorers.RecycledScorer;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class RecycledScorerTests {
    // We don't care about recycled die faces, so we'll use
    // this as a stand-in for any old recycled die.
    private static final Die ANY_RECYCLED_DIE = new Die(Material.RECYCLED, 3);

    private Building building;

    @BeforeEach
    public void setup() {
        building = new Building();
    }

    @Nested
    class ScoringValidCases {
        @Nested
        class EmptyBuildingTests {
            @Test
            public void new_building_has_recycled_score_0() {
                // Given an empty building

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }
        }

        @Nested
        class OneDieBuildingTests {
            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,1",
                    "2,2",
                    "3,1",
                    "3,2"
            })
            public void one_recycled_scores_2(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a single recycled die
                assertDoesNotThrow(() -> {
                    building.add(ANY_RECYCLED_DIE, space);
                });

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 2
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(2, materialScore.getScore());

            }

            @ParameterizedTest
            @CsvSource({
                    "1,1,W4",
                    "1,2,G1",
                    "2,1,S6",
                    "2,2,S2",
                    "3,1,G3",
                    "3,2,W5"
            })
            public void no_recycled_scores_0(int rowVal, int colVal, String diceText) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a single non-recycled die
                Die notRecycledDie = new Die(diceText);

                assertDoesNotThrow(() -> {
                    building.add(notRecycledDie, space);
                });

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0

                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }
        }

        @Nested
        class TwoDiceBuildingTests {
            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,1",
                    "2,2",
                    "3,1",
                    "3,2"
            })
            public void two_recycled_together_scores_5(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of 2 recycled die stacked on each other
                assertDoesNotThrow(() -> {
                    building.add(ANY_RECYCLED_DIE, space);
                    building.add(ANY_RECYCLED_DIE, space);
                });

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 5
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(5, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,1",
                    "2,2",
                    "3,1",
                    "3,2"
            })
            public void two_recycled_apart_scores_5(int rowVal, int colVal) {
                // Given a building composed of 2 separated recycled dice
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                int separateRowVal = ((rowVal + 1) % 3) + 1;
                int separateColVal = ((colVal + 1) % 2) + 1;
                Space separatedSpace = Space.from(Row.at(separateRowVal), Col.at(separateColVal));

                assertDoesNotThrow(() -> {
                    building.add(ANY_RECYCLED_DIE, space);
                    building.add(ANY_RECYCLED_DIE, separatedSpace);
                });

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 5
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(5, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,1",
                    "2,2",
                    "3,1",
                    "3,2"
            })
            public void recycled_and_other_material_stacked_together_scores_2(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a stacked recycled and other material
                assertDoesNotThrow(() -> {
                    building.add(new Die(Material.GLASS, 1), space);
                    building.add(ANY_RECYCLED_DIE, space);
                });

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 2
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(2, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,1",
                    "2,2",
                    "3,1",
                    "3,2"
            })
            public void recycled_and_other_material_apart_scores_2(int rowVal, int colVal) {
                // Given a building composed of 2 separated recycled and non-recycled dice
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                int separateRowVal = ((rowVal + 1) % 3) + 1;
                int separateColVal = ((colVal + 1) % 2) + 1;
                Space separatedSpace = Space.from(Row.at(separateRowVal), Col.at(separateColVal));

                assertDoesNotThrow(() -> {
                    building.add(ANY_RECYCLED_DIE, space);
                    building.add(new Die("W2"), separatedSpace);
                });

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 2
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(2, materialScore.getScore());
            }

        }

        @Nested
        class SixRecycledTests {
            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,1",
                    "2,2",
                    "3,1",
                    "3,2"
            })
            public void stack_of_all_recycled_scores_30(int rowVal, int colVal) {
                // Given a building composed of a stack of 6 recycled dice
                for (int count = 1; count <= 6; count++) {
                    Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                    assertDoesNotThrow(() -> {
                        building.add(ANY_RECYCLED_DIE, space);
                    });
                }

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 30
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(30, materialScore.getScore());
            }

            @Test
            public void all_separated_recycled_scores_30() {
                // Given a building composed of 6 separated recycled dice
                assertDoesNotThrow(() -> {
                    building.add(ANY_RECYCLED_DIE, Space.from(Row.at(1), Col.at(1)));
                    building.add(ANY_RECYCLED_DIE, Space.from(Row.at(1), Col.at(2)));
                    building.add(ANY_RECYCLED_DIE, Space.from(Row.at(2), Col.at(1)));
                    building.add(ANY_RECYCLED_DIE, Space.from(Row.at(2), Col.at(2)));
                    building.add(ANY_RECYCLED_DIE, Space.from(Row.at(3), Col.at(1)));
                    building.add(ANY_RECYCLED_DIE, Space.from(Row.at(3), Col.at(2)));
                });

                // When you get the recycled score for that building
                RecycledScorer scorer = new RecycledScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 30
                assertEquals(Material.RECYCLED, materialScore.getMaterial());
                assertEquals(30, materialScore.getScore());
            }
        }

    }

}
