package fr.gro.windmapproject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapsInitializer;

import fr.gro.JsHandler;
import fr.gro.business.ImageTranslation;
import fr.gro.business.WeatherHttpClient;
import fr.gro.business.WeatherParserJson;
import fr.gro.controller.SaveImage;
import fr.gro.exception.JsonParserException;
import fr.gro.exception.WeatherClientException;
import fr.gro.objets.WeatherObject;

public class WindMap extends FragmentActivity implements
		SensorEventListener {

	public static final String PREFS_NAME = "WindSpotted_Preferences";

	private SaveImage si;
	private Bitmap bm = null;
	private static final DecimalFormat FORMAT_DECIMAL = new DecimalFormat(
			"##.00");

	private double latitude;
	private double longitude;
	private double azimuth = 0.0;

	/** * The sensor manager */
	SensorManager sensorManager;
	/** sensor */
	private Sensor sensorAccelerometer;
	private Sensor sensorMagneticField;

	private float[] valuesAccelerometer;
	private float[] valuesMagneticField;

	private float[] matrixR;
	private float[] matrixI;
	private float[] matrixValues;

	/** handler JS <> Java */
	private JsHandler _jsHandler;

	WebView myBrowser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wind_map);

		// init des capteurs
		initCapteur();

		// init webview et handler javascript
		try {
			initWebView();
		} catch (GooglePlayServicesNotAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		si = new SaveImage();

		// message d'info si c'est la premiere fois
		showMgsInfo();

		Button button = (Button) findViewById(R.id.activityWindMap_directionDuVent_Bouton);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getAccelerometre();
			}
		});

	}

	/**
	 * Initialisation de la webview et des parametre du browser utilisé
	 * Initialisation du handler JS utilisé
	 * 
	 * @throws GooglePlayServicesNotAvailableException
	 */
	private void initWebView() throws GooglePlayServicesNotAvailableException {
		// init de la webview
		myBrowser = (WebView) findViewById(R.id.mybrowser);

		// Set whether the DOM storage API is enabled.
		myBrowser.getSettings().setDomStorageEnabled(true);
		// myBrowser.getSettings().setPluginState(PluginState.ON);
		myBrowser.getSettings().setAllowFileAccess(true);
		// myBrowser.getSettings().setAppCacheMaxSize(1024 * 8);
		myBrowser.getSettings().setAppCacheEnabled(true);
		myBrowser.getSettings().setJavaScriptEnabled(true);

		// association avec le handler javascript qui fera le lien java <>
		// javascript
		_jsHandler = new JsHandler(this, myBrowser);
		myBrowser.addJavascriptInterface(_jsHandler, "JsHandler");

		myBrowser.getSettings().setUseWideViewPort(false);
		myBrowser.setWebChromeClient(new WebChromeClient());

		// chargement du fichier xml
		myBrowser.loadUrl("file:///android_asset/simplemap.html");
		MapsInitializer.initialize(getApplicationContext());
	}

	/**
	 * Initialisation des capteurs : - accelerometres - magnetic
	 */
	private void initCapteur() {
		// initialisation des capteurs
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorAccelerometer = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorMagneticField = sensorManager
				.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

		valuesAccelerometer = new float[3];
		valuesMagneticField = new float[3];

		matrixR = new float[9];
		matrixI = new float[9];
		matrixValues = new float[3];

	}

	/**
	 * Message explicatif la premiere fois
	 */
	private void showMgsInfo() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		boolean isFirstTime = settings.getBoolean("isFirstTime", true);
		if (isFirstTime) {
			exectAlertDialog(Html.fromHtml(getResources().getString(
					R.string.mode_emploi)), Html.fromHtml(getResources()
					.getString(R.string.windspotted_modeemploi)));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		// sauvegarde des parametres de l'appli
		savePreferences();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// setUpMapIfNeeded();
		sensorManager.registerListener(this, sensorAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, sensorMagneticField,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		// stop les capteurs
		if (sensorManager != null) {
			sensorManager.unregisterListener(this, sensorAccelerometer);
			sensorManager.unregisterListener(this, sensorMagneticField);
		}
		// clear le browser
		// if (myBrowser != null) {
		// myBrowser.loadUrl("about:blank");
		// }
		// Clear le repertoire des images (fleches)
		si.clearRepertory();

		// sauvegarde des parametres de l'appli
		savePreferences();

		super.onStop();
	}

	private void savePreferences() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat("latitude", (float) this.latitude);
		editor.putFloat("longitude", (float) this.longitude);
		editor.putBoolean("isFirstTime", false);
		editor.commit();
	}
	/**
	 * Positionne un marker sur la map en forme de fleche la direction de la
	 * fleche change selon la diretion du vent
	 * 
	 * @param degree
	 *            : degres d'orientation du vent
	 * @param speed
	 *            : vitesse du vent
	 */
	private void printMarkerWind(int degree, Double speed) {


		ImageTranslation it = new ImageTranslation();
		// degres pour rotation de la fleche afin qu'elle affiche le sens du
		// vent
		int degreeToTranslate = 180;
		bm = null;
		// choix de l'image en fonction de la vitesse du vent
		double speedKMH = 0.0;
		if (speed != null) {
			speedKMH = speed * 18 / 5;

			if (speedKMH < 20) {
				// fleche verte
				bm = it.getImageWithRotation(R.drawable.fleche_verte, this,
						degree + degreeToTranslate);
			} else if (speedKMH < 40) {
				// fleche jaune
				bm = it.getImageWithRotation(R.drawable.fleche_jaune, this,
						degree + degreeToTranslate);
			} else if (speedKMH < 60) {
				// fleche orange
				bm = it.getImageWithRotation(R.drawable.fleche_orange, this,
						degree + degreeToTranslate);
			} else {
				// fleche rouge
				bm = it.getImageWithRotation(R.drawable.fleche_rouge, this,
						degree + degreeToTranslate);
			}
		} else {
			// fleche noire
			bm = it.getImageWithRotation(R.drawable.fleche, this, degree
					+ degreeToTranslate - 90);
			bm = it.resizeImage(bm, this.getApplicationContext(), 12);
		}


		// enregistrement de l'image dans la SD card et affichage de l'image via
		// JS
		try {
			String result = si.saveBitmap(bm);
			System.out.println("CHEMIN DE SAVE IMAGE IN SDCARD" + result);
			
			_jsHandler.placeMarker(result);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*************************
	 * Fonctions des boutons *
	 ************************/

	/**
	 * Called when the snapshot button is clicked.
	 */
	public void onScreenshot(View view) {
		takeSnapshotWebview();
	}

	/**
	 * Prise d'un screenshot de la webview
	 */
	private void takeSnapshotWebview() {
		myBrowser.setDrawingCacheEnabled(true);

		Bitmap snapShot = myBrowser.getDrawingCache();

		Bitmap bmp = Bitmap.createBitmap(snapShot);

		sendMMSScreenshot(bmp);
		myBrowser.setDrawingCacheEnabled(false);

	}

	/**
	 * Envoi un sms avec le screenshot de la webview
	 * 
	 * @param mScreenShot
	 */
	private void sendMMSScreenshot(Bitmap mScreenShot) {

		// envoi d'un MMS avec le snashot
		Toast.makeText(getApplicationContext(), "Send MMS picture",
				Toast.LENGTH_LONG).show();

		String pathofBmp = Images.Media.insertImage(getContentResolver(),
				mScreenShot, "snapshotWindMap", null);
		Uri bmpUri = Uri.parse(pathofBmp);

		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.putExtra(
				"sms_body",
				"Latitude :"
						+ FORMAT_DECIMAL.format(latitude)
						+ "|Longitude : "
						+ FORMAT_DECIMAL.format(longitude)
						+ " avec WindMap, l'application des surfeurs! Recupère toi aussi WindMap en cliquant sur le lien : [link windmap on googleplay]");

		intent.putExtra(Intent.EXTRA_STREAM, bmpUri);
		intent.setType("image/png");
		startActivity(intent);
	}

	/**
	 * Recupere les prévisions sur le web lors d'un double tab
	 * 
	 * @param latitude
	 * @param longitude
	 */
	public void getPrevisionFromWeb(double lat, double lon) {

		WeatherHttpClient whc = new WeatherHttpClient();
		WeatherParserJson wpj = new WeatherParserJson();
		WeatherObject wo = new WeatherObject();

		// Recuperation des données météo
		AsyncTask<String, Void, String> responseWeather = null;
		this.latitude = lat;
		this.longitude = lon;
		responseWeather = whc.execute(Double.toString(this.latitude),
				Double.toString(this.longitude));

		try {
			if (responseWeather.get() != null && !responseWeather.equals("-1")) {
				wo = wpj.getWeather(responseWeather.get());

				wo.getWind().getDeg();
				wo.getWind().getSpeed();

				Toast.makeText(
						getApplicationContext(),
						"Wind direction : " + wo.getWind().getDeg()
								+ "degres || Wind speed : "
								+ wo.getWind().getSpeed() + "m/s",
						Toast.LENGTH_LONG).show();

				// print du marker concernant la vitesse et le sens du vent
				printMarkerWind(wo.getWind().getDeg(), wo.getWind().getSpeed());
			} else {
				Toast.makeText(getApplicationContext(),
						"Donnees meteo indisponibles", Toast.LENGTH_LONG)
						.show();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// on créé une AlertDialog pour alerter l'utilisateur
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Une erreur est survenue.");
			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			adb.show();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// on créé une AlertDialog pour alerter l'utilisateur
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Une erreur est survenue.");
			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			adb.show();
		} catch (JsonParserException e) {
			// on créé une AlertDialog pour alerter l'utilisateur
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Une erreur est survenue lors de la recuperation des données météos.");
			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			adb.show();
		} catch (WeatherClientException e) {
			// on créé une AlertDialog pour alerter l'utilisateur
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Une erreur est survenue lors de la recuperation des données météos.");
			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
				}
			});
			adb.show();
		} catch (Exception e) {
			// on créé une AlertDialog pour alerter l'utilisateur
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("Une erreur est survenue lors de la recuperation des données météos.");
			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
					System.exit(0);
				}
			});
			adb.show();
		}
	}


	private void getAccelerometre() {
		_jsHandler.getLatitude();
		_jsHandler.getLongitude();
		System.out.println("VALEUR LAT ::::::::::::" + latitude);
		System.out.println("VALEUR LON ::::::::::::" + longitude);

		printImageFWind();

	}

	private void printImageFWind() {
		printMarkerWind((int) Math.round(azimuth), null);
		Toast.makeText(getApplicationContext(),
				"Wind direction : " + azimuth + " degres", Toast.LENGTH_LONG)
				.show();
	}


	@Override
	public void onSensorChanged(SensorEvent e) {

		switch (e.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
			for (int i = 0; i < 3; i++) {
				valuesAccelerometer[i] = e.values[i];
			}
			break;
		case Sensor.TYPE_MAGNETIC_FIELD:
			for (int i = 0; i < 3; i++) {
				valuesMagneticField[i] = e.values[i];
			}
			break;
		}

		boolean success = SensorManager.getRotationMatrix(matrixR, matrixI,
				valuesAccelerometer, valuesMagneticField);

		if (success) {
			SensorManager.getOrientation(matrixR, matrixValues);

			azimuth = Math.toDegrees(matrixValues[0]);


			if (azimuth < 0) {
				azimuth += 360;
			}

		}

	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	/**
	 * Consultation des preferences
	 */
	public void getPositionFromPreferences() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		this.latitude = (double) settings.getFloat("latitude", (float) 0.0);
		this.longitude = (double) settings.getFloat("longitude", (float) 0.0);

		_jsHandler.initPosition(this.latitude, this.longitude);
	}

	/* demande de rafraichissement des variables lat/lon quand necessaire */
	public void demandeRafraichissementLatLon() {
		_jsHandler.demandeRafraichissementLatLon();
	}

	public void setLatitude(double lat) {
		this.latitude = lat;
	}

	public void setLongitude(double lon) {
		this.longitude = lon;
	}

	/**
	 * Fonction de pop up
	 */
	private void exectAlertDialog(Spanned titre, Spanned description) {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog2.setTitle(titre);

		// Setting Dialog Message
		alertDialog2.setMessage(description);

		// textView avec la description du type d'entrainement

		// Showing Alert Dialog
		alertDialog2.show();
	}

}
