package de.uniks.stp24;

import de.uniks.stp24.model.game.Empire;
import de.uniks.stp24.model.game.Technology;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


public class TechnologyTest {
    
    // Setting and getting createdAt property

    @Test
    public void test_set_and_get_createdAt() {
        Technology technology = new Technology();
        LocalDateTime now = LocalDateTime.now();
        technology.setCreatedAt(now);
        assertEquals(now, technology.getCreatedAt());
    }

    // Setting createdAt to null

    @Test
    public void test_set_createdAt_to_null() {
        Technology technology = new Technology();
        technology.setCreatedAt(null);
        assertNull(technology.getCreatedAt());
    }

    // Setting and getting _id property
    @Test
    public void test_set_and_get_id_property() {
        Technology technology = new Technology();
        String id = "12345";
        technology.set_id(id);
        assertEquals(id, technology.get_id());
    }

    // Setting and getting updatedAt property
    @Test
    public void test_set_and_get_updatedAt() {
        Technology technology = new Technology();
        LocalDateTime now = LocalDateTime.now();
        technology.setUpdatedAt(now);
        assertEquals(now, technology.getUpdatedAt());
    }

    // Setting and getting cost property
    @Test
    public void test_set_and_get_cost() {
        Technology technology = new Technology();
        int cost = 100;
        technology.setCost(cost);
        assertEquals(cost, technology.getCost());
    }

    // Adding and removing requires items
    @Test
    public void test_add_and_remove_requires_items() {
        Technology technology = new Technology();
        String requiredItem1 = "Item1";
        String requiredItem2 = "Item2";

        // Adding requires items
        technology.withRequires(requiredItem1);
        technology.withRequires(requiredItem2);

        // Checking if items were added successfully
        assertTrue(technology.getRequires().contains(requiredItem1));
        assertTrue(technology.getRequires().contains(requiredItem2));

        // Removing requires items
        technology.withoutRequires(requiredItem1);

        // Checking if the first item was removed successfully
        assertFalse(technology.getRequires().contains(requiredItem1));
        assertTrue(technology.getRequires().contains(requiredItem2));
    }

    // Adding and removing precedes items
    @Test
    public void test_add_and_remove_precedes_items() {
        Technology technology = new Technology();
        String item1 = "Item1";
        String item2 = "Item2";

        // Adding precedes items
        technology.withPrecedes(item1);
        technology.withPrecedes(item2);
        assertEquals(2, technology.getPrecedes().size());

        // Removing precedes items
        technology.withoutPrecedes(item1);
        assertEquals(1, technology.getPrecedes().size());
    }

    // Adding and removing tags items
    @Test
    public void test_add_and_remove_tags() {
        Technology technology = new Technology();
        String tag1 = "Tag1";
        String tag2 = "Tag2";

        // Adding tags
        technology.withTags(tag1);
        technology.withTags(tag2);
        assertEquals(2, technology.getTags().size());
        assertTrue(technology.getTags().contains(tag1));
        assertTrue(technology.getTags().contains(tag2));

        // Removing tags
        technology.withoutTags(tag1);
        assertFalse(technology.getTags().contains(tag1));
        assertEquals(1, technology.getTags().size());

        technology.withoutTags(tag2);
        assertFalse(technology.getTags().contains(tag2));
        assertEquals(0, technology.getTags().size());
    }

    // Setting and getting empire property
    @Test
    public void test_set_and_get_empire_property() {
        Technology technology = new Technology();
        Empire empire = new Empire();
    
        technology.setEmpire(empire);
        assertEquals(empire, technology.getEmpire());
    }

    // Converting Technology object to string representation
    @Test
    public void test_technology_toString_representation() {
        Technology technology = new Technology();
        technology.set_id("123");
        technology.withRequires("Resource A", "Resource B");
        technology.withPrecedes("Tech X", "Tech Y");
        technology.withTags("Tag1", "Tag2");

        String expected = "123 [Resource A, Resource B] [Tech X, Tech Y] [Tag1, Tag2]";
        assertEquals(expected, technology.toString());
    }

    // Setting updatedAt to null
    @Test
    public void test_set_updatedAt_to_null() {
        Technology technology = new Technology();
        LocalDateTime updatedAt = LocalDateTime.now();
        technology.setUpdatedAt(updatedAt);
        technology.setUpdatedAt(null);
        assertNull(technology.getUpdatedAt());
    }

    // Setting _id to an empty string
    @Test
    public void test_set_id_empty_string() {
        Technology technology = new Technology();
        technology.set_id("12345");
        technology.set_id("");
        assertEquals("", technology.get_id());
    }

    // Setting cost to a negative value
    @Test
    public void test_set_negative_cost() {
        // Create a new instance of Technology
        Technology technology = new Technology();

        // Set a negative cost
        int negativeCost = -10;
        technology.setCost(negativeCost);

        // Assert that the cost is set correctly
        assertEquals(negativeCost, technology.getCost());
    }

    // Adding duplicate items to requires
    @Test
    public void test_add_duplicate_items_to_requires() {
        Technology technology = new Technology();
        String item = "item";
        technology.withRequires(item);
        technology.withRequires(item);
        assertEquals(2, technology.getRequires().size());
    }

    // Adding duplicate items to tags
    @Test
    public void test_add_duplicate_items_to_tags() {
        Technology technology = new Technology();
        String tag = "tag1";
        technology.withTags(tag);
        technology.withTags(tag);
        assertEquals(2, technology.getTags().size());
    }

    // Adding duplicate items to precedes
    @Test
    public void test_add_duplicate_items_to_precedes() {
        Technology technology = new Technology();
        String item = "Item1";
        technology.withPrecedes(item);
        technology.withPrecedes(item);
        assertEquals(2, technology.getPrecedes().size());
    }

    // Removing non-existent items from requires
    @Test
    public void test_removing_non_existent_items_from_requires() {
        // Create a new Technology instance
        Technology technology = new Technology();

        // Add some items to the requires list
        technology.withRequires("item1", "item2", "item3");

        // Remove a non-existent item
        technology.withoutRequires("non_existent_item");

        // Check if the non-existent item was not removed
        assertFalse(technology.getRequires().contains("non_existent_item"));
    }

    // Removing non-existent items from precedes
    @Test
    public void test_remove_non_existent_items_from_precedes() {
        // Create a Technology instance
        Technology technology = new Technology();

        // Add some items to the 'precedes' list
        technology.withPrecedes("item1", "item2", "item3");

        // Remove a non-existent item from 'precedes'
        technology.withoutPrecedes("non_existent_item");

        // Check if the non-existent item was not removed
        assertEquals(3, technology.getPrecedes().size());
    }

    // Removing non-existent items from tags
    @Test
    public void test_remove_non_existent_tags() {
        Technology technology = new Technology();
        technology.withTags("tag1", "tag2", "tag3");

        // Remove a tag that doesn't exist
        technology.withoutTags("tag4");

        // Check if the tags remain unchanged
        assertEquals(3, technology.getTags().size());
    }
}