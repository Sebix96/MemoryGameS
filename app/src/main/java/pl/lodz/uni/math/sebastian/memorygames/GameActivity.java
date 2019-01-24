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

public class GameActivity extends AppCompatActivity {

    ArrayList<String> patchList = new ArrayList<String>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final GridView gridview = (GridView) findViewById(R.id.gridView);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                loadImageFromStorage((String) patchList.get(i), v);
                i++;
                if (i > 3) {
                    i = 0;
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


}
