package test.phase3.building;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import main.building.Building;
import main.building.BuildingFileReader;
import main.exceptions.checked.InvalidBuildingException;
import main.logging.SimpleLogger;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class BuildingFileReaderTests {
    private static final String ANY_BUILDING_PATH = "building.txt";
    private Path tempBuildingPath;
    private SimpleLogger logger;

    @TempDir
    private Path buildingTempDir;

    @BeforeEach
    public void setUp() throws IOException {
        tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);

        String blueprintFileContents = "";
        blueprintFileContents += String.format("X2%n");
        blueprintFileContents += String.format("X1%n");
        blueprintFileContents += String.format("3X%n");

        Files.write(tempBuildingPath, blueprintFileContents.getBytes());

        logger = new SimpleLogger();
    }

    @Test
    public void example_1_from_instructions() throws IOException {
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

        try {
            Building building = BuildingFileReader.load(pathToBuilding, logger);

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

            // Should be no issues with the building in the file.
            assertTrue(logger.getMessages().isEmpty());
        } catch (InvalidBuildingException ex) {
            fail("Loading building failed, but shouldn't have.");
        }

    }

    @Test
    public void example_2_from_instructions() throws IOException {
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
        try {
            Building building = BuildingFileReader.load(pathToBuilding, logger);

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

            // Should be no issues with the building in the file.
            assertTrue(logger.getMessages().isEmpty());
        } catch (InvalidBuildingException ex) {
            fail("Loading building failed, but shouldn't have.");
        }

    }

    @Test
    public void example_3_from_instructions() throws IOException {
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
        try {
            Building building = BuildingFileReader.load(pathToBuilding, logger);

            // Then the expected building is returned
            assertEquals(1, building.getHeight());
            assertEquals(1, building.getNumDice());
            assertTrue(building.isValid());

            // Stack at row 1, col 1
            assertEquals("R4", building.getDie(Space.from(Row.at(1), Col.at(1)), 1).toString());

            // Should be no issues with the building in the file.
            assertTrue(logger.getMessages().isEmpty());
        } catch (InvalidBuildingException ex) {
            fail("Loading building failed, but shouldn't have.");
        }

    }

    @Test
    public void example_4_from_instructions() throws IOException {
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
        try {
            Building building = BuildingFileReader.load(pathToBuilding, logger);

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

            // Should be no issues with the building in the file.
            assertTrue(logger.getMessages().isEmpty());
        } catch (InvalidBuildingException ex) {
            fail("Loading building failed, but shouldn't have.");
        }

    }

    @Test
    public void example_5_from_instructions() throws IOException {
        // Given the path to a building file
        Path tempBuildingPath = buildingTempDir.resolve(ANY_BUILDING_PATH);
        String pathToBuilding = tempBuildingPath.toString();

        // And given the building file contents from example 5
        String buildingFileContents = "";
        buildingFileContents += String.format("[row.1]%n");
        buildingFileContents += String.format("col.1 = \"R6 W1\"%n"); // Descending dice!
        buildingFileContents += String.format("col.2 = \"S2 S5\"%n");
        buildingFileContents += String.format("%n");
        buildingFileContents += String.format("[row.2]%n");
        buildingFileContents += String.format("col.1 = \"G5 G4\"%n"); // More descending dice!
        buildingFileContents += String.format("%n");
        buildingFileContents += String.format("[row.3]%n");
        buildingFileContents += String.format("col.1 = \"W2 W3\"%n"); // Too many dice!

        Files.write(tempBuildingPath, buildingFileContents.getBytes());
        try {
            // When an attempt is made to load that building
            BuildingFileReader.load(pathToBuilding, logger);

            // If the catch doesn't trigger, something is wrong...the building is invalid!
            fail("The building is invalid, so the catch should have happened.");

        } catch (InvalidBuildingException ex) {
            // These errors should have been logged during the load process.
            List<String> expectedMessages = List.of(
                    "Error when adding W1 at [1,1] to level 2. Building has these violations: [DESCENDING_DICE].",
                    "Error when adding S2 at [1,2] to level 1. Building has these violations: [DESCENDING_DICE].",
                    "Error when adding S5 at [1,2] to level 2. Building has these violations: [DESCENDING_DICE].",
                    "Error when adding G5 at [2,1] to level 1. Building has these violations: [DESCENDING_DICE].",
                    "Error when adding G4 at [2,1] to level 2. Building has these violations: [DESCENDING_DICE].",
                    "Error when adding W2 at [3,1] to level 1. Building has these violations: [BUILDING_OVERLARGE, DESCENDING_DICE].",
                    "Error when adding W3 at [3,1] to level 2. Building has these violations: [BUILDING_OVERLARGE, DESCENDING_DICE].");

            assertIterableEquals(expectedMessages, logger.getMessages());
        }

    }

    @Test
    public void example_6_from_instructions() throws IOException {
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
        try {
            Building building = BuildingFileReader.load(pathToBuilding, logger);
            // Then the expected building is returned
            assertEquals(0, building.getHeight());
            assertEquals(0, building.getNumDice());
            assertTrue(building.isValid());

            // Should be no issues with the building in the file.
            assertTrue(logger.getMessages().isEmpty());
        } catch (InvalidBuildingException ex) {
            fail("Loading building failed, but shouldn't have.");
        }

    }

}
