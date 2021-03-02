package ru.netology;

import org.hamcrest.core.StringStartsWith;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

public class MessageSenderImplTest {

    private GeoService geoService;
    private LocalizationService localizationService;
    Map<String, String> headers = new HashMap<String, String>();

    @Before
    public void initParams(){
        geoService = Mockito.mock(GeoServiceImpl.class);
        Mockito.when(geoService.byIp(Mockito.anyString())).thenReturn(null);
        Mockito.when(geoService.byIp(GeoServiceImpl.LOCALHOST)).thenReturn(new Location(null, null, null, 0));
        Mockito.when(geoService.byIp(GeoServiceImpl.MOSCOW_IP)).thenReturn(new Location("Moscow", Country.RUSSIA, "Lenina", 15));
        Mockito.when(geoService.byIp(Mockito.startsWith("172."))).thenReturn(new Location("Moscow", Country.RUSSIA, null, 0));
        Mockito.when(geoService.byIp(Mockito.startsWith("96."))).thenReturn(new Location("New York", Country.USA, null, 0));
        Mockito.when(geoService.byIp(GeoServiceImpl.NEW_YORK_IP)).thenReturn(new Location("New York", Country.USA, " 10th Avenue", 32));

        localizationService = Mockito.mock(LocalizationServiceImpl.class);
        Mockito.when(localizationService.locale((Country) Mockito.any())).thenReturn("Welcome");
        Mockito.when(localizationService.locale(Country.RUSSIA)).thenReturn("Добро пожаловать");
        Mockito.when(localizationService.locale(Country.USA)).thenReturn("Welcome");
    }

    @Test
    public void testMethodSend_RussiaLocation(){
        final String expectedString = "Добро пожаловать";

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER,"172.0.0.1");
        String resultString = messageSender.send(headers);
        Assert.assertEquals(expectedString, resultString);

        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER,GeoServiceImpl.MOSCOW_IP);
        resultString = messageSender.send(headers);
        Assert.assertEquals(expectedString, resultString);
    }

    @Test
    public void testMethodSend_UsaLocation(){
        final String expectedString = "Welcome";

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER,"96.0.0.1");
        String resultString = messageSender.send(headers);
        Assert.assertEquals(expectedString, resultString);

        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER,GeoServiceImpl.NEW_YORK_IP);
        resultString = messageSender.send(headers);
        Assert.assertEquals(expectedString, resultString);
    }

    @Test
    public void testMethodSend_LocalHostLocation(){
        final String expectedString = "Welcome";

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER,GeoServiceImpl.LOCALHOST);
        String resultString = messageSender.send(headers);
        Assert.assertEquals(expectedString, resultString);
    }

    @Test
    public void testMethodSend_UnknownLocation(){

        MessageSender messageSender = new MessageSenderImpl(geoService, localizationService);

        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER,"173.0.0.1");
        Assert.assertThrows(NullPointerException.class, () -> messageSender.send(headers));

        headers.clear();
        Assert.assertThrows(NullPointerException.class, () -> messageSender.send(headers));
    }
}
