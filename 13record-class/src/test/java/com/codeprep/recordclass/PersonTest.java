package com.codeprep.recordclass;

import com.codeprep.Person;

/**
 * Test class demonstrating the use of Record classes in Java
 * This test showcases the automatic features provided by records:
 * - Automatic constructor
 * - Automatic getters (accessor methods)
 * - Automatic equals(), hashCode(), and toString()
 * - Compact constructor validation
 * - Custom methods
 */
public class PersonTest {

    /**
     * Test 1: Automatic constructor and accessor methods
     * Records automatically generate a canonical constructor and getter methods
     */
    public void testRecordConstructorAndAccessors() {
        System.out.println("\n=== Test 1: Constructor and Accessors ===");

        // Create a Person record using the automatic constructor
        Person person = new Person("Alice", 30, "alice@example.com");

        // Access fields using automatic accessor methods
        // Note: Records provide accessor methods with the same name as the field (not getX())
        assert person.name().equals("Alice") : "Name should be Alice";
        assert person.age() == 30 : "Age should be 30";
        assert person.email().equals("alice@example.com") : "Email should be alice@example.com";

        System.out.println("✓ Constructor and accessors work correctly");
        System.out.println("  Person: " + person.name() + ", " + person.age() + ", " + person.email());
    }

    /**
     * Test 2: Automatic toString() method
     * Records automatically generate a meaningful toString() representation
     */
    public void testRecordToString() {
        System.out.println("\n=== Test 2: Automatic toString() ===");

        Person person = new Person("Bob", 25, "bob@example.com");
        String personString = person.toString();

        // The automatic toString() format is: ClassName[field1=value1, field2=value2, ...]
        System.out.println("toString() output: " + personString);
        assert personString.contains("Bob") : "toString should contain name";
        assert personString.contains("25") : "toString should contain age";
        assert personString.contains("bob@example.com") : "toString should contain email";

        System.out.println("✓ toString() works correctly");
    }

    /**
     * Test 3: Automatic equals() and hashCode() methods
     * Records automatically implement equals() and hashCode() based on all fields
     */
    public void testRecordEqualsAndHashCode() {
        System.out.println("\n=== Test 3: Automatic equals() and hashCode() ===");

        Person person1 = new Person("Charlie", 35, "charlie@example.com");
        Person person2 = new Person("Charlie", 35, "charlie@example.com");
        Person person3 = new Person("David", 35, "david@example.com");

        // Test equals()
        assert person1.equals(person2) : "Persons with same values should be equal";
        assert !person1.equals(person3) : "Persons with different values should not be equal";
        assert person1.equals(person1) : "Person should equal itself";
        assert !person1.equals(null) : "Person should not equal null";

        // Test hashCode()
        assert person1.hashCode() == person2.hashCode() : "Equal objects should have same hashCode";
        System.out.println("✓ equals() and hashCode() work correctly");
        System.out.println("  person1.hashCode(): " + person1.hashCode());
        System.out.println("  person2.hashCode(): " + person2.hashCode());
    }

    /**
     * Test 4: Immutability of record fields
     * All fields in a record are implicitly final and cannot be modified
     */
    public void testRecordImmutability() {
        System.out.println("\n=== Test 4: Record Immutability ===");

        Person person = new Person("Eve", 28, "eve@example.com");

        // Records are immutable - fields cannot be changed after creation
        // This ensures thread-safety and predictable behavior
        // The following would cause a compilation error:
        // person.name = "NewName";  // Error: Cannot assign a value to final variable

        System.out.println("✓ Record fields are immutable (final)");
        System.out.println("  All fields are automatically final and cannot be modified");
    }

    /**
     * Test 5: Compact constructor validation
     * The Person record has a compact constructor that validates input
     */
    public void testCompactConstructorValidation() {
        System.out.println("\n=== Test 5: Compact Constructor Validation ===");

        // Valid person should be created successfully
        try {
            Person validPerson = new Person("Frank", 40, "frank@example.com");
            System.out.println("✓ Valid person created: " + validPerson.name());
        } catch (IllegalArgumentException e) {
            assert false : "Valid person should not throw exception";
        }

        // Test null name validation
        try {
            Person invalidPerson = new Person(null, 30, "test@example.com");
            assert false : "Should throw exception for null name";
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Null name validation works: " + e.getMessage());
        }

        // Test blank name validation
        try {
            Person invalidPerson = new Person("   ", 30, "test@example.com");
            assert false : "Should throw exception for blank name";
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Blank name validation works: " + e.getMessage());
        }

        // Test negative age validation
        try {
            Person invalidPerson = new Person("Grace", -5, "grace@example.com");
            assert false : "Should throw exception for negative age";
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Negative age validation works: " + e.getMessage());
        }
    }

    /**
     * Test 6: Custom methods in records
     * Records can have custom methods just like regular classes
     */
    public void testCustomMethods() {
        System.out.println("\n=== Test 6: Custom Methods ===");

        Person person = new Person("Henry", 45, "henry@example.com");
        String displayName = person.getDisplayName();

        assert displayName.equals("Henry (45 years old)") : "Display name should match expected format";
        System.out.println("✓ Custom method works correctly");
        System.out.println("  Display name: " + displayName);
    }

    /**
     * Test 7: Custom constructor
     * Records can have multiple constructors that delegate to the canonical constructor
     */
    public void testCustomConstructor() {
        System.out.println("\n=== Test 7: Custom Constructor ===");

        // Use custom constructor that generates default email
        Person person = new Person("John Smith", 42);

        // Verify the person was created with generated email
        assert person.name().equals("John Smith") : "Name should be John Smith";
        assert person.age() == 42 : "Age should be 42";
        assert person.email().equals("john.smith@example.com") : "Email should be generated from name";

        System.out.println("✓ Custom constructor works correctly");
        System.out.println("  Person: " + person.name());
        System.out.println("  Generated email: " + person.email());

        // Test with different names to show email generation
        Person person2 = new Person("Alice Wonder", 28);
        assert person2.email().equals("alice.wonder@example.com") : "Email should be alice.wonder@example.com";
        System.out.println("  Person2: " + person2.name() + " -> " + person2.email());
    }

    /**
     * Test 8: Records as DTOs (Data Transfer Objects)
     * Common use case: passing data between layers or systems
     */
    public void testRecordAsDTO() {
        System.out.println("\n=== Test 8: Record as DTO ===");

        // Simulate creating a DTO from user input
        Person userDTO = new Person("Isabella", 32, "isabella@example.com");

        // Simulate sending the DTO to another layer (e.g., service layer)
        Person processedDTO = processUserData(userDTO);

        assert processedDTO.equals(userDTO) : "DTOs should be equal";
        System.out.println("✓ Record works well as a DTO");
        System.out.println("  Original DTO: " + userDTO);
        System.out.println("  Processed DTO: " + processedDTO);
    }

    // Helper method to simulate data processing
    private Person processUserData(Person user) {
        // In a real application, this might save to database, send to API, etc.
        return new Person(user.name(), user.age(), user.email());
    }

    /**
     * Test 9: Using records in collections
     * Records work seamlessly in collections due to proper equals() and hashCode()
     */
    public void testRecordInCollections() {
        System.out.println("\n=== Test 9: Record in Collections ===");

        Person person1 = new Person("Jack", 50, "jack@example.com");
        Person person2 = new Person("Kelly", 27, "kelly@example.com");
        Person person3 = new Person("Jack", 50, "jack@example.com"); // duplicate

        java.util.Set<Person> personSet = new java.util.HashSet<>();
        personSet.add(person1);
        personSet.add(person2);
        personSet.add(person3); // Should not be added (duplicate)

        assert personSet.size() == 2 : "Set should contain only 2 unique persons";
        assert personSet.contains(person1) : "Set should contain person1";
        assert personSet.contains(person2) : "Set should contain person2";

        System.out.println("✓ Record works correctly in collections");
        System.out.println("  Set size: " + personSet.size() + " (duplicates automatically handled)");
    }

    /**
     * Main method to run all tests
     */
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║          Record Class Test Suite - Demonstration             ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        PersonTest test = new PersonTest();

        try {
            test.testRecordConstructorAndAccessors();
            test.testRecordToString();
            test.testRecordEqualsAndHashCode();
            test.testRecordImmutability();
            test.testCompactConstructorValidation();
            test.testCustomMethods();
            test.testCustomConstructor();
            test.testRecordAsDTO();
            test.testRecordInCollections();

            System.out.println("\n╔═══════════════════════════════════════════════════════════════╗");
            System.out.println("║                  ✓ All Tests Passed!                         ║");
            System.out.println("╚═══════════════════════════════════════════════════════════════╝");

        } catch (AssertionError e) {
            System.err.println("\n✗ Test Failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println("\n✗ Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

