package test.phase2.building;

import static java.util.Comparator.comparing;
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
import main.building.Die;
import main.building.Material;
import main.exceptions.runtime.InvalidLevelException;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class BuildingTests {
    private static final Die ANY_DIE = new Die("W3");

    @Nested
    class NoArgConstructorTests {
        @Test
        public void new_building_has_height_0() {
            Building building = new Building();

            assertEquals(0, building.getHeight());
        }

        @Test
        public void new_building_has_no_dice() {
            Building building = new Building();

            assertEquals(0, building.getNumDice());
            assertTrue(building.isEmpty());
        }

        @Test
        public void new_building_is_valid() {
            Building building = new Building();

            assertTrue(building.isValid());
        }

        @Test
        public void new_building_has_no_violations() {
            Building building = new Building();

            assertFalse(building.getViolations().hasViolations());
        }
    }

    @Nested
    class CopyConstructorTests {
        @Test
        public void copy_constructor_works() throws Exception {
            // Given a building with one die in it
            Building originalBuilding = new Building();
            originalBuilding.add(new Die("G3"), Space.from(Row.at(2), Col.at(1)));

            // When we store the original's basic properties
            int originalHeight = originalBuilding.getHeight();
            int originalNumDice = originalBuilding.getNumDice();
            boolean originalIsValid = originalBuilding.isValid();
            boolean originalViolations = originalBuilding.getViolations().hasViolations();
            Die originalDie = originalBuilding.getDie(Space.from(Row.at(2), Col.at(1)), 1);

            // And when we make a copy of that building
            Building copyOfBuilding = new Building(originalBuilding);

            // Then they copy has the same basic properties as the original
            assertEquals(originalHeight, copyOfBuilding.getHeight());
            assertEquals(originalNumDice, copyOfBuilding.getNumDice());
            assertEquals(originalIsValid, copyOfBuilding.isValid());
            assertEquals(originalViolations, copyOfBuilding.getViolations().hasViolations());
            assertEquals(originalDie, copyOfBuilding.getDie(Space.from(Row.at(2), Col.at(1)), 1));

            // And when we alter the original
            originalBuilding.add(new Die("W2"), Space.from(Row.at(2), Col.at(1))); // Descending die!

            // Then the copy hasn't changed.
            assertEquals(originalHeight, copyOfBuilding.getHeight());
            assertEquals(originalNumDice, copyOfBuilding.getNumDice());
            assertEquals(originalIsValid, copyOfBuilding.isValid());
            assertEquals(originalViolations, copyOfBuilding.getViolations().hasViolations());
            assertEquals(originalDie, copyOfBuilding.getDie(Space.from(Row.at(2), Col.at(1)), 1));
        }
    }

    @Nested
    class AddDieTests {
        private Building building;

        @BeforeEach
        public void setUp() {
            this.building = new Building();
        }

        @Test
        public void adding_dice_in_one_place_increases_height_and_num_dice() throws Exception {
            building.add(ANY_DIE, Space.from(Row.at(1), Col.at(1)));

            assertEquals(1, building.getHeight());
            assertEquals(1, building.getNumDice());
            assertFalse(building.isEmpty());

            building.add(ANY_DIE, Space.from(Row.at(1), Col.at(1)));

            assertEquals(2, building.getHeight());
            assertEquals(2, building.getNumDice());
        }

        @Test
        public void adding_dice_but_not_stacked_increases_num_dice_but_doesnt_increase_height()
                throws Exception {
            building.add(ANY_DIE, Space.from(Row.at(1), Col.at(1)));
            building.add(ANY_DIE, Space.from(Row.at(1), Col.at(2)));
            assertEquals(2, building.getNumDice());
            assertEquals(1, building.getHeight());

            building.add(ANY_DIE, Space.from(Row.at(2), Col.at(1)));
            building.add(ANY_DIE, Space.from(Row.at(2), Col.at(2)));
            assertEquals(4, building.getNumDice());
            assertEquals(1, building.getHeight());

            building.add(ANY_DIE, Space.from(Row.at(3), Col.at(1)));
            building.add(ANY_DIE, Space.from(Row.at(3), Col.at(2)));
            assertEquals(6, building.getNumDice());
            assertEquals(1, building.getHeight());
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
        public void adding_desending_dice_creates_an_invalid_building_with_descending_violation(int row, int col)
                throws Exception {
            building.add(new Die("S4"), Space.from(Row.at(row), Col.at(col)));
            building.add(new Die("W2"), Space.from(Row.at(row), Col.at(col)));

            assertFalse(building.isValid());
            assertEquals("[DESCENDING_DICE]", building.getViolations().toString());
        }

    }

    @Nested
    class HeightTests {
        @Test
        public void building_with_one_die_has_height_1() throws Exception {
            for (int row = 1; row <= 3; row++) {
                for (int col = 1; col <= 2; col++) {
                    Building building = new Building();
                    building.add(new Die("G1"), Space.from(Row.at(row), Col.at(col)));

                    assertEquals(1, building.getHeight());
                }
            }
        }

        @Test
        public void building_with_one_die_in_each_location_has_height_1() throws Exception {
            Building building = new Building();
            for (int row = 1; row <= 3; row++) {
                for (int col = 1; col <= 2; col++) {

                    building.add(new Die("G1"), Space.from(Row.at(row), Col.at(col)));

                }
            }
            assertEquals(1, building.getHeight());
        }

        @Test
        public void building_with_stacks_of_heights_3_2_1_has_height_3() throws Exception {
            Building building = new Building();

            building.add(new Die("W4"), Space.from(Row.at(3), Col.at(2)));
            building.add(new Die("R6"), Space.from(Row.at(3), Col.at(2)));
            building.add(new Die("W6"), Space.from(Row.at(3), Col.at(2)));

            building.add(new Die("W4"), Space.from(Row.at(1), Col.at(1)));
            building.add(new Die("R6"), Space.from(Row.at(1), Col.at(1)));

            building.add(new Die("W4"), Space.from(Row.at(2), Col.at(1)));

            assertEquals(3, building.getHeight());
        }

    }

    @Nested
    class AllDiceOfGivenMaterialTests {
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
        public void building_with_1_glass_will_return_list_with_glass_but_others_will_return_empty(int row, int col)
                throws Exception {

            building.add(new Die("G1"), Space.from(Row.at(row), Col.at(col)));

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
        public void building_with_1_stone_will_return_list_with_stone_but_others_will_return_empty(int row, int col)
                throws Exception {

            building.add(new Die("S2"), Space.from(Row.at(row), Col.at(col)));

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
        public void building_with_1_recycled_will_return_list_with_recycled_but_others_will_return_empty(int row,
                int col) throws Exception {

            building.add(new Die("R4"), Space.from(Row.at(row), Col.at(col)));

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
        public void building_with_1_wood_will_return_list_with_wood_but_others_will_return_empty(int row,
                int col) throws Exception {

            building.add(new Die("W6"), Space.from(Row.at(row), Col.at(col)));

            assertEquals("[W6]", building.all(Material.WOOD).toString());
            assertEquals("[]", building.all(Material.RECYCLED).toString());
            assertEquals("[]", building.all(Material.STONE).toString());
            assertEquals("[]", building.all(Material.GLASS).toString());
        }

        @Test
        public void valid_building_with_mix_of_materials_returns_expected_lists() throws Exception {
            building.add(new Die("G1"), Space.from(Row.at(1), Col.at(1)));

            building.add(new Die("S3"), Space.from(Row.at(1), Col.at(1)));
            building.add(new Die("G5"), Space.from(Row.at(1), Col.at(1)));

            building.add(new Die("R6"), Space.from(Row.at(2), Col.at(1)));

            building.add(new Die("R3"), Space.from(Row.at(3), Col.at(1)));
            building.add(new Die("W5"), Space.from(Row.at(3), Col.at(1)));

            assertEquals("[G1, G5]", sorted(building.all(Material.GLASS)));
            assertEquals("[S3]", sorted(building.all(Material.STONE)));
            assertEquals("[R3, R6]", sorted(building.all(Material.RECYCLED)));
            assertEquals("[W5]", sorted(building.all(Material.WOOD)));
        }

    }

    @Nested
    class GetDieInvalidCases {

        @Test
        public void getting_die_from_space_with_empty_stack_throws_InvalidLevelException() throws Exception {
            Building building = new Building();

            InvalidLevelException ex = assertThrowsExactly(InvalidLevelException.class, () -> {
                building.getDie(Space.from(Row.at(2), Col.at(1)), 1);
            });

            assertEquals("No die at level 1 at [2,1].", ex.getMessage());

        }

        @Test
        public void getting_die_too_high_from_space_with_dice_throws_InvalidLevelException() throws Exception {
            Building building = new Building();
            building.add(new Die("W4"), Space.from(Row.at(2), Col.at(2)));
            building.add(new Die("G6"), Space.from(Row.at(2), Col.at(2)));

            InvalidLevelException ex = assertThrowsExactly(InvalidLevelException.class, () -> {
                building.getDie(Space.from(Row.at(2), Col.at(2)), 3);
            });

            assertEquals("No die at level 3 at [2,2].", ex.getMessage());

        }

        @Test
        public void getting_die_too_low_from_space_with_dice_throws_InvalidLevelException() throws Exception {
            Building building = new Building();
            building.add(new Die("R1"), Space.from(Row.at(1), Col.at(2)));

            InvalidLevelException ex = assertThrowsExactly(InvalidLevelException.class, () -> {
                building.getDie(Space.from(Row.at(1), Col.at(2)), 0);
            });

            assertEquals("No die at level 0 at [1,2].", ex.getMessage());

        }

    }

    @Nested
    class AllOnLevelInvalidCases {
        @Test
        public void getting_all_from_too_low_level_throws_InvalidLevelException() throws Exception {
            Building building = new Building();
            building.add(new Die("W4"), Space.from(Row.at(1), Col.at(1)));
            building.add(new Die("G6"), Space.from(Row.at(2), Col.at(2)));

            InvalidLevelException ex = assertThrowsExactly(InvalidLevelException.class, () -> {
                building.allOnLevel(0);
            });

            assertEquals("Building has no die at level 0.", ex.getMessage());

        }

        @Test
        public void getting_all_from_level_greater_than_building_height_throws_InvalidLevelException()
                throws Exception {
            Building building = new Building();
            building.add(new Die("W4"), Space.from(Row.at(1), Col.at(1)));

            building.add(new Die("G6"), Space.from(Row.at(2), Col.at(2)));
            building.add(new Die("R6"), Space.from(Row.at(2), Col.at(2))); // Building has height of 2.

            InvalidLevelException ex = assertThrowsExactly(InvalidLevelException.class, () -> {
                building.allOnLevel(3);
            });

            assertEquals("Building has no die at level 3.", ex.getMessage());

        }

    }
}
