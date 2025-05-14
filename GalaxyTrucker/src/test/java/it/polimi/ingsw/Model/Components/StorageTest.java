package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    // Test constants
    private static final SideType[] TEST_SIDES = {
            SideType.SMOOTH,
            SideType.DOUBLE,
            SideType.SINGLE,
            SideType.UNIVERSAL
    };

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange & Act
        Storage storage = new Storage(TEST_SIDES, true, 3);

        // Assert
        assertArrayEquals(TEST_SIDES, new SideType[]{
                storage.getFront(),
                storage.getRight(),
                storage.getBack(),
                storage.getLeft()
        });
        assertTrue(storage.isRed());
        assertEquals(3, storage.getNumberOfMaximumElements());
        assertEquals("Storage", storage.getComponentName());
        assertArrayEquals(new int[]{0, 0, 0, 0}, storage.getGoods());
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        Storage storage = new Storage();

        // Assert
        assertNotNull(storage);
        assertFalse(storage.isRed()); // Default boolean is false
        assertEquals(0, storage.getNumberOfMaximumElements());
        assertArrayEquals(new int[]{0, 0, 0, 0}, storage.getGoods());
    }

    @Test
    void isEmpty_whenEmpty_shouldReturnTrue() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, true, 2);

        // Act & Assert
        assertTrue(storage.isEmpty());
    }

    @Test
    void isEmpty_whenNotEmpty_shouldReturnFalse() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, true, 2);
        storage.addGoods(new int[]{1, 0, 0, 0});

        // Act & Assert
        assertFalse(storage.isEmpty());
    }

    @Test
    void isFull_whenFull_shouldReturnTrue() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, true, 2);
        storage.addGoods(new int[]{1, 1, 0, 0});

        // Act & Assert
        assertTrue(storage.isFull());
    }

    @Test
    void isFull_whenNotFull_shouldReturnFalse() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, true, 3);
        storage.addGoods(new int[]{1, 0, 0, 0});

        // Act & Assert
        assertFalse(storage.isFull());
    }

    @Test
    void addGoods_shouldIncreaseGoodsCount() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, true, 5);
        int[] toAdd = {1, 2, 0, 1};

        // Act
        storage.addGoods(toAdd);

        // Assert
        assertArrayEquals(new int[]{1, 2, 0, 1}, storage.getGoods());
    }

    @Test
    void removeGoods_shouldDecreaseGoodsCount() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, true, 5);
        storage.addGoods(new int[]{2, 3, 1, 2});
        int[] toRemove = {1, 1, 0, 1};

        // Act
        storage.removeGoods(toRemove);

        // Assert
        assertArrayEquals(new int[]{1, 2, 1, 1}, storage.getGoods());
    }

    @Test
    void getAvailableRedSlots_shouldReturnCorrectValue() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, true, 5);
        storage.addGoods(new int[]{1, 0, 1, 0});

        // Act & Assert
        assertEquals(3, storage.getAvailableRedSlots());
    }

    @Test
    void getAvailableBlueSlots_shouldReturnCorrectValue() {
        // Arrange
        Storage storage = new Storage(TEST_SIDES, false, 5);
        storage.addGoods(new int[]{0, 1, 1, 0});

        // Act & Assert
        assertEquals(3, storage.getAvailableBlueSlots());
    }

    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Storage",
                    "isRed": true,
                    "numberOfMaximumElements": 3,
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        Storage storage = mapper.readValue(json, Storage.class);

        // Assert
        assertTrue(storage.isRed());
        assertEquals(3, storage.getNumberOfMaximumElements());
        assertArrayEquals(TEST_SIDES, new SideType[]{
                storage.getFront(),
                storage.getRight(),
                storage.getBack(),
                storage.getLeft()
        });
    }

    @Test
    void jsonDeserialization_withMissingFields_shouldUseDefaults() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Storage",
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        Storage storage = mapper.readValue(json, Storage.class);

        // Assert
        assertFalse(storage.isRed()); // Default false
        assertEquals(0, storage.getNumberOfMaximumElements()); // Default 0
    }
}
