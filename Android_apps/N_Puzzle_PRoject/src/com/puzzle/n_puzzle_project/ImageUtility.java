package com.puzzle.n_puzzle_project;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class ImageUtility {

	
	public ImageUtility()
	{
		System.gc();
	}
	
	
	
	
	
	
	/**
	 * Method to save bitmap into internal memory
	 * 
	 * @param image
	 *            and context
	 */
	
	
	public boolean saveImageToInternalStorage(Bitmap image, Context context) {
		
		try {

			FileOutputStream fos = context.openFileOutput("photo.png",
					Context.MODE_PRIVATE);
			
			image.compress(Bitmap.CompressFormat.PNG, 100, fos);
			// 100 means no compression, the lower you go, the stronger the
			// compression
			fos.close();

			return true;
		} catch (Exception e) {
			Log.e("saveToInternalStorage()", e.getMessage());
		}
		return false;
	}
	
	

	public Bitmap getImageFromInternalStorage(Context context) {
		Bitmap image = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize=2;
			File filePath = context.getFileStreamPath("photo.png");
			
			image = BitmapFactory.decodeFile(filePath.toString(),options);
		}

		catch (Exception e) {
			Log.e("Load from InternalStorage()", e.getMessage());
		}

		return image;

	}

	public ArrayList<Bitmap> splitImage(Bitmap image, int chunkNumbers) {

		// For the number of rows and columns of the grid to be displayed
		int rows, cols;

		// For height and width of the small image chunks
		int chunkHeight, chunkWidth;

		// To store all the small image chunks in bitmap format in this list
		ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);

		// Getting the scaled bitmap of the source image
		Bitmap bitmap = image;
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,
				bitmap.getWidth(), bitmap.getHeight(), true);

		rows = cols = (int) Math.sqrt(chunkNumbers);
		chunkHeight = bitmap.getHeight() / rows;
		chunkWidth = bitmap.getWidth() / cols;

		// xCoord and yCoord are the pixel positions of the image chunks
		int yCoord = 0;
		for (int x = 0; x < rows; x++) {
			int xCoord = 0;
			for (int y = 0; y < cols; y++) {
				chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord,
						yCoord, chunkWidth, chunkHeight));
				xCoord += chunkWidth;
			}
			yCoord += chunkHeight;
		}

		return chunkedImages;
	}
}
