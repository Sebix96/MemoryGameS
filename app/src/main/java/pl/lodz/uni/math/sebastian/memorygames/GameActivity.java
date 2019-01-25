package pl.lodz.uni.math.sebastian.memorygames;

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

    ArrayList<String> patchList = new ArrayList<String>();
    int[] Array = {0, 0, 1, 1, 2, 2, 3, 3};
    long earlierId = 99;
    View earlierView=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        shuffleArray(Array);

        final GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                if(id!=earlierId){
                    loadImageFromStorage((String) patchList.get(Array[(int) id]), v);
                    if (earlierId != 99) {
                        if (Array[(int) earlierId] == Array[(int) id]) {
                            ImageView img = (ImageView) v;
                            img.setVisibility(View.GONE);
                            img = (ImageView) earlierView;
                            img.setVisibility(View.GONE);
                            earlierId = id;
                            earlierView = v;
                        } else {
                            ImageView img = (ImageView) v;
                            img.setImageDrawable(getDrawable(R.drawable.sample_0));
                            img = (ImageView) earlierView;
                            img.setImageDrawable(getDrawable(R.drawable.sample_0));
                            earlierId = 99;
                            earlierView = null;
                        }
                    }
                    else
                    {
                        earlierId = id;
                        earlierView = v;
                    }

                }


                Toast.makeText(GameActivity.this, "" + id,
                        Toast.LENGTH_SHORT).show();
            }
        });
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        patchList = bundle.getStringArrayList("message");

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


}
