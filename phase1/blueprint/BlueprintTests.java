package test.phase1.blueprint;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import main.blueprint.Blueprint;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class BlueprintTests {
        @Nested
        class HeightTargetTests {
                @ParameterizedTest
                @CsvSource({
                                "'X3 3X XX',0,3,3,0,0,0",
                                "'11 11 11',1,1,1,1,1,1",
                                "'1X X3 2X',1,0,0,3,2,0",
                                "'XX XX XX',0,0,0,0,0,0",
                })
                public void new_blueprint_has_expected_height_targets(String contructorText,
                                int expectedR1C1Target, int expectedR1C2Target,
                                int expectedR2C1Target, int expectedR2C2Target,
                                int expectedR3C1Target, int expectedR3C2Target) {

                        // Given a blueprint with height targets and prohibited spaces
                        Blueprint blueprint = new Blueprint(contructorText);

                        // When the height target of each space is found
                        int actualR1C1Target = blueprint.heightTargetAt(Space.from(Row.at(1), Col.at(1)));
                        int actualR1C2Target = blueprint.heightTargetAt(Space.from(Row.at(1), Col.at(2)));

                        int actualR2C1Target = blueprint.heightTargetAt(Space.from(Row.at(2), Col.at(1)));
                        int actualR2C2Target = blueprint.heightTargetAt(Space.from(Row.at(2), Col.at(2)));

                        int actualR3C1Target = blueprint.heightTargetAt(Space.from(Row.at(3), Col.at(1)));
                        int actualR3C2Target = blueprint.heightTargetAt(Space.from(Row.at(3), Col.at(2)));

                        // Then each target is as expected
                        assertEquals(expectedR1C1Target, actualR1C1Target);
                        assertEquals(expectedR1C2Target, actualR1C2Target);

                        assertEquals(expectedR2C1Target, actualR2C1Target);
                        assertEquals(expectedR2C2Target, actualR2C2Target);

                        assertEquals(expectedR3C1Target, actualR3C1Target);
                        assertEquals(expectedR3C2Target, actualR3C2Target);
                }
        }

        @Nested
        class IsOpenSpaceTests {
                @ParameterizedTest
                @CsvSource({
                                "'X3 3X XX',false,true,true,false,false,false",
                                "'11 11 11',true,true,true,true,true,true",
                                "'1X X3 2X',true,false,false,true,true,false",
                                "'XX XX XX',false,false,false,false,false,false",
                })
                public void says_spaces_with_positive_height_targets_are_open(String contructorText,
                                boolean expectedR1C1IsOpen, boolean expectedR1C2IsOpen,
                                boolean expectedR2C1IsOpen, boolean expectedR2C2IsOpen,
                                boolean expectedR3C1IsOpen, boolean expectedR3C2IsOpen) {

                        // Given a blueprint with height targets and prohibited spaces
                        Blueprint blueprint = new Blueprint(contructorText);

                        // When asked whether a given space is open
                        boolean actualR1C1IsOpen = blueprint.isOpenSpace(Space.from(Row.at(1), Col.at(1)));
                        boolean actualR1C2IsOpen = blueprint.isOpenSpace(Space.from(Row.at(1), Col.at(2)));

                        boolean actualR2C1IsOpen = blueprint.isOpenSpace(Space.from(Row.at(2), Col.at(1)));
                        boolean actualR2C2IsOpen = blueprint.isOpenSpace(Space.from(Row.at(2), Col.at(2)));

                        boolean actualR3C1IsOpen = blueprint.isOpenSpace(Space.from(Row.at(3), Col.at(1)));
                        boolean actualR3C2IsOpen = blueprint.isOpenSpace(Space.from(Row.at(3), Col.at(2)));

                        // Then each blueprint responds as expected
                        assertEquals(expectedR1C1IsOpen, actualR1C1IsOpen);
                        assertEquals(expectedR1C2IsOpen, actualR1C2IsOpen);

                        assertEquals(expectedR2C1IsOpen, actualR2C1IsOpen);
                        assertEquals(expectedR2C2IsOpen, actualR2C2IsOpen);

                        assertEquals(expectedR3C1IsOpen, actualR3C1IsOpen);
                        assertEquals(expectedR3C2IsOpen, actualR3C2IsOpen);
                }
        }

        @Nested
        class IsProhibitedSpaceTests {
                @ParameterizedTest
                @CsvSource({
                                "'X3 3X XX',true,false,false,true,true,true",
                                "'11 11 11',false,false,false,false,false,false",
                                "'1X X3 2X',false,true,true,false,false,true",
                                "'XX XX XX',true,true,true,true,true,true",
                })
                public void says_spaces_with_0_height_targets_are_prohibited(String contructorText,
                                boolean expectedR1C1IsProhibited, boolean expectedR1C2IsProhibited,
                                boolean expectedR2C1IsProhibited, boolean expectedR2C2IsProhibited,
                                boolean expectedR3C1IsProhibited, boolean expectedR3C2IsProhibited) {

                        // Given a blueprint with height targets and prohibited spaces
                        Blueprint blueprint = new Blueprint(contructorText);

                        // When asked whether a given space is prohibited
                        boolean actualR1C1IsProhibited = blueprint.isProhibitedSpace(Space.from(Row.at(1), Col.at(1)));
                        boolean actualR1C2IsProhibited = blueprint.isProhibitedSpace(Space.from(Row.at(1), Col.at(2)));

                        boolean actualR2C1IsProhibited = blueprint.isProhibitedSpace(Space.from(Row.at(2), Col.at(1)));
                        boolean actualR2C2IsProhibited = blueprint.isProhibitedSpace(Space.from(Row.at(2), Col.at(2)));

                        boolean actualR3C1IsProhibited = blueprint.isProhibitedSpace(Space.from(Row.at(3), Col.at(1)));
                        boolean actualR3C2IsProhibited = blueprint.isProhibitedSpace(Space.from(Row.at(3), Col.at(2)));

                        // Then each blueprint responds as expected
                        assertEquals(expectedR1C1IsProhibited, actualR1C1IsProhibited);
                        assertEquals(expectedR1C2IsProhibited, actualR1C2IsProhibited);

                        assertEquals(expectedR2C1IsProhibited, actualR2C1IsProhibited);
                        assertEquals(expectedR2C2IsProhibited, actualR2C2IsProhibited);

                        assertEquals(expectedR3C1IsProhibited, actualR3C1IsProhibited);
                        assertEquals(expectedR3C2IsProhibited, actualR3C2IsProhibited);
                }
        }

        @Nested
        class ToStringTests {
                @ParameterizedTest
                @CsvSource({
                                "'X3 3X XX','X3\n3X\nXX'",
                                "'11 11 11','11\n11\n11'",
                                "'1X X3 2X','1X\nX3\n2X'",
                                "'XX XX XX','XX\nXX\nXX'",
                })
                public void returns_expected_toTostring_value(String contructorText, String expectedToStringValue) {
                        // Given a blueprint
                        Blueprint blueprint = new Blueprint(contructorText);

                        // When asked for its toString value
                        String actualToStringValue = blueprint.toString();

                        // Then the value is as expected
                        assertEquals(expectedToStringValue, actualToStringValue);
                }
        }

}
