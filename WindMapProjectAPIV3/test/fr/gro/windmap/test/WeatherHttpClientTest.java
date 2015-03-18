package fr.gro.windmap.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import fr.gro.business.WeatherHttpClient;

public class WeatherHttpClientTest {

	
	WeatherHttpClient weatherClient;
	
	@Before
	public void setUp() throws Exception {
		weatherClient = new WeatherHttpClient();
	}


	@Test
	public void testWeatherHttpClientLong(){
		String result = null;
		try {
			result = weatherClient.getWeatherData("48.4", "-4.4833");
		} catch (Throwable e) {
			e.printStackTrace();
			fail();
		}
		
		assertNotNull(result);
		
		System.out.println(result);
	}


}
