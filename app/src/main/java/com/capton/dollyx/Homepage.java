package com.capton.dollyx;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    ProportionalImageView imageView;
    ImageView prevView, nextView;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private ToggleButton toggleButton;
    private MediaPlayer mediaPlayer;
    private Animation.AnimationListener mAnimationListener;
    private Context mContext;
    ArrayList<Bitmap> data;
    ImageView play, stop;
    private static String PLAY_TAG = "play";
    private static String IMG_POSITION_TAG = "position";
    int play_status;
    int imagePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toggleButton = (ToggleButton)findViewById(R.id.toggle_btn);
        prevView = (ImageView)findViewById(R.id.swipe_left);
        nextView = (ImageView)findViewById(R.id.swipe_right);
        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        play = (ImageView)findViewById(R.id.play);
        stop = (ImageView)findViewById(R.id.stop);
        mContext = this;
        imagePosition = getIntent().getIntExtra(IMG_POSITION_TAG, 0);
        play_status = getIntent().getIntExtra(PLAY_TAG, 1);
        data = getData();

        mViewFlipper = (ViewFlipper) this.findViewById(R.id.main_image_flipper);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int index = 0; index<data.size(); index++) {

            View view = inflater.inflate(R.layout.image_view_layout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view_selected);
            view.setTag(imageView);
            imageView.setImageBitmap(data.get(index));
            mViewFlipper.addView(view, index);
        }
        mediaPlayer.start();
        mediaPlayer.pause();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.start();
            }
        });
        toggleButton.setOnClickListener(this);
        prevView.setOnClickListener(this);
        nextView.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_startpage) {

        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(Homepage.this, ImagesGridViewActivity.class));
        } else if (id == R.id.nav_makewish) {
            Toast.makeText(getApplicationContext(), R.string.birthday_msg, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_manage) {
            //startActivity(new Intent(this, SettingsActivity.class));

        }  else if (id == R.id.nav_quit) {
            this.finish();
        } else if (id == R.id.nav_about){
            startActivity(new Intent(this, AboutActivity.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.toggle_btn) {
            if(toggleButton.isChecked())
                mediaPlayer.start();
            else mediaPlayer.pause();
            slideShow(toggleButton.isChecked());

        }
        else if(id == R.id.swipe_left){
            Snackbar.make(prevView, R.string.birthday_msg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if(id == R.id.swipe_right){
            Snackbar.make(prevView, R.string.birthday_msg, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
        else if(id == R.id.fab){
            startActivity(new Intent(this, ViewImageActivity.class));
        }

    }
    private ArrayList<Bitmap> getData() {
        final ArrayList<Bitmap> bitmaps = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.frame_gif_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));

            bitmaps.add(bitmap);
        }
        return bitmaps;
    }
    private void slideShow(boolean yes) {
        if (yes){
            //sets auto flipping
            //mViewFlipper.setAutoStart(true);
            mViewFlipper.setFlipInterval(500);
            mViewFlipper.startFlipping();
        }
        else{
            //stop auto flipping
            mViewFlipper.stopFlipping();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(toggleButton.isChecked())
            mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(toggleButton.isChecked())
            mediaPlayer.start();
    }
}
