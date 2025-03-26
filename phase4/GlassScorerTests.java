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
import main.scorers.GlassScorer;
import main.scorers.MaterialScore;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class GlassScorerTests {

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
            public void new_building_has_glass_score_0() {
                // Given an empty building

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());
            }
        }

        @Nested
        class OneDieBuildingTests {
            @ParameterizedTest
            @CsvSource({
                    "1,1,4",
                    "1,2,1",
                    "2,1,6",
                    "2,2,2",
                    "3,1,3",
                    "3,2,5"
            })
            public void one_glass_scores_face_value(int rowVal, int colVal, int face) {
                // Given a building composed of a single glass die
                Die die = new Die(Material.GLASS, face);
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                assertDoesNotThrow(() -> {
                    building.add(die, space);
                });

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be the face value of the die
                assertEquals(face, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1,W4",
                    "1,2,R1",
                    "2,1,S6",
                    "2,2,S2",
                    "3,1,R3",
                    "3,2,W5"
            })
            public void no_glass_scores_0(int rowVal, int colVal, String diceText) {
                // Given a building composed of a single non-glass die
                Die die = new Die(diceText);
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                assertDoesNotThrow(() -> {
                    building.add(die, space);
                });

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be 0
                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(0, materialScore.getScore());

            }
        }

        @Nested
        class TwoDiceBuildingTests {
            @ParameterizedTest
            @CsvSource({
                    "1,1,3",
                    "1,2,6",
                    "2,1,1",
                    "2,2,5",
                    "3,1,4",
                    "3,2,2"
            })
            public void two_glass_together_scores_face_values(int rowVal, int colVal, int faceValue) {
                // Given a building composed of 2 glass die stacked on each other
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                assertDoesNotThrow(() -> {
                    building.add(new Die(Material.GLASS, faceValue), space);
                    building.add(new Die(Material.GLASS, faceValue), space);
                });

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be the sum of the face values
                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(2 * faceValue, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1,3",
                    "1,2,6",
                    "2,1,1",
                    "2,2,5",
                    "3,1,4",
                    "3,2,2"
            })
            public void two_glass_apart_scores_face_values(int rowVal, int colVal, int faceValue) {
                // Given a building composed of 2 separated glass dice
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                int separateRowVal = ((rowVal + 1) % 3) + 1;
                int separateColVal = ((colVal + 1) % 2) + 1;
                Space separatedSpace = Space.from(Row.at(separateRowVal), Col.at(separateColVal));

                assertDoesNotThrow(() -> {
                    building.add(new Die(Material.GLASS, faceValue), space);
                    building.add(new Die(Material.GLASS, faceValue), separatedSpace);
                });

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be the sum of the face values
                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(2 * faceValue, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1,3",
                    "1,2,6",
                    "2,1,1",
                    "2,2,5",
                    "3,1,4",
                    "3,2,2"
            })
            public void glass_and_other_material_stacked_together_scores_face_value_of_glass_only(int rowVal,
                    int colVal,
                    int faceValue) {
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                // Given a building composed of a stacked glass and other material

                assertDoesNotThrow(() -> {
                    building.add(new Die(Material.GLASS, faceValue), space);
                    building.add(new Die("R6"), space);
                });

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be the face value of the glass
                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(faceValue, materialScore.getScore());
            }

            @ParameterizedTest
            @CsvSource({
                    "1,1,3",
                    "1,2,6",
                    "2,1,1",
                    "2,2,5",
                    "3,1,4",
                    "3,2,2"
            })
            public void glass_and_other_material_apart_scores_face_value_of_glass_only(int rowVal, int colVal,
                    int faceValue) {
                // Given a building composed of 2 separated glass and non-glass dice
                Space space = Space.from(Row.at(rowVal), Col.at(colVal));

                int separateRowVal = ((rowVal + 1) % 3) + 1;
                int separateColVal = ((colVal + 1) % 2) + 1;
                Space separatedSpace = Space.from(Row.at(separateRowVal), Col.at(separateColVal));

                assertDoesNotThrow(() -> {
                    building.add(new Die(Material.GLASS, faceValue), space);
                    building.add(new Die("W2"), separatedSpace);
                });

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be the face value of the glass
                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(faceValue, materialScore.getScore());
            }

        }

        @Nested
        class SixGlassTests {
            @ParameterizedTest
            @CsvSource({
                    "1,1,3",
                    "1,2,6",
                    "2,1,1",
                    "2,2,5",
                    "3,1,4",
                    "3,2,2"
            })
            public void stack_of_all_glass(int rowVal, int colVal, int faceValue) {
                // Given a building composed of a stack of 6 glass dice with the same face
                for (int count = 1; count <= 6; count++) {
                    Space space = Space.from(Row.at(rowVal), Col.at(colVal));
                    assertDoesNotThrow(() -> {
                        building.add(new Die(Material.GLASS, faceValue), space);
                    });
                }

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be the sum of the face values of the glass
                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(6 * faceValue, materialScore.getScore());
            }

            @Test
            public void all_separated_glass() {
                // Given a building composed of 6 separated glass dice
                assertDoesNotThrow(() -> {
                    building.add(new Die("G1"), Space.from(Row.at(1), Col.at(1)));
                    building.add(new Die("G2"), Space.from(Row.at(1), Col.at(2)));
                    building.add(new Die("G3"), Space.from(Row.at(2), Col.at(1)));
                    building.add(new Die("G4"), Space.from(Row.at(2), Col.at(2)));
                    building.add(new Die("G5"), Space.from(Row.at(3), Col.at(1)));
                    building.add(new Die("G6"), Space.from(Row.at(3), Col.at(2)));
                });

                // When you get the glass score for that building
                GlassScorer scorer = new GlassScorer(building);
                MaterialScore materialScore = scorer.score();

                // Then that score should be the sum of the face values of the glass
                assertEquals(Material.GLASS, materialScore.getMaterial());
                assertEquals(21, materialScore.getScore());
            }
        }

    }

}
