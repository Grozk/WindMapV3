package fr.gro.business;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.gro.exception.JsonParserException;
import fr.gro.objets.WeatherObject;

public class WeatherParserJson {
	
	/*{
	 * "coord":{"lon":-2.15,"lat":57},
	 * "sys":{"message":0.0048,"country":"GB","sunrise":1392190941,"sunset":1392224594},
	 * "weather":[{"id":501,"main":"Rain","description":"moderate rain","icon":"10n"}],
	 * "base": "cmc stations",
	 * "main":{"temp":279.15,"pressure":958,"humidity":87,"temp_min":279.15,"temp_max":279.15},
	 * "wind":{"speed":7.7,"deg":150},"rain":{"3h":1.5},
	 * "clouds":{"all":75},
	 * "dt":1392238200,
	 * "id":2641549,
	 * "name":"Newtonhill",
	 * "cod":200
	 * }*/
	
	public WeatherObject getWeather(String jsonToParse) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		WeatherObject wo = null;
		try {
			wo = mapper.readValue(jsonToParse, WeatherObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JsonParserException(e);
		}

		return wo;
	}
	

}
