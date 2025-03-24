package test.phase3.scoringresult;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import main.blueprint.Blueprint;
import main.building.Building;
import main.building.Die;
import main.exceptions.checked.InvalidPlacementException;
import main.scoringresult.ScoringResult;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class ScoringResultTests {

    @Nested
    class ConstructorTestsValidCases {
        @Test
        public void constructor_doesnt_throw_InvalidPlacementException_if_building_is_empty() {
            Blueprint blueprint = new Blueprint("1X 21 1X");

            Building building = new Building();

            assertDoesNotThrow(() -> {
                // This shouldn't throw anything, since empty buildings don't violate
                // any placement rules.
                new ScoringResult(blueprint, building);
            });

        }

        @Test
        public void constructor_doesnt_throw_InvalidPlacementException_if_no_prohibited_spaces_and_there_is_a_bonus() {
            // Given a ScoringResult for a valid Building that
            // fills all expected Blueprint spaces exactly
            Blueprint blueprint = new Blueprint("1X 21 1X");

            Building building = new Building();
            assertDoesNotThrow(() -> {
                // This shouldn't throw anything, since all these adds
                // are valid.
                building.add(new Die("W1"), Space.from(Row.at(1), Col.at(1)));

                building.add(new Die("G2"), Space.from(Row.at(2), Col.at(1)));
                building.add(new Die("G3"), Space.from(Row.at(2), Col.at(1)));

                building.add(new Die("R5"), Space.from(Row.at(2), Col.at(2)));

                building.add(new Die("S2"), Space.from(Row.at(3), Col.at(1)));
            });

            assertDoesNotThrow(() -> {
                // This shouldn't throw anything, since the building is valid -
                // and even gets a bonus!
                new ScoringResult(blueprint, building);
            });

        }

        @Test
        public void constructor_doesnt_throw_InvalidPlacementException_if_no_prohibited_spaces_used_but_no_bonus() {
            Blueprint blueprint = new Blueprint("3X 3X XX");

            Building building = new Building();
            assertDoesNotThrow(() -> {
                // This shouldn't throw anything, since all these adds
                // are valid.
                building.add(new Die("W1"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("W2"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("W3"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("W4"), Space.from(Row.at(1), Col.at(1)));

                building.add(new Die("G2"), Space.from(Row.at(2), Col.at(1)));
                building.add(new Die("G3"), Space.from(Row.at(2), Col.at(1)));
            });

            assertDoesNotThrow(() -> {
                // This shouldn't throw anything, since the building is valid -
                // even though it doesn't get a bonus!
                new ScoringResult(blueprint, building);
            });

        }
    }

    @Nested
    class ConstructorTestsInvalidCases {

        private static final Die ANY_DIE = new Die("S3");

        @ParameterizedTest
        @CsvSource({
                "'X3 3X XX',1,1",
                "'X3 3X XX',2,2",
                "'X3 3X XX',3,1",
                "'X3 3X XX',3,2"
        })
        public void constructor_throws_InvalidPlacementException_when_building_has_stacks_on_prohibited_spaces(
                String blueprintTemplate,
                int badRowVal,
                int badColVal) {

            Space prohibitedSpace = Space.from(Row.at(badRowVal), Col.at(badColVal));

            Blueprint blueprint = new Blueprint(blueprintTemplate);

            Building badPlacementBuilding = new Building();

            assertDoesNotThrow(() -> {
                // This shouldn't throw anything, since the resulting building is valid.
                badPlacementBuilding.add(ANY_DIE, prohibitedSpace);
            });

            InvalidPlacementException ex = assertThrowsExactly(InvalidPlacementException.class, () -> {
                new ScoringResult(blueprint, badPlacementBuilding);
            });

            String expectedMsg = String.format("Invalid placement happened at %s.", prohibitedSpace);
            assertEquals(expectedMsg, ex.getMessage());

        }
    }
}
