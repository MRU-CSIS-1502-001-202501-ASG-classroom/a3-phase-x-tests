package test.phase1.blueprint;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import main.blueprint.Blueprint;
import main.blueprint.BlueprintFileReader;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class BlueprintFileReaderTests {
        private static final String ANY_BLUEPRINT_PATH = "blueprint.txt";
        private Path tempBlueprintPath;

        @TempDir
        private Path blueprintTempDir;

        @BeforeEach
        public void setUp() throws Exception {
                tempBlueprintPath = blueprintTempDir.resolve(ANY_BLUEPRINT_PATH);

                String blueprintFileContents = "";
                blueprintFileContents += String.format("X2%n");
                blueprintFileContents += String.format("X1%n");
                blueprintFileContents += String.format("3X%n");

                Files.write(tempBlueprintPath, blueprintFileContents.getBytes());
        }

        @Test
        public void load_returns_expected_blueprint() throws Exception {

                // Given the path to a blueprint file
                String pathToBlueprint = tempBlueprintPath.toString();

                // When an attempt is made to load that blueprint
                Blueprint blueprint = BlueprintFileReader.load(pathToBlueprint);

                // Then the expected blueprint is returned
                assertEquals(0, blueprint.heightTargetAt(Space.from(Row.at(1), Col.at(1))));
                assertEquals(2, blueprint.heightTargetAt(Space.from(Row.at(1), Col.at(2))));

                assertEquals(0, blueprint.heightTargetAt(Space.from(Row.at(2), Col.at(1))));
                assertEquals(1, blueprint.heightTargetAt(Space.from(Row.at(2), Col.at(2))));

                assertEquals(3, blueprint.heightTargetAt(Space.from(Row.at(3), Col.at(1))));
                assertEquals(0, blueprint.heightTargetAt(Space.from(Row.at(3), Col.at(2))));
        }

}
