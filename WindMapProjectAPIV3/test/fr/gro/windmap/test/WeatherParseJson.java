package fr.gro.windmap.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import fr.gro.business.WeatherParserJson;
import fr.gro.objets.WeatherObject;

public class WeatherParseJson {

	String messagejson = "{\"coord\":{\"lon\":-2.15,\"lat\":57},"
			+ "\"sys\":{\"message\":0.0048,\"country\":\"GB\",\"sunrise\":1392190941,\"sunset\":1392224594},"
			+ "\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10n\"}],"
			+ "\"base\":\"cmc stations\","
			+ "\"main\":{\"temp\":279.15,\"pressure\":958,\"humidity\":87,\"temp_min\":279.15,\"temp_max\":279.15},"
			+ "\"wind\":{\"speed\":7.7,\"deg\":150},"
			+ "\"rain\":{\"3h\":1.5}," + "\"clouds\":{\"all\":75},"
			+ "\"dt\":1392238200," + "\"id\":2641549,"
			+ "\"name\":\"Newtonhill\"," + "\"cod\":200}";

	WeatherParserJson parserJson;

	@Before
	public void setUp() throws Exception {
		parserJson = new WeatherParserJson();
	}

	@Test
	public void testParseMessageJson() {
		WeatherObject wv = null;
		try {
			wv = parserJson.getWeather(messagejson);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		assertEquals(Integer.valueOf("150"), wv.getWind().getDeg());
		assertEquals(Double.valueOf("7.7"), wv.getWind().getSpeed());

	}
}
