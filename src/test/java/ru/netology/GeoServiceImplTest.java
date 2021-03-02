package ru.netology;

import org.junit.Assert;
import org.junit.Test;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;

public class GeoServiceImplTest {

    @Test
    public void testMethodByIp(){

        GeoService geoService = new GeoServiceImpl();

        Location resultLocation = geoService.byIp(GeoServiceImpl.LOCALHOST);
        Assert.assertEquals(null, resultLocation.getCountry());

        resultLocation = geoService.byIp(GeoServiceImpl.NEW_YORK_IP);
        Assert.assertEquals(Country.USA, resultLocation.getCountry());

        resultLocation = geoService.byIp("96.0.0.1");
        Assert.assertEquals(Country.USA, resultLocation.getCountry());

        resultLocation = geoService.byIp(GeoServiceImpl.MOSCOW_IP);
        Assert.assertEquals(Country.RUSSIA, resultLocation.getCountry());

        resultLocation = geoService.byIp("172.0.0.1");
        Assert.assertEquals(Country.RUSSIA, resultLocation.getCountry());

        resultLocation = geoService.byIp("217.0.0.1");
        Assert.assertEquals(null, resultLocation);
    }
}
