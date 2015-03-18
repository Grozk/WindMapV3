package fr.gro.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

public class SaveImage {

	public SaveImage() {
		super();
	}

	public String saveBitmap(Bitmap mBitmap) throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		mBitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes);

		// you can create a new file name "test.jpg" in sdcard folder.
		File fParent = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "WindSpotted");
		File fImage = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "WindSpotted" + File.separator
				+ "flecheToPrint" + Math.random() + ".png");
		if (!fParent.exists()) {
			fParent.mkdir();
		}

		// suppression de l'ancien enregistrement
		if (fImage.exists()) {
			fImage.delete();
		}

		FileOutputStream fo = null;
		try {
			fImage.createNewFile();
			// write the bytes in file
			fo = new FileOutputStream(fImage);
			fo.write(bytes.toByteArray());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			fo.flush();
			fo.close();

		}

		return fImage.getAbsolutePath().toString();
	}

	public void clearRepertory() {
		File fParent = new File(Environment.getExternalStorageDirectory()
				+ File.separator + "WindSpotted");

		for (File f : fParent.listFiles()) {
			f.delete();
		}
	}

}
