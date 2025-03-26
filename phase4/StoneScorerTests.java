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
import main.scorers.StoneScorer;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class StoneScorerTests {
    // We don't care about stone die faces, so we'll use
    // this as a stand-in for any old stone die.
    private static final Die ANY_STONE_DIE = new Die(Material.STONE, 6);

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
            public void new_building_has_stone_score_0() {
                // Given an empty building

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.STONE, materialScore.getMaterial());
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
            public void one_stone_scores_2(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a single stone die
                assertDoesNotThrow(() -> {
                    building.add(ANY_STONE_DIE, space);
                });

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1,W4",
                    "1,2,G1",
                    "2,1,R6",
                    "2,2,R2",
                    "3,1,G3",
                    "3,2,W5"
            })
            public void no_stone_scores_0(int rowVal, int colVal, String diceText) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a single non-stone die
                Die notStoneDie = new Die(diceText);

                assertDoesNotThrow(() -> {
                    building.add(notStoneDie, space);
                });

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.STONE, materialScore.getMaterial());
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
            public void two_stone_together_scores_5(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of 2 stacked stone dice
                assertDoesNotThrow(() -> {
                    building.add(ANY_STONE_DIE, space);
                    building.add(ANY_STONE_DIE, space);
                });
                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 5
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
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
            public void two_stone_apart_scores_4(int rowVal, int colVal) {
                // Given a building composed of 2 separate stone dice
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                int separateRowVal = ((rowVal + 1) % 3) + 1;
                int separateColVal = ((colVal + 1) % 2) + 1;
                Space separatedSpace = Space.from(Row.at(separateRowVal), Col.at(separateColVal));

                assertDoesNotThrow(() -> {
                    building.add(ANY_STONE_DIE, space);
                    building.add(ANY_STONE_DIE, separatedSpace);
                });

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 4
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
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
            public void stone_on_top_of_other_material_stacked_together_scores_3(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a stone stacked on another material
                assertDoesNotThrow(() -> {
                    building.add(new Die(Material.GLASS, 1), space);
                    building.add(ANY_STONE_DIE, space);
                });
                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 3
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
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
            public void stone_under_other_material_stacked_together_scores_2(int rowVal, int colVal) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a stone stacked under another material
                assertDoesNotThrow(() -> {
                    building.add(ANY_STONE_DIE, space);
                    building.add(new Die(Material.WOOD, 6), space);
                });

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 2
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
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
            public void stone_and_other_material_apart_scores_2(int rowVal, int colVal) {
                // Given a building composed of 2 separated stone and non-stone dice
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                int separateRowVal = ((rowVal + 1) % 3) + 1;
                int separateColVal = ((colVal + 1) % 2) + 1;
                Space separatedSpace = Space.from(Row.at(separateRowVal), Col.at(separateColVal));

                assertDoesNotThrow(() -> {
                    building.add(ANY_STONE_DIE, space);
                    building.add(new Die("W2"), separatedSpace);
                });

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 2
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }

        }

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
            public void stack_of_all_stone_scores_34(int rowVal, int colVal) {
                // Given a building composed of a stack of 6 stone dice
                for (int count = 1; count <= 6; count++) {
                    Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                    assertDoesNotThrow(() -> {
                        building.add(ANY_STONE_DIE, space);
                    });
                }

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 34
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }

            @Test
            public void six_single_stone_stacks_scores_12() {
                // Given a building composed of 6 separated stone dice
                assertDoesNotThrow(() -> {
                    building.add(ANY_STONE_DIE, Space.from(Row.at(1), Col.at(1)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(1), Col.at(2)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(2), Col.at(1)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(2), Col.at(2)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(3), Col.at(1)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(3), Col.at(2)));
                });

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 12
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }

            @Test
            public void three_double_stone_stacks_scores_15() {
                // Given a building composed of 3 separated stone stacks of 2
                assertDoesNotThrow(() -> {
                    building.add(ANY_STONE_DIE, Space.from(Row.at(1), Col.at(1)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(1), Col.at(1)));

                    building.add(ANY_STONE_DIE, Space.from(Row.at(2), Col.at(2)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(2), Col.at(2)));

                    building.add(ANY_STONE_DIE, Space.from(Row.at(3), Col.at(1)));
                    building.add(ANY_STONE_DIE, Space.from(Row.at(3), Col.at(1)));
                });

                // When you get the stone score for that building
                StoneScorer scorer = new StoneScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 15
                assertEquals(Material.STONE, materialScore.getMaterial());
                // ⚠️ For those who haven't completed stone correctly (yet?)
                assertEquals(0, materialScore.getScore());
            }
        }

    }

}
