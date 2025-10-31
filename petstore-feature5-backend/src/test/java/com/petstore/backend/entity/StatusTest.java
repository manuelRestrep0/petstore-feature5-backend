package com.petstore.backend.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusTest {

    private Status status;

    @BeforeEach
    void setUp() {
        status = new Status();
    }

    @Test
    void constructor_ShouldCreateEmptyStatus() {
        // Then
        assertNotNull(status);
        assertNull(status.getStatusId());
        assertNull(status.getStatusName());
    }

    @Test
    void setStatusId_ShouldSetStatusId() {
        // Given
        Integer statusId = 1;

        // When
        status.setStatusId(statusId);

        // Then
        assertEquals(statusId, status.getStatusId());
    }

    @Test
    void setStatusName_ShouldSetStatusName() {
        // Given
        String statusName = "ACTIVE";

        // When
        status.setStatusName(statusName);

        // Then
        assertEquals(statusName, status.getStatusName());
    }

    @Test
    void allFieldsCanBeSetAndRetrieved() {
        // Given
        Integer statusId = 1;
        String statusName = "INACTIVE";

        // When
        status.setStatusId(statusId);
        status.setStatusName(statusName);

        // Then
        assertEquals(statusId, status.getStatusId());
        assertEquals(statusName, status.getStatusName());
    }

    @Test
    void fieldsCanBeNullSafely() {
        // Given
        status.setStatusId(null);
        status.setStatusName(null);

        // When & Then
        assertNull(status.getStatusId());
        assertNull(status.getStatusName());

        // Should not throw any exceptions
        assertDoesNotThrow(() -> {
            status.toString();
            status.hashCode();
        });
    }

    @Test
    void statusName_ShouldAcceptEmptyString() {
        // Given
        String emptyStatusName = "";

        // When
        status.setStatusName(emptyStatusName);

        // Then
        assertEquals(emptyStatusName, status.getStatusName());
    }

    @Test
    void statusName_ShouldAcceptLongString() {
        // Given
        String longStatusName = "A".repeat(100);

        // When
        status.setStatusName(longStatusName);

        // Then
        assertEquals(longStatusName, status.getStatusName());
    }

    @Test
    void statusId_ShouldAcceptZero() {
        // Given
        Integer zeroId = 0;

        // When
        status.setStatusId(zeroId);

        // Then
        assertEquals(zeroId, status.getStatusId());
    }

    @Test
    void statusId_ShouldAcceptNegativeValue() {
        // Given
        Integer negativeId = -1;

        // When
        status.setStatusId(negativeId);

        // Then
        assertEquals(negativeId, status.getStatusId());
    }

    @Test
    void statusId_ShouldAcceptLargeValue() {
        // Given
        Integer largeId = Integer.MAX_VALUE;

        // When
        status.setStatusId(largeId);

        // Then
        assertEquals(largeId, status.getStatusId());
    }
}
