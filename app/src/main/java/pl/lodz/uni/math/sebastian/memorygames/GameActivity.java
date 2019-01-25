package pl.lodz.uni.math.sebastian.memorygames;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class GameActivity extends AppCompatActivity {

    ArrayList<String> pathList = new ArrayList<String>();
    int[] Array = {0, 0, 1, 1, 2, 2, 3, 3};
    int pointCounter=0;
    long earlierId = 99;
    View earlierView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        shuffleArray(Array);

        final GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                loadImageFromStorage((String) pathList.get(Array[(int) id]), v);
                if (id != earlierId) {
                    if (earlierId != 99) {
                        comparisonView(v, id);
                    } else {
                        earlierId = id;
                        earlierView = v;
                    }
                }
                if(pointCounter==4){
                    Toast.makeText(GameActivity.this, "End Game", Toast.LENGTH_SHORT).show();
                    View button = findViewById(R.id.buttonPlayAgain);
                    button.setVisibility(View.VISIBLE);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        pathList = bundle.getStringArrayList("message");
    }

    private void loadImageFromStorage(String path, View v) {
        try {
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = (ImageView) v;
            img.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void comparisonView(View v, long id) {
        if (Array[(int) earlierId] == Array[(int) id]) {
            ImageView img = (ImageView) v;
            img.setVisibility(View.GONE);
            img = (ImageView) earlierView;
            img.setVisibility(View.GONE);
            pointCounter++;
        } else {
            ImageView img = (ImageView) v;
            img.setImageDrawable(getDrawable(R.drawable.sample_0));
            img = (ImageView) earlierView;
            img.setImageDrawable(getDrawable(R.drawable.sample_0));
        }
        earlierId = 99;
        earlierView = null;
    }

    static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void backToMainActivity(View view) {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
