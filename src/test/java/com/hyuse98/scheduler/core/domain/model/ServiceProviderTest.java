package com.hyuse98.scheduler.core.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ServiceProviderTest {

    private UUID id;
    private Date createdAt;
    private Date birthday;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        createdAt = new Date();
        birthday = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 35);
    }

    @Test
    void shouldCreateServiceProviderSuccessfully() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        assertNotNull(provider);
        assertEquals(id, provider.getId());
        assertEquals("Dr. Ana", provider.getName());
        assertEquals("ana@clinic.com", provider.getEmail());
        assertEquals("11999999999", provider.getPhoneNumber());
        assertEquals(birthday, provider.getBirthday());
        assertEquals("Av. Paulista, 100", provider.getAddress());
        assertEquals("Psicologia", provider.getExpertise());
        assertEquals("CRP-06/12345", provider.getRegistry());
        assertEquals(createdAt, provider.getCreatedAt());
        assertTrue(provider.getIsActive());
    }

    @Test
    void shouldReconstituteServiceProviderSuccessfully() {
        ServiceProvider provider = ServiceProvider.reconstitute(id, "Dr. Carlos", "carlos@clinic.com",
                "11888888888", birthday, "Rua B, 200",
                "Fisioterapia", "CREFITO-3/12345", createdAt, false);

        assertNotNull(provider);
        assertEquals(id, provider.getId());
        assertEquals("Dr. Carlos", provider.getName());
        assertFalse(provider.getIsActive());
    }

    @Test
    void shouldValidateSuccessfully() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        assertDoesNotThrow(provider::validate);
    }

    @Test
    void shouldThrowWhenNameIsNullOnValidate() {
        ServiceProvider provider = ServiceProvider.create(id, null, "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        assertThrows(NullPointerException.class, provider::validate);
    }

    @Test
    void shouldThrowWhenEmailIsNullOnValidate() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", null,
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        assertThrows(NullPointerException.class, provider::validate);
    }

    @Test
    void shouldThrowWhenExpertiseIsNullOnValidate() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                null, "CRP-06/12345", createdAt, true);

        assertThrows(NullPointerException.class, provider::validate);
    }

    @Test
    void shouldThrowWhenRegistryIsNullOnValidate() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", null, createdAt, true);

        assertThrows(NullPointerException.class, provider::validate);
    }

    @Test
    void shouldUpdateProfileSuccessfully() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        Date newBirthday = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 365 * 40);
        provider.updateProfile("Dra. Ana Maria", "11777777777", newBirthday,
                "Rua C, 300", "Neuropsicologia", "CRP-06/99999");

        assertEquals("Dra. Ana Maria", provider.getName());
        assertEquals("11777777777", provider.getPhoneNumber());
        assertEquals(newBirthday, provider.getBirthday());
        assertEquals("Rua C, 300", provider.getAddress());
        assertEquals("Neuropsicologia", provider.getExpertise());
        assertEquals("CRP-06/99999", provider.getRegistry());
    }

    @Test
    void shouldNotUpdateProfileWhenFieldsAreNullOrBlank() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        provider.updateProfile(null, "", null, "   ", null, "  ");

        assertEquals("Dr. Ana", provider.getName());
        assertEquals("11999999999", provider.getPhoneNumber());
        assertEquals(birthday, provider.getBirthday());
        assertEquals("Av. Paulista, 100", provider.getAddress());
        assertEquals("Psicologia", provider.getExpertise());
        assertEquals("CRP-06/12345", provider.getRegistry());
    }

    @Test
    void shouldDisableServiceProvider() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        assertTrue(provider.getIsActive());

        ServiceProvider disabledProvider = provider.disable();

        assertFalse(disabledProvider.getIsActive());
        assertSame(provider, disabledProvider, "disable() deve retornar a mesma instância");
    }

    @Test
    void shouldEnableServiceProvider() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, false);

        assertFalse(provider.getIsActive());

        ServiceProvider enabledProvider = provider.enable();

        assertTrue(enabledProvider.getIsActive());
        assertSame(provider, enabledProvider, "enable() deve retornar a mesma instância");
    }

    @Test
    void shouldDisableAndThenEnableServiceProvider() {
        ServiceProvider provider = ServiceProvider.create(id, "Dr. Ana", "ana@clinic.com",
                "11999999999", birthday, "Av. Paulista, 100",
                "Psicologia", "CRP-06/12345", createdAt, true);

        provider.disable();
        assertFalse(provider.getIsActive());

        provider.enable();
        assertTrue(provider.getIsActive());
    }
}
