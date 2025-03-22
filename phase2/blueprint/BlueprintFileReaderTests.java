package test.phase2.blueprint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import main.blueprint.Blueprint;
import main.blueprint.BlueprintFileReader;
import main.exceptions.runtime.InvalidBlueprintTemplateException;
import main.space.Col;
import main.space.Row;
import main.space.Space;

public class BlueprintFileReaderTests {
        private static final String ANY_BLUEPRINT_PATH = "blueprint.txt";

        @TempDir
        private Path blueprintTempDir;

        @Test
        public void load_returns_expected_blueprint_when_file_contents_valid() throws Exception {

                Path tempBlueprintPath = blueprintTempDir.resolve(ANY_BLUEPRINT_PATH);

                String blueprintFileContents = "";
                blueprintFileContents += String.format("X2%n");
                blueprintFileContents += String.format("X1%n");
                blueprintFileContents += String.format("3X%n");

                Files.write(tempBlueprintPath, blueprintFileContents.getBytes());

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

        @Test
        public void load_throws_InvalidBlueprintTemplateException_when_file_contents_have_invalid_height_targets()
                        throws IOException {

                Path tempBlueprintPath = blueprintTempDir.resolve(ANY_BLUEPRINT_PATH);

                String blueprintFileContents = "";
                blueprintFileContents += String.format("X2%n");
                blueprintFileContents += String.format("X0%n"); // invalid target 0
                blueprintFileContents += String.format("3X%n");

                Files.write(tempBlueprintPath, blueprintFileContents.getBytes());

                // Given the path to a blueprint file
                String pathToBlueprint = tempBlueprintPath.toString();

                // When an attempt is made to load that blueprint
                InvalidBlueprintTemplateException ex = assertThrowsExactly(InvalidBlueprintTemplateException.class,
                                () -> {
                                        BlueprintFileReader.load(pathToBlueprint);
                                });

                assertEquals("Invalid blueprint template used.", ex.getMessage());
        }

        @Test
        public void load_throws_InvalidBlueprintTemplateException_when_file_contents_have_non_X_characters()
                        throws IOException {

                Path tempBlueprintPath = blueprintTempDir.resolve(ANY_BLUEPRINT_PATH);

                String blueprintFileContents = "";
                blueprintFileContents += String.format("X2%n");
                blueprintFileContents += String.format("X1%n");
                blueprintFileContents += String.format("3Y%n"); // invalid character Y

                Files.write(tempBlueprintPath, blueprintFileContents.getBytes());

                // Given the path to a blueprint file
                String pathToBlueprint = tempBlueprintPath.toString();

                // When an attempt is made to load that blueprint
                InvalidBlueprintTemplateException ex = assertThrowsExactly(InvalidBlueprintTemplateException.class,
                                () -> {
                                        BlueprintFileReader.load(pathToBlueprint);
                                });

                assertEquals("Invalid blueprint template used.", ex.getMessage());
        }

}
