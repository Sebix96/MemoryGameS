package pl.lodz.uni.math.sebastian.memorygames;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ArrayList<String> pathList = new ArrayList<String>();
    private int picturesCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String path = saveToInternalStorage(imageBitmap);
            pathList.add(path);
            if (picturesCounter == 4) {
                View buttonPlay = findViewById(R.id.buttonPlay);
                buttonPlay.setVisibility(View.VISIBLE);
                View pictureButton = findViewById(R.id.pictureButton);
                pictureButton.setVisibility(View.GONE);
                View textView = findViewById(R.id.textView);
                textView.setVisibility(View.GONE);
            } else {
                Toast.makeText(MainActivity.this, "Take " + (4 - picturesCounter) + " more pictures !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        Date currentTime = Calendar.getInstance().getTime();
        File mypath = new File(directory, currentTime.toString() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        picturesCounter++;
        return directory.getAbsolutePath() + "/" + currentTime.toString() + ".jpg";
    }

    public void runGameActivity(View view) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("message", pathList);
        startActivity(intent);
    }

}
