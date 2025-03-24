package test.phase3.building;

import static java.util.Comparator.comparing;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import main.building.Building;
import main.building.DiceStack;
import main.building.Die;
import main.building.Material;
import main.exceptions.checked.InvalidBuildingException;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class BuildingTests {
    private static final Die ANY_DIE = new Die("W3");

    @Nested
    class AddDieTestsValidCases {
        private Building building;

        @BeforeEach
        public void setUp() {
            this.building = new Building();
        }

        @Test
        public void adding_dice_changes_emptiness() {
            assertTrue(building.isEmpty());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
            });

            assertFalse(building.isEmpty());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
            });

            assertFalse(building.isEmpty());
        }

        @Test
        public void adding_dice_in_valid_way_doesnt_create_violations() {
            assertFalse(building.getViolations().hasViolations());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
            });

            assertFalse(building.getViolations().hasViolations());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
            });

            assertFalse(building.getViolations().hasViolations());
        }

        @Test
        public void adding_dice_in_valid_way_doesnt_make_building_invalid() {
            assertTrue(building.isValid());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
            });

            assertTrue(building.isValid());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));
            });

            assertTrue(building.isValid());
        }

        @Test
        public void adding_dice_in_one_place_increases_height_and_num_dice() {
            Space space = Space.from(Row.at(1), Col.at(1));

            assertEquals(0, building.getHeight());
            assertEquals(0, building.getNumDice());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, space);
            });

            assertEquals(1, building.getHeight());
            assertEquals(1, building.getNumDice());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, space);
            });

            assertEquals(2, building.getHeight());
            assertEquals(2, building.getNumDice());
        }

        @Test
        public void adding_dice_but_not_stacked_increases_num_dice_but_doesnt_increase_height() {
            assertEquals(0, building.getHeight());
            assertEquals(0, building.getNumDice());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));
            });

            assertEquals(2, building.getNumDice());
            assertEquals(1, building.getHeight());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(2)));
            });

            assertEquals(4, building.getNumDice());
            assertEquals(1, building.getHeight());

            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(2)));
            });

            assertEquals(6, building.getNumDice());
            assertEquals(1, building.getHeight());
        }

    }

    @Nested
    class AddDiceStackTestsValidCases {
        private Building building;
        private DiceStack ANY_DICE_STACK;

        @BeforeEach
        public void setUp() {
            this.building = new Building();
            ANY_DICE_STACK = new DiceStack();
            ANY_DICE_STACK.add(new Die("R2"));
            ANY_DICE_STACK.add(new Die("G2"));
        }

        @Test
        public void adding_dice_changes_emptiness() {
            assertTrue(building.isEmpty());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(3), Col.at(1)));
            });

            assertFalse(building.isEmpty());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DICE_STACK, Space.from(Row.at(1), Col.at(1)));
            });

            assertFalse(building.isEmpty());
        }

        @Test
        public void adding_dice_in_valid_way_doesnt_create_violations() {
            assertFalse(building.getViolations().hasViolations());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(3), Col.at(1)));
            });

            assertFalse(building.getViolations().hasViolations());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DICE_STACK, Space.from(Row.at(1), Col.at(1)));
            });

            assertFalse(building.getViolations().hasViolations());
        }

        @Test
        public void adding_dice_in_valid_way_doesnt_make_building_invalid() {
            assertTrue(building.isValid());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(3), Col.at(1)));
            });

            assertTrue(building.isValid());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DICE_STACK, Space.from(Row.at(1), Col.at(1)));
            });

            assertTrue(building.isValid());
        }

        @Test
        public void adding_dice_in_one_place_increases_height_and_num_dice() {
            Space space = Space.from(Row.at(1), Col.at(1));

            assertEquals(0, building.getHeight());
            assertEquals(0, building.getNumDice());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, space);
            });

            assertEquals(2, building.getHeight());
            assertEquals(2, building.getNumDice());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, space);
                building.add(ANY_DICE_STACK, space);
            });

            assertEquals(6, building.getHeight());
            assertEquals(6, building.getNumDice());
        }

        @Test
        public void adding_dice_but_not_stacked_increases_num_dice_but_doesnt_increase_height() {
            assertEquals(0, building.getHeight());
            assertEquals(0, building.getNumDice());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(1), Col.at(1)));
            });

            assertEquals(2, building.getNumDice());
            assertEquals(2, building.getHeight());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(1), Col.at(2)));
            });

            assertEquals(4, building.getNumDice());
            assertEquals(2, building.getHeight());

            assertDoesNotThrow(() -> {
                building.add(ANY_DICE_STACK, Space.from(Row.at(2), Col.at(2)));
            });

            assertEquals(6, building.getNumDice());
            assertEquals(2, building.getHeight());
        }

        @Test
        public void adding_dice_stacks_doesnt_replace_dice_already_there() {
            Die presentDie = new Die("R3");
            Space space = Space.from(Row.at(2), Col.at(1));

            assertDoesNotThrow(() -> {
                building.add(presentDie, space);
            });

            DiceStack stack = new DiceStack();
            stack.add(new Die("G3"));
            stack.add(new Die("R4"));

            assertDoesNotThrow(() -> {
                building.add(stack, space);
            });

            Die dieWherePresentDieShouldBe = building.getDie(space, 1);

            assertEquals(presentDie, dieWherePresentDieShouldBe);

        }
    }

    @Nested
    class AddDieTestsInvalidCases {
        private Building building;

        @BeforeEach
        public void setUp() {
            this.building = new Building();
        }

        @Test
        public void adding_descending_die_throws_InvalidBuildingException() {
            Building building = new Building();

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), Space.from(Row.at(2), Col.at(1))); // This add is ok.
            });

            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(new Die("W3"), Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals("Building has these violations: [DESCENDING_DICE].", ex.getMessage());
        }

        @Test
        public void adding_descending_die_still_increases_height() {
            Building building = new Building();

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), Space.from(Row.at(2), Col.at(1))); // This add is ok.
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(new Die("W3"), Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals(2, building.getHeight());
        }

        @Test
        public void adding_descending_die_still_increases_number_of_dice() {
            Building building = new Building();

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), Space.from(Row.at(2), Col.at(1))); // This add is ok.
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(new Die("W3"), Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals(2, building.getNumDice());
        }

        @Test
        public void adding_descending_die_still_generates_violations() {
            Building building = new Building();

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), Space.from(Row.at(2), Col.at(1))); // This add is ok.
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(new Die("W3"), Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals("[DESCENDING_DICE]", building.getViolations().toString());

        }

        @Test
        public void adding_descending_die_still_sets_building_as_invalid() {
            Building building = new Building();

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), Space.from(Row.at(2), Col.at(1))); // This add is ok.
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(new Die("W3"), Space.from(Row.at(2), Col.at(1)));
            });

            assertFalse(building.isValid());
        }

        @Test
        public void adding_descending_die_still_places_die() {
            Building building = new Building();

            Space space = Space.from(Row.at(2), Col.at(1));

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), space); // This add is ok.
            });

            Die addedDie = new Die("W3");
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(addedDie, space);
            });

            Die actualDie = building.getDie(space, 2);

            assertEquals(addedDie, actualDie);
        }

        @Test
        public void adding_multiple_descending_die_still_changes_building_as_usual() {
            Building building = new Building();

            Space space = Space.from(Row.at(3), Col.at(2));

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), space); // This add is ok.
            });

            Die firstDescDie = new Die("W3");
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(firstDescDie, space);
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("S5"), space);
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("W5"), space);
            });

            Die secondDescDie = new Die("R1");
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(secondDescDie, space);
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("G6"), space);
            });

            Die actualFirstDie = building.getDie(space, 2);
            Die actualSecondDie = building.getDie(space, 5);

            assertEquals(firstDescDie, actualFirstDie);
            assertEquals(secondDescDie, actualSecondDie);

            assertFalse(building.isValid());

            assertEquals(6, building.getNumDice());

            assertEquals(6, building.getHeight());

            assertEquals("[DESCENDING_DICE]", building.getViolations().toString());

        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void adding_7th_die_to_building_in_one_stack_throws_InvalidBuildingException_with_expected_msg(
                int rowVal,
                int colVal) {

            Space space = Space.from(Row.at(rowVal), Col.at(colVal));

            assertDoesNotThrow(() -> {
                // First 6 dice are ok.
                for (int count = 1; count <= 6; count++) {
                    building.add(ANY_DIE, space);
                }
            });

            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                // The stack is now too big - and so is the building.
                building.add(ANY_DIE, space);
            });

            assertEquals("Building has these violations: [BUILDING_OVERLARGE, STACK_OVERLARGE].", ex.getMessage());
        }

        @Test
        public void adding_8th_die_to_building_in_one_stack_still_changes_building_as_usual() {

            Space space = Space.from(Row.at(3), Col.at(1));

            assertDoesNotThrow(() -> {
                // First 6 dice are ok.
                for (int count = 1; count <= 6; count++) {
                    building.add(ANY_DIE, space);
                }
            });

            Die firstDieTooMany = new Die("G6");
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(firstDieTooMany, space);
            });

            Die secondDieTooMany = new Die("R6");
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(secondDieTooMany, space);
            });

            Die actualFirstDie = building.getDie(space, 7);
            Die actualSecondDie = building.getDie(space, 8);

            assertEquals(firstDieTooMany, actualFirstDie);
            assertEquals(secondDieTooMany, actualSecondDie);

            assertFalse(building.isValid());

            assertEquals(8, building.getNumDice());

            assertEquals(8, building.getHeight());

            assertEquals("[BUILDING_OVERLARGE, STACK_OVERLARGE]", building.getViolations().toString());

        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void adding_7th_die_to_building_but_no_overlarge_stack_throws_InvalidBuildingException_with_expected_msg(
                int rowVal,
                int colVal) {

            // Spread 6 dice evenly across building.
            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));

                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(2)));

                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(2)));
            });

            // Add a 7th die.
            Space space = Space.from(Row.at(rowVal), Col.at(colVal));
            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(ANY_DIE, space);
            });

            assertEquals("Building has these violations: [BUILDING_OVERLARGE].", ex.getMessage());
        }

        @Test
        public void adding_8th_die_to_building_but_no_overlarge_stack_still_changes_building_as_usual() {

            // Spread 6 dice evenly across building.
            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));

                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(2)));

                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(2)));
            });

            // Add a 7th die.
            Die firstDieTooMany = new Die("G6");
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(firstDieTooMany, Space.from(Row.at(1), Col.at(2)));
            });

            // Add a 7th die.
            Die secondDieTooMany = new Die("W6");
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(secondDieTooMany, Space.from(Row.at(2), Col.at(2)));
            });

            Die actualFirstDie = building.getDie(Space.from(Row.at(1), Col.at(2)), 2);
            Die actualSecondDie = building.getDie(Space.from(Row.at(2), Col.at(2)), 2);

            assertEquals(firstDieTooMany, actualFirstDie);
            assertEquals(secondDieTooMany, actualSecondDie);

            assertFalse(building.isValid());

            assertEquals(8, building.getNumDice());

            assertEquals(2, building.getHeight());

            assertEquals("[BUILDING_OVERLARGE]", building.getViolations().toString());

        }

        @Test
        public void breaking_multiple_rules_reports_expected_exception_message() {
            Space space = Space.from(Row.at(3), Col.at(1));

            assertDoesNotThrow(() -> {
                // First 6 dice are ok.
                for (int count = 1; count <= 6; count++) {
                    building.add(ANY_DIE, space);
                }
            });

            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("G1"), space); // Descending and too big stack and building.
            });

            assertEquals("Building has these violations: [BUILDING_OVERLARGE, DESCENDING_DICE, STACK_OVERLARGE].",
                    ex.getMessage());
        }

    }

    @Nested
    class AddDiceStackTestsInvalidCases {
        private Building building;

        @BeforeEach
        public void setUp() {
            this.building = new Building();
        }

        @Test
        public void adding_descending_dice_stack_to_ok_stack_throws_InvalidBuildingException() {
            Building building = new Building();

            Space sharedSpace = Space.from(Row.at(2), Col.at(1));

            DiceStack okStack = new DiceStack();
            okStack.add(new Die("S2"));
            okStack.add(new Die("W2"));

            DiceStack descStack = new DiceStack();
            descStack.add(new Die("G4"));
            descStack.add(new Die("R1"));

            assertDoesNotThrow(() -> {
                building.add(okStack, sharedSpace); // This add is ok.
            });

            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(descStack, sharedSpace);
            });

            assertEquals("Building has these violations: [DESCENDING_DICE].", ex.getMessage());
        }

        @Test
        public void adding_descending_dice_stack_to_different_location_throws_InvalidBuildingException() {
            Building building = new Building();

            DiceStack okStack = new DiceStack();
            okStack.add(new Die("S2"));
            okStack.add(new Die("W2"));

            DiceStack descStack = new DiceStack();
            descStack.add(new Die("G4"));
            descStack.add(new Die("R1"));

            assertDoesNotThrow(() -> {
                building.add(okStack, Space.from(Row.at(3), Col.at(1))); // This add is ok.
            });

            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(descStack, Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals("Building has these violations: [DESCENDING_DICE].", ex.getMessage());
        }

        @Test
        public void adding_descending_dice_stack_still_increases_height() {
            Building building = new Building();

            DiceStack descStack = new DiceStack();
            descStack.add(new Die("G4"));
            descStack.add(new Die("R1"));

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(descStack, Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals(2, building.getHeight());
        }

        @Test
        public void adding_descending_dice_stack_still_increases_number_of_dice() {
            Building building = new Building();

            DiceStack descStack = new DiceStack();
            descStack.add(new Die("G4"));
            descStack.add(new Die("R1"));

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(descStack, Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals(2, building.getNumDice());
        }

        @Test
        public void adding_descending_dice_stack_still_generates_violations() {
            Building building = new Building();

            DiceStack descStack = new DiceStack();
            descStack.add(new Die("G4"));
            descStack.add(new Die("R1"));

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(descStack, Space.from(Row.at(2), Col.at(1)));
            });

            assertEquals("[DESCENDING_DICE]", building.getViolations().toString());

        }

        @Test
        public void adding_descending_dice_stack_still_sets_building_as_invalid() {
            Building building = new Building();

            DiceStack descStack = new DiceStack();
            descStack.add(new Die("G4"));
            descStack.add(new Die("R1"));

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(descStack, Space.from(Row.at(2), Col.at(1)));
            });

            assertFalse(building.isValid());
        }

        @Test
        public void adding_descending_dice_stack_still_places_dice() {
            Building building = new Building();

            Space space = Space.from(Row.at(2), Col.at(1));

            Die bottomDie = new Die("G4");
            Die topDie = new Die("R1");

            DiceStack descStack = new DiceStack();
            descStack.add(bottomDie);
            descStack.add(topDie);

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                // This creates a descending die violation.
                building.add(descStack, space);
            });

            Die actualTopDie = building.getDie(space, 2);
            Die actualBotomDie = building.getDie(space, 1);

            assertEquals(topDie, actualTopDie);
            assertEquals(bottomDie, actualBotomDie);
        }

        @Test
        public void adding_multiple_descending_dice_stacks_still_changes_building_as_usual() {
            Building building = new Building();

            Space space = Space.from(Row.at(3), Col.at(2));

            DiceStack okStack = new DiceStack();
            okStack.add(new Die("S2"));
            okStack.add(new Die("W2"));

            Die firstDescDie = new Die("G1");

            DiceStack descStackOne = new DiceStack();
            descStackOne.add(firstDescDie);
            descStackOne.add(new Die("W3"));

            Die secondDescDie = new Die("G3");

            DiceStack descStackTwo = new DiceStack();
            descStackTwo.add(new Die("W4"));
            descStackTwo.add(secondDescDie);

            assertDoesNotThrow(() -> {
                building.add(okStack, space); // This add is ok.
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(descStackOne, space);
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(descStackTwo, space);
            });

            Die actualFirstDie = building.getDie(space, 3);
            Die actualSecondDie = building.getDie(space, 6);

            assertEquals(firstDescDie, actualFirstDie);
            assertEquals(secondDescDie, actualSecondDie);

            assertFalse(building.isValid());

            assertEquals(6, building.getNumDice());

            assertEquals(6, building.getHeight());

            assertEquals("[DESCENDING_DICE]", building.getViolations().toString());

        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void adding_7th_die_to_building_in_one_stack_throws_InvalidBuildingException_with_expected_msg(
                int rowVal,
                int colVal) {

            Space space = Space.from(Row.at(rowVal), Col.at(colVal));

            DiceStack overlargeStack = new DiceStack();

            for (int count = 1; count <= 7; count++) {
                overlargeStack.add(ANY_DIE);
            }

            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                // The stack is now too big - and so is the building.
                building.add(overlargeStack, space);
            });

            assertEquals("Building has these violations: [BUILDING_OVERLARGE, STACK_OVERLARGE].", ex.getMessage());
        }

        @Test
        public void adding_8th_die_to_building_in_one_stack_still_changes_building_as_usual() {

            Space space = Space.from(Row.at(3), Col.at(1));
            DiceStack okStack = new DiceStack();
            Die bottomDie = new Die("R1");

            okStack.add(bottomDie);

            for (int count = 1; count <= 5; count++) {
                okStack.add(ANY_DIE);
            }

            assertDoesNotThrow(() -> {
                building.add(okStack, space); // This add is ok.
            });

            DiceStack toppingStack = new DiceStack();
            toppingStack.add(new Die("R5"));
            Die topDie = new Die("G6");
            toppingStack.add(topDie);

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(toppingStack, space);
            });

            Die actualBottomDie = building.getDie(space, 1);
            Die actualTopDie = building.getDie(space, 8);

            assertEquals(bottomDie, actualBottomDie);
            assertEquals(topDie, actualTopDie);

            assertFalse(building.isValid());

            assertEquals(8, building.getNumDice());

            assertEquals(8, building.getHeight());

            assertEquals("[BUILDING_OVERLARGE, STACK_OVERLARGE]", building.getViolations().toString());

        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void adding_7th_die_to_building_but_no_overlarge_stack_throws_InvalidBuildingException_with_expected_msg(
                int rowVal,
                int colVal) {

            // Spread 6 dice evenly across building.
            assertDoesNotThrow(() -> {
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));

                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(2), Col.at(2)));

                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
                building.add(ANY_DIE, Space.from(Row.at(3), Col.at(2)));
            });

            // Add a 7th die: a stack with one die
            Space space = Space.from(Row.at(rowVal), Col.at(colVal));
            DiceStack oneDieStack = new DiceStack();
            oneDieStack.add(new Die("R5"));

            InvalidBuildingException ex = assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(oneDieStack, space);
            });

            assertEquals("Building has these violations: [BUILDING_OVERLARGE].", ex.getMessage());
        }

    }

    @Nested
    class AllDiceOfGivenMaterialTestsValidCases {
        private Building building;

        private String sorted(ArrayList<Die> dice) {
            dice.sort(comparing(Die::toString));
            return dice.toString();
        }

        @BeforeEach
        public void setUp() {
            this.building = new Building();
        }

        @Test
        public void empty_building_will_return_empty_list() {
            assertEquals("[]", building.all(Material.GLASS).toString());
            assertEquals("[]", building.all(Material.WOOD).toString());
            assertEquals("[]", building.all(Material.RECYCLED).toString());
            assertEquals("[]", building.all(Material.STONE).toString());
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void building_with_1_glass_will_return_list_with_glass_but_others_will_return_empty(
                int rowVal,
                int colVal) {

            assertDoesNotThrow(() -> {
                building.add(new Die("G1"), Space.from(Row.at(rowVal), Col.at(colVal)));
            });

            assertEquals("[G1]", building.all(Material.GLASS).toString());
            assertEquals("[]", building.all(Material.WOOD).toString());
            assertEquals("[]", building.all(Material.RECYCLED).toString());
            assertEquals("[]", building.all(Material.STONE).toString());
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void building_with_1_stone_will_return_list_with_stone_but_others_will_return_empty(
                int rowVal,
                int colVal) {

            assertDoesNotThrow(() -> {
                building.add(new Die("S2"), Space.from(Row.at(rowVal), Col.at(colVal)));

            });

            assertEquals("[S2]", building.all(Material.STONE).toString());
            assertEquals("[]", building.all(Material.WOOD).toString());
            assertEquals("[]", building.all(Material.RECYCLED).toString());
            assertEquals("[]", building.all(Material.GLASS).toString());
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void building_with_1_recycled_will_return_list_with_recycled_but_others_will_return_empty(
                int rowVal,
                int colVal) {

            assertDoesNotThrow(() -> {
                building.add(new Die("R4"), Space.from(Row.at(rowVal), Col.at(colVal)));
            });

            assertEquals("[R4]", building.all(Material.RECYCLED).toString());
            assertEquals("[]", building.all(Material.WOOD).toString());
            assertEquals("[]", building.all(Material.STONE).toString());
            assertEquals("[]", building.all(Material.GLASS).toString());
        }

        @ParameterizedTest
        @CsvSource({
                "1, 1",
                "1, 2",
                "2, 1",
                "2, 2",
                "3, 1",
                "3, 2"
        })
        public void building_with_1_wood_will_return_list_with_wood_but_others_will_return_empty(
                int rowVal,
                int colVal) {

            assertDoesNotThrow(() -> {
                building.add(new Die("W6"), Space.from(Row.at(rowVal), Col.at(colVal)));
            });

            assertEquals("[W6]", building.all(Material.WOOD).toString());
            assertEquals("[]", building.all(Material.RECYCLED).toString());
            assertEquals("[]", building.all(Material.STONE).toString());
            assertEquals("[]", building.all(Material.GLASS).toString());
        }

        @Test
        public void valid_building_with_mix_of_materials_returns_expected_lists() {
            assertDoesNotThrow(() -> {
                building.add(new Die("G1"), Space.from(Row.at(1), Col.at(1)));

                building.add(new Die("S3"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("G5"), Space.from(Row.at(1), Col.at(1)));

                building.add(new Die("R6"), Space.from(Row.at(2), Col.at(1)));

                building.add(new Die("R3"), Space.from(Row.at(3), Col.at(1)));
                building.add(new Die("W5"), Space.from(Row.at(3), Col.at(1)));
            });

            assertEquals("[G1, G5]", sorted(building.all(Material.GLASS)));
            assertEquals("[S3]", sorted(building.all(Material.STONE)));
            assertEquals("[R3, R6]", sorted(building.all(Material.RECYCLED)));
            assertEquals("[W5]", sorted(building.all(Material.WOOD)));
        }

    }

    @Nested
    class AllDiceOfGivenMaterialTestsInvalidCases {
        private Building building;

        private String sorted(ArrayList<Die> dice) {
            dice.sort(comparing(Die::toString));
            return dice.toString();
        }

        @BeforeEach
        public void setUp() {
            this.building = new Building();
        }

        @Test
        public void invalid_building_with_mix_of_materials_returns_expected_lists() {
            assertDoesNotThrow(() -> {
                building.add(new Die("G1"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("G2"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("G3"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("G4"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("G5"), Space.from(Row.at(1), Col.at(1)));
                building.add(new Die("G6"), Space.from(Row.at(1), Col.at(1)));
            });

            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("S3"), Space.from(Row.at(1), Col.at(1))); // stack & building overlarge now
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("S3"), Space.from(Row.at(1), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("S3"), Space.from(Row.at(1), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("G1"), Space.from(Row.at(1), Col.at(1))); // descending!
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("R1"), Space.from(Row.at(2), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("R3"), Space.from(Row.at(2), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("R6"), Space.from(Row.at(2), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("R2"), Space.from(Row.at(3), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("W5"), Space.from(Row.at(3), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("W6"), Space.from(Row.at(3), Col.at(1)));
            });
            assertThrowsExactly(InvalidBuildingException.class, () -> {
                building.add(new Die("W2"), Space.from(Row.at(3), Col.at(2)));
            });

            assertEquals("[G1, G1, G2, G3, G4, G5, G6]", sorted(building.all(Material.GLASS)));
            assertEquals("[S3, S3, S3]", sorted(building.all(Material.STONE)));
            assertEquals("[R1, R2, R3, R6]", sorted(building.all(Material.RECYCLED)));
            assertEquals("[W2, W5, W6]", sorted(building.all(Material.WOOD)));
        }

    }

}
