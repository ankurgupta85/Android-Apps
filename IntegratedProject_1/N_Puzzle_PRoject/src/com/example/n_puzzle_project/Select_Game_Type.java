package com.example.n_puzzle_project;




import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class Select_Game_Type extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		System.gc();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select__game__type);
		Context context = getApplicationContext();
		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		TextView text = (TextView)findViewById(R.id.text);
		text.setTextSize(height/70);
		text.setPadding(5, 5, 5, 5);
		int no_cols = 3;
		int no_rows = 3;
		TableLayout table = (TableLayout)findViewById(R.id.table);
		Resources r = getResources();
		TableRow tr = new TableRow(this);
		Bitmap image =null;
		for (int j = 1; j <= 12; j++) {
			ImageView  imgView = new ImageView(this);
			imgView.setId(j);
			int i= j -1;
			if(j==11)
			{
				image = BitmapFactory.decodeResource(getResources(), R.drawable.image_gallery);
				
			}
			else if(j==12)
			{
				image = BitmapFactory.decodeResource(getResources(), R.drawable.image_url);
				
			}
			else
			{
			int drawableId = r.getIdentifier("image_"+i, "drawable", getPackageName());
			image = BitmapFactory.decodeResource(getResources(), drawableId);
			}
			
			image = Bitmap.createScaledBitmap(image, width/no_cols, width/no_rows, true);
			
			
			android.view.ViewGroup.LayoutParams layout = new LayoutParams(width/no_cols, width/no_rows);
			imgView.setLayoutParams(layout);
			imgView.setImageBitmap(image);
			imgView.setPadding(5, 5, 5, 5);
			imgView.setOnClickListener(this);
			tr.addView(imgView);
			
			if (j % no_cols == 0) {

				table.addView(tr, new TableLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

				tr = new TableRow(this);

			}

		}
	
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	//	getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		
		
		switch(v.getId())
		{
		case 1:
			// case of numbers	
			//Game.class
			
			Intent button_game = new Intent(getApplicationContext(),Game_type_selection.class);
			button_game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			button_game.putExtra("game_type", "normal");
			startActivity(button_game);
			break;
			
			
			
		case 11:
			// case of gallery image. Need to do loading from gallery
			Intent gallery_intent = new Intent(getApplicationContext(), Image_From_Gallery.class);
			gallery_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(gallery_intent);
			break;
			
			
			
			
		case 12:
			// Case of image from url. Need to get file and validate it 
			// Pass control to another activity to take input and load image. 
			Intent url_intent = new Intent(getApplicationContext(), Image_From_URL.class);
			url_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(url_intent);
			break;
			
			
		default:
			// Case of all images
			//Game_Type_Selection.class
			
			Drawable drawable =((ImageView) v).getDrawable();
			BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
			Bitmap bitmap = bitmapDrawable .getBitmap();
			ImageUtility imgUtil = new ImageUtility();
			boolean imageSaved = imgUtil.saveImageToInternalStorage(bitmap, getApplicationContext());
			if(imageSaved)
			{
			Toast.makeText(getApplicationContext(), " Image saved", Toast.LENGTH_SHORT).show();
			Intent image_game= new Intent(getApplicationContext(),Confirm_Image.class);
			image_game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			image_game.putExtra("game_type", "image");
			image_game.putExtra("image", bitmap);
			image_game.putExtra("image_source", "predefined");
			startActivity(image_game);
			break;
			
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Not able to load image", Toast.LENGTH_SHORT).show();
				
				
			}
			
		}
		
	}

}
