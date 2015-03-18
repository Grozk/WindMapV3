package fr.gro;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import fr.gro.windmapproject.WindMap;
/**
 * Class to handle all calls from JS & from Java too
 **/
public class JsHandler
 {
	WindMap windMapActivity;
	String TAG = "JsHandler";
	WebView webView;
	
	
	public JsHandler(WindMap _windMapActivity, WebView _webView) {
		webView = _webView;
		windMapActivity = _windMapActivity;
	}


	/**
	 * This function handles call from Android-Java
	 */
	public void displayMessage(String jsString) {
		
		final String webUrl = "javascript:displayJavaMsg('" + jsString + "')";
		// Add this to avoid android.view.windowmanager$badtokenexception unable to add window
		if (!windMapActivity.isFinishing())
			// loadurl on UI main thread
			windMapActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				webView.loadUrl(webUrl); 
			}
		});
	}

	/**
	 * Recuperation de la longitude dans le script JS
	 */
	public void getLongitude() {

		final String webUrl = "javascript:getLongitude()";
		// Add this to avoid android.view.windowmanager$badtokenexception unable
		// to add window
		if (!windMapActivity.isFinishing())
			// loadurl on UI main thread
			windMapActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(webUrl);
				}
			});
	}

	/**
	 * Recuperation de la latitude dans le script JS
	 */
	public void getLatitude() {

		final String webUrl = "javascript:getLatitude()";
		// Add this to avoid android.view.windowmanager$badtokenexception unable
		// to add window
		if (!windMapActivity.isFinishing())
			// loadurl on UI main thread
			windMapActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(webUrl);
				}
			});
	}

	/**
	 * Place un marqueur sur la carte
	 */
	public void placeMarker(String path) {

		final String webUrl = "javascript:placeMarker('" + path + "')";
		// Add this to avoid android.view.windowmanager$badtokenexception unable
		// to add window
		if (!windMapActivity.isFinishing())
			// loadurl on UI main thread
			windMapActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(webUrl);
				}
			});
	}

	/**
	 * Place un marqueur sur la carte
	 */
	public void setLatitudePrefToJs(double latitude) {

		final String webUrl = "javascript:setLatitude(" + latitude + ")";
		// Add this to avoid android.view.windowmanager$badtokenexception unable
		// to add window
		if (!windMapActivity.isFinishing())
			// loadurl on UI main thread
			windMapActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(webUrl);
				}
			});
	}

	/**
	 * Place un marqueur sur la carte
	 */
	public void setLongitudePrefToJs(double longitude) {

		final String webUrl = "javascript:setLatitude(" + longitude + ")";
		// Add this to avoid android.view.windowmanager$badtokenexception unable
		// to add window
		if (!windMapActivity.isFinishing())
			// loadurl on UI main thread
			windMapActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(webUrl);
				}
			});
	}

	public void initPosition(double latitude,double longitude){
		
		final String webUrl = "javascript:initPosition(" + latitude + ","
				+ longitude + ")";
		// Add this to avoid android.view.windowmanager$badtokenexception unable
		// to add window
		if (!windMapActivity.isFinishing())
			// loadurl on UI main thread
			windMapActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					webView.loadUrl(webUrl);
				}
			});
	}

	public void demandeRafraichissementLatLon() {
		getLongitude();
		getLatitude();

	}


	@JavascriptInterface
	public void setLat(double lat) {
		this.windMapActivity.setLatitude(lat);
	}

	@JavascriptInterface
	public void setLon(double lon) {
		this.windMapActivity.setLongitude(lon);
	}

	@JavascriptInterface
	public void getPrevision(double lat, double lon) {
		this.windMapActivity.getPrevisionFromWeb(lat, lon);
	}

	@JavascriptInterface
	public void getPositionFromPreferences() {
		this.windMapActivity.getPositionFromPreferences();
	}
}