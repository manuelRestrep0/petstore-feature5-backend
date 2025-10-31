package com.petstore.backend.dto;

import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.petstore.backend.entity.Category;
import com.petstore.backend.entity.Role;
import com.petstore.backend.entity.Status;
import com.petstore.backend.entity.User;

class PromotionDeletedDTOTest {

    private PromotionDeletedDTO promotionDeletedDTO;
    private Category testCategory;
    private Status testStatus;
    private User testUser;
    private User testDeletedByUser;

    @BeforeEach
    void setUp() {
        promotionDeletedDTO = new PromotionDeletedDTO();
        
        // Setup test entities
        testCategory = new Category();
        testCategory.setCategoryId(1);
        testCategory.setCategoryName("Electronics");
        
        testStatus = new Status();
        testStatus.setStatusId(1);
        testStatus.setStatusName("ACTIVE");
        
        Role role = new Role();
        role.setRoleId(1);
        role.setRoleName("MARKETING_ADMIN");
        
        testUser = new User();
        testUser.setUserId(1);
        testUser.setUserName("admin");
        testUser.setEmail("admin@petstore.com");
        testUser.setRole(role);
        
        testDeletedByUser = new User();
        testDeletedByUser.setUserId(2);
        testDeletedByUser.setUserName("manager");
        testDeletedByUser.setEmail("manager@petstore.com");
        testDeletedByUser.setRole(role);
    }

    @Test
    void constructor_ShouldCreateEmptyPromotionDeletedDTO() {
        // Then
        assertNotNull(promotionDeletedDTO);
        assertNull(promotionDeletedDTO.getPromotionId());
        assertNull(promotionDeletedDTO.getPromotionName());
        assertNull(promotionDeletedDTO.getDescription());
        assertNull(promotionDeletedDTO.getStartDate());
        assertNull(promotionDeletedDTO.getEndDate());
        assertNull(promotionDeletedDTO.getDiscountValue());
        assertNull(promotionDeletedDTO.getStatusName());
        assertNull(promotionDeletedDTO.getStatus());
        assertNull(promotionDeletedDTO.getUserName());
        assertNull(promotionDeletedDTO.getUser());
        assertNull(promotionDeletedDTO.getCategoryName());
        assertNull(promotionDeletedDTO.getCategory());
        assertNull(promotionDeletedDTO.getDeletedAt());
        assertNull(promotionDeletedDTO.getDeletedByUserName());
        assertNull(promotionDeletedDTO.getDeletedBy());
        assertNull(promotionDeletedDTO.getDaysUntilPurge());
    }

    @Test
    void constructorWithBasicFields_ShouldCreatePromotionDeletedDTOWithCalculatedDays() {
        // Given
        Integer promotionId = 1;
        String promotionName = "Summer Sale";
        String description = "Great discount";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);
        Double discountValue = 25.0;
        String statusName = "ACTIVE";
        String userName = "admin";
        String categoryName = "Electronics";
        ZonedDateTime deletedAt = ZonedDateTime.now().minusDays(5); // 5 days ago
        String deletedByUserName = "manager";

        // When
        PromotionDeletedDTO dto = new PromotionDeletedDTO(
            promotionId, promotionName, description, startDate, endDate,
            discountValue, statusName, userName, categoryName, deletedAt, deletedByUserName
        );

        // Then
        assertEquals(promotionId, dto.getPromotionId());
        assertEquals(promotionName, dto.getPromotionName());
        assertEquals(description, dto.getDescription());
        assertEquals(startDate, dto.getStartDate());
        assertEquals(endDate, dto.getEndDate());
        assertEquals(discountValue, dto.getDiscountValue());
        assertEquals(statusName, dto.getStatusName());
        assertEquals(userName, dto.getUserName());
        assertEquals(categoryName, dto.getCategoryName());
        assertEquals(deletedAt, dto.getDeletedAt());
        assertEquals(deletedByUserName, dto.getDeletedByUserName());
        assertNotNull(dto.getDaysUntilPurge());
        assertTrue(dto.getDaysUntilPurge() >= 0);
        assertTrue(dto.getDaysUntilPurge() <= 30);
    }

    @Test
    void constructorWithEntities_ShouldCreatePromotionDeletedDTOWithEntityReferences() {
        // Given
        Integer promotionId = 1;
        String promotionName = "Winter Sale";
        String description = "Cold weather discount";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(15);
        Double discountValue = 30.0;
        ZonedDateTime deletedAt = ZonedDateTime.now().minusDays(10);

        // When
        PromotionDeletedDTO dto = new PromotionDeletedDTO(
            promotionId, promotionName, description, startDate, endDate,
            discountValue, testStatus, testUser, testCategory, deletedAt, testDeletedByUser
        );

        // Then
        assertEquals(promotionId, dto.getPromotionId());
        assertEquals(promotionName, dto.getPromotionName());
        assertEquals(description, dto.getDescription());
        assertEquals(startDate, dto.getStartDate());
        assertEquals(endDate, dto.getEndDate());
        assertEquals(discountValue, dto.getDiscountValue());
        assertEquals(testStatus, dto.getStatus());
        assertEquals("ACTIVE", dto.getStatusName());
        assertEquals(testUser, dto.getUser());
        assertEquals("admin", dto.getUserName());
        assertEquals(testCategory, dto.getCategory());
        assertEquals("Electronics", dto.getCategoryName());
        assertEquals(deletedAt, dto.getDeletedAt());
        assertEquals(testDeletedByUser, dto.getDeletedBy());
        assertEquals("manager", dto.getDeletedByUserName());
        assertNotNull(dto.getDaysUntilPurge());
    }

    @Test
    void constructorWithNullDeletedAt_ShouldNotCalculateDaysUntilPurge() {
        // Given
        ZonedDateTime nullDeletedAt = null;

        // When
        PromotionDeletedDTO dto = new PromotionDeletedDTO(
            1, "Test Sale", "Description", LocalDate.now(), LocalDate.now().plusDays(10),
            20.0, "ACTIVE", "user", "category", nullDeletedAt, "manager"
        );

        // Then
        assertNull(dto.getDeletedAt());
        assertNull(dto.getDaysUntilPurge());
    }

    @Test
    void setPromotionId_ShouldSetPromotionId() {
        // Given
        Integer promotionId = 123;

        // When
        promotionDeletedDTO.setPromotionId(promotionId);

        // Then
        assertEquals(promotionId, promotionDeletedDTO.getPromotionId());
    }

    @Test
    void setPromotionName_ShouldSetPromotionName() {
        // Given
        String promotionName = "Black Friday";

        // When
        promotionDeletedDTO.setPromotionName(promotionName);

        // Then
        assertEquals(promotionName, promotionDeletedDTO.getPromotionName());
    }

    @Test
    void setDescription_ShouldSetDescription() {
        // Given
        String description = "Amazing deals";

        // When
        promotionDeletedDTO.setDescription(description);

        // Then
        assertEquals(description, promotionDeletedDTO.getDescription());
    }

    @Test
    void setStartDate_ShouldSetStartDate() {
        // Given
        LocalDate startDate = LocalDate.of(2024, 1, 1);

        // When
        promotionDeletedDTO.setStartDate(startDate);

        // Then
        assertEquals(startDate, promotionDeletedDTO.getStartDate());
    }

    @Test
    void setEndDate_ShouldSetEndDate() {
        // Given
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        // When
        promotionDeletedDTO.setEndDate(endDate);

        // Then
        assertEquals(endDate, promotionDeletedDTO.getEndDate());
    }

    @Test
    void setDiscountValue_ShouldSetDiscountValue() {
        // Given
        Double discountValue = 45.5;

        // When
        promotionDeletedDTO.setDiscountValue(discountValue);

        // Then
        assertEquals(discountValue, promotionDeletedDTO.getDiscountValue());
    }

    @Test
    void setStatusName_ShouldSetStatusName() {
        // Given
        String statusName = "EXPIRED";

        // When
        promotionDeletedDTO.setStatusName(statusName);

        // Then
        assertEquals(statusName, promotionDeletedDTO.getStatusName());
    }

    @Test
    void setUserName_ShouldSetUserName() {
        // Given
        String userName = "marketingUser";

        // When
        promotionDeletedDTO.setUserName(userName);

        // Then
        assertEquals(userName, promotionDeletedDTO.getUserName());
    }

    @Test
    void setCategoryName_ShouldSetCategoryName() {
        // Given
        String categoryName = "Clothing";

        // When
        promotionDeletedDTO.setCategoryName(categoryName);

        // Then
        assertEquals(categoryName, promotionDeletedDTO.getCategoryName());
    }

    @Test
    void setDeletedAt_ShouldSetDeletedAt() {
        // Given
        ZonedDateTime deletedAt = ZonedDateTime.now();

        // When
        promotionDeletedDTO.setDeletedAt(deletedAt);

        // Then
        assertEquals(deletedAt, promotionDeletedDTO.getDeletedAt());
    }

    @Test
    void setDeletedByUserName_ShouldSetDeletedByUserName() {
        // Given
        String deletedByUserName = "supervisor";

        // When
        promotionDeletedDTO.setDeletedByUserName(deletedByUserName);

        // Then
        assertEquals(deletedByUserName, promotionDeletedDTO.getDeletedByUserName());
    }

    @Test
    void setDaysUntilPurge_ShouldSetDaysUntilPurge() {
        // Given
        Integer daysUntilPurge = 15;

        // When
        promotionDeletedDTO.setDaysUntilPurge(daysUntilPurge);

        // Then
        assertEquals(daysUntilPurge, promotionDeletedDTO.getDaysUntilPurge());
    }

    @Test
    void setStatus_ShouldSetStatusAndStatusName() {
        // When
        promotionDeletedDTO.setStatus(testStatus);

        // Then
        assertEquals(testStatus, promotionDeletedDTO.getStatus());
        assertEquals("ACTIVE", promotionDeletedDTO.getStatusName());
    }

    @Test
    void setStatus_WithNullStatus_ShouldSetNullStatusName() {
        // When
        promotionDeletedDTO.setStatus(null);

        // Then
        assertNull(promotionDeletedDTO.getStatus());
        assertNull(promotionDeletedDTO.getStatusName());
    }

    @Test
    void setUser_ShouldSetUserAndUserName() {
        // When
        promotionDeletedDTO.setUser(testUser);

        // Then
        assertEquals(testUser, promotionDeletedDTO.getUser());
        assertEquals("admin", promotionDeletedDTO.getUserName());
    }

    @Test
    void setUser_WithNullUser_ShouldSetNullUserName() {
        // When
        promotionDeletedDTO.setUser(null);

        // Then
        assertNull(promotionDeletedDTO.getUser());
        assertNull(promotionDeletedDTO.getUserName());
    }

    @Test
    void setCategory_ShouldSetCategoryAndCategoryName() {
        // When
        promotionDeletedDTO.setCategory(testCategory);

        // Then
        assertEquals(testCategory, promotionDeletedDTO.getCategory());
        assertEquals("Electronics", promotionDeletedDTO.getCategoryName());
    }

    @Test
    void setCategory_WithNullCategory_ShouldSetNullCategoryName() {
        // When
        promotionDeletedDTO.setCategory(null);

        // Then
        assertNull(promotionDeletedDTO.getCategory());
        assertNull(promotionDeletedDTO.getCategoryName());
    }

    @Test
    void setDeletedBy_ShouldSetDeletedByAndDeletedByUserName() {
        // When
        promotionDeletedDTO.setDeletedBy(testDeletedByUser);

        // Then
        assertEquals(testDeletedByUser, promotionDeletedDTO.getDeletedBy());
        assertEquals("manager", promotionDeletedDTO.getDeletedByUserName());
    }

    @Test
    void setDeletedBy_WithNullDeletedBy_ShouldSetNullDeletedByUserName() {
        // When
        promotionDeletedDTO.setDeletedBy(null);

        // Then
        assertNull(promotionDeletedDTO.getDeletedBy());
        assertNull(promotionDeletedDTO.getDeletedByUserName());
    }

    @Test
    void daysUntilPurgeCalculation_WhenDeletedRecently_ShouldReturnHighNumber() {
        // Given - deleted 1 day ago
        ZonedDateTime recentDeletion = ZonedDateTime.now().minusDays(1);

        // When
        PromotionDeletedDTO dto = new PromotionDeletedDTO(
            1, "Recent Sale", "Description", LocalDate.now(), LocalDate.now().plusDays(10),
            20.0, "ACTIVE", "user", "category", recentDeletion, "manager"
        );

        // Then
        assertNotNull(dto.getDaysUntilPurge());
        assertTrue(dto.getDaysUntilPurge() >= 25); // Should be around 29 days left
    }

    @Test
    void daysUntilPurgeCalculation_WhenDeletedLongAgo_ShouldReturnZero() {
        // Given - deleted 35 days ago (past purge date)
        ZonedDateTime oldDeletion = ZonedDateTime.now().minusDays(35);

        // When
        PromotionDeletedDTO dto = new PromotionDeletedDTO(
            1, "Old Sale", "Description", LocalDate.now(), LocalDate.now().plusDays(10),
            20.0, "ACTIVE", "user", "category", oldDeletion, "manager"
        );

        // Then
        assertNotNull(dto.getDaysUntilPurge());
        assertEquals(0, dto.getDaysUntilPurge()); // Should be 0 since purge date has passed
    }

    @Test
    void daysUntilPurgeCalculation_WhenDeletedExactly30DaysAgo_ShouldReturnZero() {
        // Given - deleted exactly 30 days ago
        ZonedDateTime exactPurgeDeletion = ZonedDateTime.now().minusDays(30);

        // When
        PromotionDeletedDTO dto = new PromotionDeletedDTO(
            1, "Purge Sale", "Description", LocalDate.now(), LocalDate.now().plusDays(10),
            20.0, "ACTIVE", "user", "category", exactPurgeDeletion, "manager"
        );

        // Then
        assertNotNull(dto.getDaysUntilPurge());
        assertEquals(0, dto.getDaysUntilPurge());
    }
}
