package fr.gro.business;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

public class ImageTranslation {


	/**
	 * Tourne l'image passée en parametre avec un angle passé en parametre
	 * 
	 * @param idDrawable
	 *            : l'image a tourné
	 * @param context
	 * @param degree
	 *            : l'angle de transformation
	 * @return
	 */
	public Bitmap getImageWithRotation(int idDrawable, Context context,
			int degree) {

		Matrix mat = new Matrix();
		mat.postRotate(degree);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;

		Bitmap source = BitmapFactory.decodeResource(context.getResources(),
				idDrawable);

		return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), mat, true);
	}

	public Bitmap resizeImage(int idDrawable, Context context, float factor) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				idDrawable);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap,
				(int) Math.floor(width / factor),
				(int) Math.floor(height / factor),
				true);

		return resizedbitmap;
	}

	public Bitmap resizeImage(Bitmap bitmap, Context context, float factor) {

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap,
				(int) Math.floor(width / factor),
				(int) Math.floor(height / factor), true);

		return resizedbitmap;
	}

}
