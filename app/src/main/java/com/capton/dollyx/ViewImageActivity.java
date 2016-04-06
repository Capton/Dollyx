package com.capton.dollyx;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class ViewImageActivity extends Activity implements OnClickListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private AnimationListener mAnimationListener;
    private Context mContext;
    private MediaPlayer mediaPlayer;
    private FloatingActionButton fab;
    ArrayList<Bitmap> data;
    ImageView play, stop, next, previous;
    private static String PLAY_TAG = "play";
    private static String IMG_POSITION_TAG = "position";
    int play_status;
    int imagePosition;



    //@SuppressWarnings("deprecation")
    private final GestureDetector detector = new GestureDetector(new SwipeGestureDetector());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        play = (ImageView)findViewById(R.id.play);
        stop = (ImageView)findViewById(R.id.stop);
        next = (ImageView)findViewById(R.id.view_imgswipe_right);
        previous = (ImageView)findViewById(R.id.view_img_swipe_left);
        mContext = this;
        imagePosition = getIntent().getIntExtra(IMG_POSITION_TAG, 0);
        play_status = getIntent().getIntExtra(PLAY_TAG, 1);
        data = getData();
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);
        mediaPlayer = MediaPlayer.create(this, R.raw.song2);
        fab = (FloatingActionButton)findViewById(R.id.view_image_fab);
        Snackbar.make(fab, R.string.birthday_msg, Snackbar.LENGTH_INDEFINITE)
                .setAction("Action", null).show();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int index = 0; index<data.size(); index++) {

            View view = inflater.inflate(R.layout.image_view_layout, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_view_selected);
            view.setTag(imageView);
            imageView.setImageBitmap(data.get(index));
            mViewFlipper.addView(view, index);
        }
        mViewFlipper.setDisplayedChild(imagePosition);

        mViewFlipper.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(final View view, final MotionEvent event) {
                detector.onTouchEvent(event);
                return true;
            }
        });
        play.setOnClickListener(this);
        stop.setOnClickListener(this);
        fab.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);

        if(play_status == 1)
            slideShow(true);


        //animation listener
        mAnimationListener = new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                //animation started event
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                //TODO animation stopped event
            }
        };
    }

    private void slideShow(boolean play_status) {
        if (play_status){
            //sets auto flipping
            //mViewFlipper.setAutoStart(true);
            mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
            mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
            // controlling animation
            mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
            mViewFlipper.setFlipInterval(4000);
            mViewFlipper.startFlipping();
            play.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
            mediaPlayer.start();
        }
        else{
            //stop auto flipping
            mViewFlipper.stopFlipping();
            stop.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
            mediaPlayer.pause();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                slideShow(true);
                break;
            case R.id.stop:
                slideShow(false);
                break;
            case R.id.view_image_fab:
                shareImage();
                break;
            case R.id.view_img_swipe_left:
                flipPrevious();
                break;
            case R.id.view_imgswipe_right:
                flipNext();
                break;
        }
    }

    private void shareImage() {
        Uri imageUri = getImageUri(mViewFlipper.getDisplayedChild());
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("image/");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.sent_to)));
    }

    private Uri getImageUri(int index) {
        Uri uri = Uri.EMPTY;
        switch (++index){
            case 1:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly6");
                break;

            case 2:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly0");
                break;

            case 3:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly1");
                break;
            case 4:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly3");
                break;

            case 5:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly4");
                break;

            case 6:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly5");
                break;

            case 7:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly7");
                break;

            case 8:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly8");
                break;

            case 9:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly9");
                break;

            case 10:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly10");
                break;

            case 11:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly11");
                break;

            case 12:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly12");
                break;
            default:
                uri = Uri.parse("android.resource://"+getPackageName()+"/drawable/dolly6");
                break;
        }
        return uri;
    }






    private ArrayList<Bitmap> getData() {
        final ArrayList<Bitmap> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));

            imageItems.add(bitmap);
        }
        return imageItems;
    }


    class SwipeGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    return flipNext();
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    return flipPrevious();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }

    private boolean flipPrevious() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
        // controlling animation
        mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
        mViewFlipper.showPrevious();
        return true;
    }

    private boolean flipNext() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
        // controlling animation
        mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
        mViewFlipper.showNext();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(play.isShown())
            mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(play.isShown())
            mediaPlayer.start();
    }
}



