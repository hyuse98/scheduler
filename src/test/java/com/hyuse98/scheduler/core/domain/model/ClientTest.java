package com.hyuse98.scheduler.core.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private UUID id;
    private Date createdAt;
    private Date birthday;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        createdAt = new Date();
        birthday = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 25);
    }

    @Test
    void shouldCreateClientWithDefaultActiveTrue() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, null);

        assertNotNull(client);
        assertTrue(client.getActive(), "Cliente criado com isActive=null deve ser ativo por padrão");
    }

    @Test
    void shouldCreateClientWithActiveFalse() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, false);

        assertFalse(client.getActive());
    }

    @Test
    void shouldReconstituteClientSuccessfully() {
        Client client = Client.reconstitute(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        assertNotNull(client);
        assertEquals(id, client.getId());
        assertEquals("email@test.com", client.getEmail());
        assertEquals("John Doe", client.getName());
        assertEquals("11999999999", client.getPhoneNumber());
        assertEquals(birthday, client.getBirthday());
        assertEquals("Rua A, 123", client.getAddress());
        assertEquals("123456789012345", client.getCns());
        assertEquals(createdAt, client.getCreatedAt());
        assertTrue(client.getActive());
    }

    @Test
    void shouldValidateSuccessfully() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        assertDoesNotThrow(client::validate);
    }

    @Test
    void shouldThrowWhenNameIsNullOnValidate() {
        Client client = Client.create(id, "email@test.com", null, "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        assertThrows(NullPointerException.class, client::validate);
    }

    @Test
    void shouldThrowWhenPhoneNumberIsNullOnValidate() {
        Client client = Client.create(id, "email@test.com", "John Doe", null,
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        assertThrows(NullPointerException.class, client::validate);
    }

    @Test
    void shouldThrowWhenBirthdayIsNullOnValidate() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                null, "Rua A, 123", "123456789012345", createdAt, true);

        assertThrows(NullPointerException.class, client::validate);
    }

    @Test
    void shouldThrowWhenAddressIsNullOnValidate() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, null, "123456789012345", createdAt, true);

        assertThrows(NullPointerException.class, client::validate);
    }

    @Test
    void shouldThrowWhenCnsIsNullOnValidate() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", null, createdAt, true);

        assertThrows(NullPointerException.class, client::validate);
    }

    @Test
    void shouldUpdateProfileSuccessfully() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        Date newBirthday = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 30);
        client.updateProfile("Jane Doe", "11888888888", newBirthday, "Rua B, 456", "000000000000000");

        assertEquals("Jane Doe", client.getName());
        assertEquals("11888888888", client.getPhoneNumber());
        assertEquals(newBirthday, client.getBirthday());
        assertEquals("Rua B, 456", client.getAddress());
        assertEquals("000000000000000", client.getCns());
    }

    @Test
    void shouldNotUpdateProfileWhenFieldsAreNullOrBlank() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        client.updateProfile(null, "", null, "  ", null);

        assertEquals("John Doe", client.getName());
        assertEquals("11999999999", client.getPhoneNumber());
        assertEquals(birthday, client.getBirthday());
        assertEquals("Rua A, 123", client.getAddress());
        assertEquals("123456789012345", client.getCns());
    }

    @Test
    void shouldDisableClient() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        assertTrue(client.getActive());

        Client disabledClient = client.disable();

        assertFalse(disabledClient.getActive());
        assertSame(client, disabledClient, "disable() deve retornar a mesma instância");
    }

    @Test
    void shouldEnableClient() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, false);

        assertFalse(client.getActive());

        Client enabledClient = client.enable();

        assertTrue(enabledClient.getActive());
        assertSame(client, enabledClient, "enable() deve retornar a mesma instância");
    }

    @Test
    void shouldDisableAndThenEnableClient() {
        Client client = Client.create(id, "email@test.com", "John Doe", "11999999999",
                birthday, "Rua A, 123", "123456789012345", createdAt, true);

        client.disable();
        assertFalse(client.getActive());

        client.enable();
        assertTrue(client.getActive());
    }
}
