package com.example.displaydrawable;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case 1:
				
			
		case 11:
			
		case 12:
			
		default:
			
			
		}
		
	}

}
