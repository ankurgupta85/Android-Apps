package com.example.n_puzzle_project;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Game_type_selection extends Activity  implements OnClickListener{
	private final long startTime = 10 * 1000;
    Intent intent = null;
	int no_rows = 0;
	int no_cols = 0;
	String file_name="";
	String remainingTime="";
	int[] input =null;
	
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	System.gc();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_type_selection);
        
        TextView easy_level = (TextView)findViewById(R.id.easy_level);
        easy_level.setOnClickListener(this);
        
        TextView medium_level = (TextView) findViewById(R.id.medium_level);
        medium_level.setOnClickListener(this);
        
        
        TextView hard_level = (TextView) findViewById(R.id.hard_level);
        hard_level.setOnClickListener(this);
        
	}

	private void redirectActivity(String game_type,Context context)
	{
		// IF game type is normal continue with user option and let user choose which configuration
		
		
		
		// If game type is image, need to let user choose which configuration but needs to take care of image. 
		// Redirect it to page with one block missing. Need to handle stored images(url and gallery) and image passed from Activity(drawable image)
		
	}
	
    private void showMessage(String text)
    {
    	Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
    	toast.show();
    	
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game_type_selection, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		Intent game=null;
		int[] input =null;
		String text ="Something else pressed";
		String path = getFilesDir()+"../shared_prefs";
		SharedPreferences pref = null;
		File f = null;
		String file_path="";

        intent = getIntent();
        String game_type = intent.getStringExtra("game_type");
        
		this.redirectActivity(game_type,getApplicationContext());
		switch(v.getId())
		{
		case R.id.easy_level:
			text = " Easy Level Selected";
			input = new int[]{8,7,6,5,4,3,2,1,-1};
			file_path = path+"/easy.xml";
			f = new File(file_path);
			if(f.exists())
			{
				f.delete();
			}
			game = new Intent(getApplicationContext(), Game.class);
			game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			game.putExtra("inputArray", input);
			game.putExtra("moves_count",0);
			game.putExtra("no_rows", 3);
			game.putExtra("no_cols", 3);
			game.putExtra("file_name", "easy");
			game.putExtra("remaining_time",startTime);
			startActivity(game);
			
			break;
		case R.id.medium_level:
			text = " Medium Level Selected";
			input = new int[]{15,14,13,12,11,10,9,8,7,6,5,4,3,1,2,-1};
			file_path = path+"/medium.xml";
			f = new File(file_path);
			if(f.exists())
			{
				f.delete();
			}
			
			game = new Intent(getApplicationContext(), Game.class);
			game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			game.putExtra("inputArray", input);
			game.putExtra("moves_count",0);
			game.putExtra("no_rows", 4);
			game.putExtra("no_cols", 4);
			game.putExtra("file_name", "medium");
			game.putExtra("remaining_time",startTime);
			startActivity(game);
			
			break;
		case R.id.hard_level:
			text = " Hard Level Selected";
			input = new int[]{24,23,22,21,20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1,-1};
			file_path = path+"/hard.xml";
			f = new File(file_path);
			if(f.exists())
			{
				f.delete();
			}
			
			game = new Intent(getApplicationContext(), Game.class);
			game.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			game.putExtra("inputArray", input);
			game.putExtra("moves_count",0);
			game.putExtra("no_rows", 5);
			game.putExtra("no_cols", 5);
			game.putExtra("file_name", "hard");
			game.putExtra("remaining_time",startTime);
			startActivity(game);
			
			break;
	
		}
		
		this.showMessage(text);
		
	}
    
}
