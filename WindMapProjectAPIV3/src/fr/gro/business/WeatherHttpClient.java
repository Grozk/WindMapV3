package fr.gro.business;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import fr.gro.exception.WeatherClientException;

public class WeatherHttpClient extends AsyncTask<String, Void, String> {

	//TODO version a changer pour garder l'api à jour
	private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
	private static String APPID = "1ab32f913b1c92641498570ee730768a";
	private final String LATITUDE = "lat=";
	private final String LONGITUDE = "lon=";
	
	
	public String getWeatherData(String lat, String lon) throws Throwable {
        HttpURLConnection con = null ;
        InputStream is = null;
		StringBuffer buffer = null;
        try {
        	System.out.println("Debut de la demande");
			con = (HttpURLConnection) (new URL(BASE_URL + LATITUDE + lat + "&"
					+ LONGITUDE + lon + "&APPID=" + APPID)).openConnection();
            con.setRequestMethod("GET");
			con.setReadTimeout(3000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();
             
            // Let's read the response
			buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");
             
            is.close();
            con.disconnect();
            System.out.println(buffer.toString());
        }
        catch(Throwable t) {
            t.printStackTrace();
			throw new WeatherClientException(t);
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }
 
		return buffer.toString();
                 
    }

	@Override
	protected String doInBackground(String... arg0) {
		try {
			return getWeatherData(arg0[0], arg0[1]);
		} catch (Throwable e) {
			e.printStackTrace();
			return "-1";
		}
	}
	
}
