package sdejesus.portfolio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

/**
 * Created by sdejesus on 7/9/16.
 */

public class ImageFullActivity extends AppCompatActivity {
    public static final String PARAMETER_IMAGE = "IMAGE_PARAMETER";
    private ImageView mImageView;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.activity_image_full);
        setGraphics();

        if(getIntent() != null){
            int drawableResourceId = getApplicationContext().getResources().getIdentifier(getIntent().getStringExtra(PARAMETER_IMAGE), "drawable"
                    , getApplicationContext().getPackageName());
            mImageView.setImageResource(drawableResourceId);

        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            supportFinishAfterTransition();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
    public void setGraphics(){
        mImageView = (ImageView) findViewById(R.id.item_project_gallery_image);
        mToolbar = (Toolbar) findViewById(R.id.activity_imagefull_toolbar);
    }
}
