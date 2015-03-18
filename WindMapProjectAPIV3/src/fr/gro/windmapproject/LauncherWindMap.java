package fr.gro.windmapproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import fr.gro.business.ConnectionDetector;

public class LauncherWindMap extends Activity implements OnClickListener {

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		// check for Internet status
		if (isInternetPresent) {
			// TODO pause pour voir un eventuelle pub
			Toast.makeText(this, "Internet OK ", Toast.LENGTH_SHORT).show();

		} else {
			// Internet connection is not present
			// on créé une AlertDialog pour alerter l'utilisateur
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle("La connexion internet n'est pas active");
			adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.dismiss();
					System.exit(0);
				}
			});
			adb.show();
		}

		Button boutonLancement = (Button) findViewById(R.id.buttonLancement);
		boutonLancement.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		Intent intentWindMap = new Intent(this, WindMap.class);
		this.startActivity(intentWindMap);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
