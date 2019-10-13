package com.app.service.test;

import com.app.enums.Category;
import com.app.model.Preference;
import com.app.service.PreferenceService;
import com.app.exception.MyUncheckedException;
import com.app.service.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class TestPreferencesService {

    private String jsonPreferenceFile = "TestFilePreferences.json";
    private PreferenceService preferenceService = new PreferenceService(jsonPreferenceFile);

    @Test
    @DisplayName("Test -> shouldThrowExceptionForWrongJsonPreferenceFileName")
    public void shouldThrowExceptionForWrongJsonPreferenceFileName() {
        MyUncheckedException e = Assertions.assertThrows(MyUncheckedException.class, () -> new PreferenceService("wrongFileName"));
        Assertions.assertEquals("FROM JSON - JSON FILENAME EXCEPTION", e.getMessage());
    }

    @Test
    @DisplayName("Test -> shouldReturnCorrectSizeOfPreferences")
    public void shouldReturnCorrectSizeOfPreferences() {
        Assertions.assertEquals(4, preferenceService.getNumberOfAllPreferences());
    }

    @Test
    @DisplayName("Test -> shouldReturnWrongSizeOfPreferences")
    public void shouldReturnWrongSizeOfPreferences() {
        Assertions.assertNotEquals(1, preferenceService.getNumberOfAllPreferences());
    }

    @Test
    @DisplayName("Test -> test correct data in ProductService ")
    public void testProductDataInService() {
        Preference preference = preferenceService.findAll().get(0);
        System.out.println(preference);
        Assertions.assertAll(
                () -> Assertions.assertEquals(preference.getPriorityNumber(), 1),
                () -> Assertions.assertEquals(preference.getCategory(), Category.AGD)
        );
    }

    @Mock
    private ProductService customerServiceMock;

    @Test
    @DisplayName("Test test mock customerServiceMock")
    public void mockProductService() {
        customerServiceMock.findNumbersOfProducts();
        Mockito.verify(customerServiceMock).findNumbersOfProducts();
        Mockito.when(customerServiceMock.findNumbersOfProducts()).thenReturn(100);
        Assertions.assertEquals(100, customerServiceMock.findNumbersOfProducts());
    }
}
