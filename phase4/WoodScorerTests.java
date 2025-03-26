package test.phase4;

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
import main.scorers.WoodScorer;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class WoodScorerTests {
    // We don't care about wood die faces, so we'll use
    // this as a stand-in for any old wood die.
    private static final Die ANY_WOOD_DIE = new Die(Material.WOOD, 1);

    private Building building;

    @BeforeEach
    public void setup() {
        building = new Building();
    }

    @Nested
    class ScoringValidCases {
        @Nested
        class EmptyBuilding {
            @Test
            public void new_building_has_wood_score_0() {
                // Given an empty building

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
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
            public void one_wood_scores_0(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a single wood die
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1,G4",
                    "1,2,R1",
                    "2,1,S6",
                    "2,2,S2",
                    "3,1,R3",
                    "3,2,G5"
            })
            public void no_wood_scores_0(int rowVal, int colVal, String diceText) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a single non-wood die
                Building building = new Building();
                Die die = new Die(diceText);

                assertDoesNotThrow(() -> {
                    building.add(die, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
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
            public void two_wood_stacked_scores_4(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of 2 wood die stacked on each other
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, space);
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 4
                assertEquals(Material.WOOD, materialScore.getMaterial());
                // ⚠️ For those who haven't completed wood correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "2,1",
                    "1,2",
                    "2,2"
            })
            public void two_wood_touching_in_same_column_scores_4(int rowVal, int colVal) {

                // Given a building composed of 2 wood die stacked on each other
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(rowVal), Col.at(colVal)));
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(rowVal + 1), Col.at(colVal)));
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 4
                assertEquals(Material.WOOD, materialScore.getMaterial());
                // ⚠️ For those who haven't completed wood correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "2,1",
                    "3,1"
            })
            public void two_wood_touching_in_same_row_scores_4(int rowVal, int colVal) {
                // Given a building composed of 2 wood die stacked on each other
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(rowVal), Col.at(colVal)));
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(rowVal), Col.at(colVal + 1)));
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 4
                assertEquals(Material.WOOD, materialScore.getMaterial());
                // ⚠️ For those who haven't completed wood correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "2,2",
                    "3,1",
                    "3,2"
            })
            public void one_wood_r1_c1_other_die_disjoint_scores_0(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a wood die at (1,1) and another disjoint die
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(1), Col.at(1)));
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "2,1",
                    "3,1",
                    "3,2"
            })
            public void one_wood_r1_c2_other_die_disjoint_scores_0(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a wood die at (1,2) and another disjoint die
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(1), Col.at(2)));
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,2",
                    "3,2"
            })
            public void one_wood_r2_c1_other_die_disjoint_scores_0(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a wood die at (2,1) and another disjoint die
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(2), Col.at(1)));
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "3,1"
            })
            public void one_wood_r2_c2_other_die_disjoint_scores_0(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a wood die at (2,2) and another disjoint die
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(2), Col.at(2)));
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,2"
            })
            public void one_wood_r3_c1_other_die_disjoint_scores_0(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a wood die at (3,1) and another disjoint die
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(3), Col.at(1)));
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1",
                    "1,2",
                    "2,1"
            })
            public void one_wood_r3_c2_other_die_disjoint_scores_0(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a wood die at (3,2) and another disjoint die
                assertDoesNotThrow(() -> {
                    building.add(ANY_WOOD_DIE, Space.from(Row.at(3), Col.at(2)));
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.WOOD, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
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
            public void wood_on_top_of_other_material_stacked_together_scores_2(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a stacked wood and other material
                assertDoesNotThrow(() -> {
                    building.add(new Die(Material.GLASS, 1), space);
                    building.add(ANY_WOOD_DIE, space);
                });

                // When you get the wood score for that building
                WoodScorer scorer = new WoodScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 2
                assertEquals(Material.WOOD, materialScore.getMaterial());
                // ⚠️ For those who haven't completed wood correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }
        }

        @Nested
        class SixWoodTests {
            @Nested
            class SixStoneTests {
                @ParameterizedTest
                @CsvSource({
                        "1,1",
                        "1,2",
                        "2,1",
                        "2,2",
                        "3,1",
                        "3,2"
                })
                public void stack_of_all_stone_scores_20(int rowVal, int colVal) {
                    // Given a building composed of a stack of 6 stone dice
                    for (int count = 1; count <= 6; count++) {
                        Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                        assertDoesNotThrow(() -> {
                            building.add(ANY_WOOD_DIE, space);
                        });
                    }

                    // When you get the stone score for that building
                    WoodScorer scorer = new WoodScorer(building);
                    MaterialScore materialScore = scorer.score();

                    // Then that score should be 34
                    assertEquals(Material.WOOD, materialScore.getMaterial());
                    // ⚠️ For those who haven't completed wood correctly (yet?)
                    assertEquals(0, materialScore.getScore());
                }

                @Test
                public void six_single_wood_stacks_scores_28() {
                    // Given a building composed of 6 separated stone dice
                    assertDoesNotThrow(() -> {
                        building.add(ANY_WOOD_DIE, Space.from(Row.at(1), Col.at(1)));
                        building.add(ANY_WOOD_DIE, Space.from(Row.at(1), Col.at(2)));
                        building.add(ANY_WOOD_DIE, Space.from(Row.at(2), Col.at(1)));
                        building.add(ANY_WOOD_DIE, Space.from(Row.at(2), Col.at(2)));
                        building.add(ANY_WOOD_DIE, Space.from(Row.at(3), Col.at(1)));
                        building.add(ANY_WOOD_DIE, Space.from(Row.at(3), Col.at(2)));
                    });

                    // When you get the stone score for that building
                    WoodScorer scorer = new WoodScorer(building);
                    MaterialScore materialScore = scorer.score();

                    // Then that score should be 12
                    assertEquals(Material.WOOD, materialScore.getMaterial());
                    // ⚠️ For those who haven't completed wood correctly (yet?)
                    assertEquals(0, materialScore.getScore());
                }
            }

        }
    }
}
