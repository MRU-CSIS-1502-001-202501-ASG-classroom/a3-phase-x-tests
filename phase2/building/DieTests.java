package test.phase2.building;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import main.building.Die;
import main.building.Material;
import main.exceptions.runtime.InvalidDieFaceException;
import main.exceptions.runtime.InvalidDieMaterialException;

public class DieTests {
    @Nested
    class TextConstructorTestsValidCases {

        @Test
        public void for_glass() {
            // Given a glass die constructed from the text constructor
            Die die = new Die("G3");

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();
            if (actualMaterial == Material.GLASS) {
                //
            }
            // Then the material and face are as expected
            assertEquals(Material.GLASS, actualMaterial);
            assertEquals(3, actualFace);
        }

        @Test
        public void for_wood() {
            // Given a wood die constructed from the text constructor
            Die die = new Die("W1");

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();

            // Then the material and face are as expected
            assertEquals(Material.WOOD, actualMaterial);
            assertEquals(1, actualFace);
        }

        @Test
        public void for_recycled() {
            // Given a recycled die constructed from the text constructor
            Die die = new Die("R2");

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();

            // Then the material and face are as expected
            assertEquals(Material.RECYCLED, actualMaterial);
            assertEquals(2, actualFace);
        }

        @Test
        public void for_stone() {
            // Given a stone die constructed from the text constructor
            Die die = new Die("S4");

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();

            // Then the material and face are as expected
            assertEquals(Material.STONE, actualMaterial);
            assertEquals(4, actualFace);
        }
    }

    @Nested
    class TextConstructorTestsInvalidCases {
        @Test
        public void unknown_material_throws_InvalidDieMaterialException() {
            InvalidDieMaterialException ex = assertThrowsExactly(InvalidDieMaterialException.class, () -> {
                new Die("X4");
            });

            assertEquals("Invalid die material: X.", ex.getMessage());

        }

        @Test
        public void face_0_throws_InvalidDieFaceException() {
            InvalidDieFaceException ex = assertThrowsExactly(InvalidDieFaceException.class, () -> {
                new Die("W0");
            });

            assertEquals("Invalid die face: 0.", ex.getMessage());

        }

        @Test
        public void face_7_throws_InvalidDieFaceException() {
            InvalidDieFaceException ex = assertThrowsExactly(InvalidDieFaceException.class, () -> {
                new Die("W7");
            });

            assertEquals("Invalid die face: 7.", ex.getMessage());

        }

    }

    @Nested
    class MaterialFaceConstructorValidCases {
        @Test
        public void for_glass() {
            // Given a glass die constructed from the material/face constructor
            Die die = new Die(Material.GLASS, 6);

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();

            // Then the material and face are as expected
            assertEquals(Material.GLASS, actualMaterial);
            assertEquals(6, actualFace);
        }

        @Test
        public void for_wood() {
            // Given a wood die constructed from the material/face constructor
            Die die = new Die(Material.WOOD, 4);

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();

            // Then the material and face are as expected
            assertEquals(Material.WOOD, actualMaterial);
            assertEquals(4, actualFace);
        }

        @Test
        public void for_recycled() {
            // Given a recycled die constructed from the material/face constructor
            Die die = new Die(Material.RECYCLED, 3);

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();

            // Then the material and face are as expected
            assertEquals(Material.RECYCLED, actualMaterial);
            assertEquals(3, actualFace);
        }

        @Test
        public void for_stone() {
            // Given a stone die constructed from the material/face constructor
            Die die = new Die(Material.STONE, 1);

            // When the material and face are queried
            Material actualMaterial = die.getMaterial();
            int actualFace = die.getFace();

            // Then the material and face are as expected
            assertEquals(Material.STONE, actualMaterial);
            assertEquals(1, actualFace);
        }
    }

    @Nested
    class MaterialFaceConstructorTestsInvalidCases {
        @Test
        public void face_0_throws_InvalidDieFaceException() {
            assertThrowsExactly(InvalidDieFaceException.class, () -> {
                new Die(Material.GLASS, 0);
            });

        }

        @Test
        public void face_7_throws_InvalidDieFaceException() {
            assertThrowsExactly(InvalidDieFaceException.class, () -> {
                new Die(Material.WOOD, 7);
            });

        }

    }

    @Nested
    class ToStringTests {
        @ParameterizedTest
        @CsvSource({
                "G2",
                "W3",
                "S6",
                "R1"
        })
        public void constructed_with_text_constructor(String dieText) {
            // Given a die constructed from a text constructor
            Die die = new Die(dieText);

            // When the toString is called
            String actualToString = die.toString();

            // Then the result should be the same as the text used to construct the die
            assertEquals(dieText, actualToString);
        }

        @ParameterizedTest
        @CsvSource({
                "GLASS, 4",
                "WOOD, 3",
                "STONE, 2",
                "RECYCLED, 1"
        })
        public void constructed_with_material_face_constructor(String materialText, int face) {
            // Given a die constructed from a material/face constructor
            Die die = new Die(Material.valueOf(materialText), face);

            // When the toString is called
            String actualToString = die.toString();

            // Then the result should be the first letter of the material and the face
            String expectedToString = "" + materialText.charAt(0) + face;
            assertEquals(expectedToString, actualToString);
        }

    }

}
