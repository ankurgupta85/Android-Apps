package com.example.n_puzzle_project;

 
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
 
public class Image_From_Gallery extends Activity implements OnClickListener{
 
    private static final int SELECT_PICTURE = 1;
 
    private String selectedImagePath;
    private ImageView img;
    Button continue_button = null;
    Button browse_button = null;
    Bitmap image=null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__from__gallery);
        continue_button = (Button) findViewById(R.id.continue_game);
        continue_button.setEnabled(false);
        img = (ImageView)findViewById(R.id.ImageView01);
        browse_button = (Button) findViewById(R.id.browse_gallery);
        browse_button.setOnClickListener(this);
        continue_button.setOnClickListener(this);
        
    }
 
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
               // Bitmap image = BitmapFactory.decodeFile(selectedImagePath);
                BitmapFactory.Options options = new BitmapFactory.Options();

                options.inSampleSize =1;

                image = BitmapFactory.decodeFile(selectedImagePath,options);
                
                
                img.setImageBitmap(image);
                continue_button.setEnabled(true);
            }
        }
    }
 
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.browse_gallery:
		{
			Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            break;
		}
		
		case R.id.continue_game:
			if(img.getDrawable() == null)
			{
				Toast.makeText(getApplicationContext(), "Please load image first", Toast.LENGTH_SHORT).show();
				continue_button.setEnabled(false);
				
			}
			else
			{
				
				ImageUtility imgUtil = new ImageUtility();
				boolean imageSaved = imgUtil.saveImageToInternalStorage(image, getApplicationContext());
				if(imageSaved)
				{
				System.out.println("Image Saved");
				Intent image_game= new Intent(getApplicationContext(),Game_type_selection.class);
				image_game.putExtra("game_type", "image");
				image_game.putExtra("image_source", "gallery");
				startActivity(image_game);
				}
				else
				{
					System.out.println("Not able to save image");
				}
			}
				
		}
		
		
	}
}