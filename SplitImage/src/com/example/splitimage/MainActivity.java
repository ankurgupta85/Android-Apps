package com.example.splitimage;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Chandra Sekhar Nayak
 * @version 1.0
 * 
 * This class is an activity and will displayed first when application starts 
 * This Activity will display an image to be split.
 * It also shows a sliding drawer with three contents.   
 */
public class MainActivity extends Activity implements OnClickListener {

	TextView moves = null;
	TextView timer = null;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /*
         * Here three, four and five are the id's of the buttons declared as 
         * the contents of the sliding drawer.
         * See main.xml for clarity
         */
        LinearLayout header = (LinearLayout) findViewById(R.id.header_layout);
		TableLayout table_header = new TableLayout(this);
		TableRow tr = new TableRow(this);
		Context context = getApplicationContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;


		TextView timer_label = new TextView(this);
		timer_label.setText("Time:");
		timer_label.setWidth(width / 4);
		timer_label.setHeight(width / 8);
		//timer_label.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);

		tr.addView(timer_label);

		timer = new TextView(this);
		timer.setText("Timer");

		timer.setWidth(width / 4);
		//timer.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

		timer.setHeight(width / 8);
		tr.addView(timer);

		TextView moves_label = new TextView(this);
		moves_label.setText("Moves:");
		moves_label.setWidth(width / 4);
		//moves_label.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);

		moves_label.setHeight(width / 8);
		tr.addView(moves_label);

		moves = new TextView(this);
		moves.setText("Count");
		moves.setWidth(width / 4);
		//moves.setTextAlignment(TextureView.TEXT_ALIGNMENT_CENTER);
		moves.setHeight(width / 8);
		tr.addView(this.moves);

		table_header.addView(tr, new TableLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		header.addView(table_header);
		
		ImageView imgView = (ImageView)findViewById(R.id.source_image);
		Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.images);
		
		
		imgView.setImageBitmap(image);

        Button b1 = (Button) findViewById(R.id.three);
        Button b2 = (Button) findViewById(R.id.four);
        Button b3 = (Button) findViewById(R.id.five);
        
        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
    	//chunkNumbers is to tell how many chunks the image should split
    	Toast.makeText(getApplicationContext(), "The last block of image is temporarily cut down for game", Toast.LENGTH_SHORT).show();
    	int chunkNumbers = 0;
    	
    	/*
    	 * switch-case is used to find the button clicked 
    	 * and assigning the actual value to chunkNumbers variable
    	 */
    	
    	switch (view.getId()) {
			case R.id.three:
				chunkNumbers = 9 ; 
				break;
			case R.id.four:
				chunkNumbers = 16 ;
				break;
			case R.id.five:
				chunkNumbers = 25 ;
		}
    	//Getting the source image to split
    	ImageView image = (ImageView) findViewById(R.id.source_image);
    	
    	
    	//invoking this method makes the actual splitting of the source image to given number of chunks
    	ArrayList<Bitmap> chunkedImages = splitImage(image, chunkNumbers);
    	Intent intent = new Intent(MainActivity.this, TempActivity.class);
    	intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		intent.putParcelableArrayListExtra("image chunks", chunkedImages);
		intent.putExtra("width", image.getWidth());
		intent.putExtra("height", image.getHeight());
		startActivity(intent);
    
    }
    
    /**
     * Splits the source image and show them all into a grid in a new activity
     * 
     * @param image The source image to split
     * @param chunkNumbers The target number of small image chunks to be formed from the source image
     */
	private ArrayList<Bitmap> splitImage(ImageView image, int chunkNumbers) {	
		
		//For the number of rows and columns of the grid to be displayed
		int rows,cols;
		
		//For height and width of the small image chunks 
		int chunkHeight,chunkWidth;
		
		//To store all the small image chunks in bitmap format in this list 
		ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
		
		//Getting the scaled bitmap of the source image
		BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
		Bitmap bitmap = drawable.getBitmap();
		Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
		
		rows = cols = (int) Math.sqrt(chunkNumbers);
		chunkHeight = bitmap.getHeight()/rows;
		chunkWidth = bitmap.getWidth()/cols;
		
		//xCoord and yCoord are the pixel positions of the image chunks
		int yCoord = 0;
		for(int x=0; x<rows; x++){
			int xCoord = 0;
			for(int y=0; y<cols; y++){
				chunkedImages.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
				xCoord += chunkWidth;
			}
			yCoord += chunkHeight;
		}
		
		System.out.println(image.getWidth());
		System.out.println(image.getHeight());
		//Start a new activity to show these chunks into a grid 
		return chunkedImages;
	}
}