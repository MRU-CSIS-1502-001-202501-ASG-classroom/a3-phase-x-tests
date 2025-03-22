package test.phase2.building;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import main.building.Building;
import main.building.BuildingFileReader;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class BuildingFileReaderTests {
        private static final String ANY_BUILDING_PATH = "building.txt";
        private Path tempBuildingPath;

        @TempDir
        private Path buildingTempDir;

        @BeforeEach
        public void setUp() throws Exception {
                tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);

                String blueprintFileContents = "";
                blueprintFileContents += String.format("X2%n");
                blueprintFileContents += String.format("X1%n");
                blueprintFileContents += String.format("3X%n");

                Files.write(tempBuildingPath, blueprintFileContents.getBytes());
        }

        @Test
        public void example_1_from_instructions() throws Exception {
                // Given the path to a building file
                Path tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);
                String pathToBuilding = tempBuildingPath.toString();

                // And given the building file contents from example 1
                String buildingFileContents = "";
                buildingFileContents += String.format("[row.1]%n");
                buildingFileContents += String.format("col.2 = \"S2 G6\"%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.2]%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.3]%n");
                buildingFileContents += String.format("col.1 = \"R1 R2 W3 S3\"%n");

                Files.write(tempBuildingPath, buildingFileContents.getBytes());

                // When an attempt is made to load that building
                Building building = BuildingFileReader.load(pathToBuilding);

                // Then the expected building is returned
                assertEquals(4, building.getHeight());
                assertEquals(6, building.getNumDice());
                assertTrue(building.isValid());

                // Stack at row 1, col 2
                assertEquals("S2", building.getDie(Space.from(Row.at(1), Col.at(2)), 1).toString());
                assertEquals("G6", building.getDie(Space.from(Row.at(1), Col.at(2)), 2).toString());

                // Stack at row 3, col 1
                assertEquals("R1", building.getDie(Space.from(Row.at(3), Col.at(1)), 1).toString());
                assertEquals("R2", building.getDie(Space.from(Row.at(3), Col.at(1)), 2).toString());
                assertEquals("W3", building.getDie(Space.from(Row.at(3), Col.at(1)), 3).toString());
                assertEquals("S3", building.getDie(Space.from(Row.at(3), Col.at(1)), 4).toString());
        }

        @Test
        public void example_2_from_instructions() throws Exception {
                // Given the path to a building file
                Path tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);
                String pathToBuilding = tempBuildingPath.toString();

                // And given the building file contents from example 2
                String buildingFileContents = "";
                buildingFileContents += String.format("[row.1]%n");
                buildingFileContents += String.format("col.1 = \"R2 G4\"%n");
                buildingFileContents += String.format("col.2 = \"W3\"%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.2]%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.3]%n");
                buildingFileContents += String.format("col.1 = \"S1\"%n");

                Files.write(tempBuildingPath, buildingFileContents.getBytes());

                // When an attempt is made to load that building
                Building building = BuildingFileReader.load(pathToBuilding);

                // Then the expected building is returned
                assertEquals(2, building.getHeight());
                assertEquals(4, building.getNumDice());
                assertTrue(building.isValid());

                // Stack at row 1, col 1
                assertEquals("R2", building.getDie(Space.from(Row.at(1), Col.at(1)), 1).toString());
                assertEquals("G4", building.getDie(Space.from(Row.at(1), Col.at(1)), 2).toString());

                // Stack at row 1, col 2
                assertEquals("W3", building.getDie(Space.from(Row.at(1), Col.at(2)), 1).toString());

                // Stack at row 3, col 1
                assertEquals("S1", building.getDie(Space.from(Row.at(3), Col.at(1)), 1).toString());
        }

        @Test
        public void example_3_from_instructions() throws Exception {
                // Given the path to a building file
                Path tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);
                String pathToBuilding = tempBuildingPath.toString();

                // And given the building file contents from example 3
                String buildingFileContents = "";
                buildingFileContents += String.format("[row.1]%n");
                buildingFileContents += String.format("col.1 = \"R4\"%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.2]%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.3]%n");

                Files.write(tempBuildingPath, buildingFileContents.getBytes());

                // When an attempt is made to load that building
                Building building = BuildingFileReader.load(pathToBuilding);

                // Then the expected building is returned
                assertEquals(1, building.getHeight());
                assertEquals(1, building.getNumDice());
                assertTrue(building.isValid());

                // Stack at row 1, col 1
                assertEquals("R4", building.getDie(Space.from(Row.at(1), Col.at(1)), 1).toString());
        }

        @Test
        public void example_4_from_instructions() throws Exception {
                // Given the path to a building file
                Path tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);
                String pathToBuilding = tempBuildingPath.toString();

                // And given the building file contents from example 4
                String buildingFileContents = "";
                buildingFileContents += String.format("[row.1]%n");
                buildingFileContents += String.format("col.1 = \"R5\"%n");
                buildingFileContents += String.format("col.2 = \"S4 G6\"%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.2]%n");
                buildingFileContents += String.format("col.1 = \"W6\"%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.3]%n");
                buildingFileContents += String.format("col.1 = \"R1 S2\"%n");

                Files.write(tempBuildingPath, buildingFileContents.getBytes());

                // When an attempt is made to load that building
                Building building = BuildingFileReader.load(pathToBuilding);

                // Then the expected building is returned
                assertEquals(2, building.getHeight());
                assertEquals(6, building.getNumDice());
                assertTrue(building.isValid());

                // Stack at row 1, col 1
                assertEquals("R5", building.getDie(Space.from(Row.at(1), Col.at(1)), 1).toString());

                // Stack at row 1, col 2
                assertEquals("S4", building.getDie(Space.from(Row.at(1), Col.at(2)), 1).toString());
                assertEquals("G6", building.getDie(Space.from(Row.at(1), Col.at(2)), 2).toString());

                // Stack at row 2, col 1
                assertEquals("W6", building.getDie(Space.from(Row.at(2), Col.at(1)), 1).toString());

                // Stack at row 3, col 1
                assertEquals("R1", building.getDie(Space.from(Row.at(3), Col.at(1)), 1).toString());
                assertEquals("S2", building.getDie(Space.from(Row.at(3), Col.at(1)), 2).toString());
        }

        @Test
        public void example_5_from_instructions() throws Exception {
                // Given the path to a building file
                Path tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);
                String pathToBuilding = tempBuildingPath.toString();

                // And given the building file contents from example 5
                String buildingFileContents = "";
                buildingFileContents += String.format("[row.1]%n");
                buildingFileContents += String.format("col.1 = \"R6 W1\"%n");
                buildingFileContents += String.format("col.2 = \"S2 S5\"%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.2]%n");
                buildingFileContents += String.format("col.1 = \"G5 G4\"%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.3]%n");
                buildingFileContents += String.format("col.1 = \"W2 W3\"%n");

                Files.write(tempBuildingPath, buildingFileContents.getBytes());

                // When an attempt is made to load that building
                Building building = BuildingFileReader.load(pathToBuilding);

                // Then the expected building is returned
                assertEquals(2, building.getHeight());
                assertEquals(8, building.getNumDice());
                assertFalse(building.isValid());

                // Stack at row 1, col 1
                assertEquals("R6", building.getDie(Space.from(Row.at(1), Col.at(1)), 1).toString());
                assertEquals("W1", building.getDie(Space.from(Row.at(1), Col.at(1)), 2).toString());

                // Stack at row 1, col 2
                assertEquals("S2", building.getDie(Space.from(Row.at(1), Col.at(2)), 1).toString());
                assertEquals("S5", building.getDie(Space.from(Row.at(1), Col.at(2)), 2).toString());

                // Stack at row 2, col 1
                assertEquals("G5", building.getDie(Space.from(Row.at(2), Col.at(1)), 1).toString());
                assertEquals("G4", building.getDie(Space.from(Row.at(2), Col.at(1)), 2).toString());

                // Stack at row 3, col 1
                assertEquals("W2", building.getDie(Space.from(Row.at(3), Col.at(1)), 1).toString());
                assertEquals("W3", building.getDie(Space.from(Row.at(3), Col.at(1)), 2).toString());
        }

        @Test
        public void example_6_from_instructions() throws Exception {
                // Given the path to a building file
                Path tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);
                String pathToBuilding = tempBuildingPath.toString();

                // And given the building file contents from example 6
                String buildingFileContents = "";
                buildingFileContents += String.format("[row.1]%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.2]%n");
                buildingFileContents += String.format("%n");
                buildingFileContents += String.format("[row.3]%n");

                Files.write(tempBuildingPath, buildingFileContents.getBytes());

                // When an attempt is made to load that building
                Building building = BuildingFileReader.load(pathToBuilding);

                // Then the expected building is returned
                assertEquals(0, building.getHeight());
                assertEquals(0, building.getNumDice());
                assertTrue(building.isValid());
        }

}
