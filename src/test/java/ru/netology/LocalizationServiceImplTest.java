package ru.netology;

import org.junit.Assert;
import org.junit.Test;
import ru.netology.entity.Country;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;

public class LocalizationServiceImplTest {

    @Test
    public void testMethodLocate(){

        LocalizationService localizationService = new LocalizationServiceImpl();

        Assert.assertEquals("Добро пожаловать", localizationService.locale(Country.RUSSIA));
        Assert.assertEquals("Welcome", localizationService.locale(Country.USA));
        Assert.assertEquals("Welcome", localizationService.locale(Country.BRAZIL));
        Assert.assertEquals("Welcome", localizationService.locale(Country.GERMANY));
    }
}
