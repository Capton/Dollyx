package com.capton.dollyx;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ViewImageActivity extends Activity implements OnClickListener {

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private ViewFlipper mViewFlipper;
    private AnimationListener mAnimationListener;
    private Context mContext;
    ArrayList<ImageItem> data;
    ImageView play, stop;
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
        mContext = this;
        imagePosition = getIntent().getIntExtra(IMG_POSITION_TAG, 0);
        play_status = getIntent().getIntExtra(PLAY_TAG, 1);
        data = getData();
        mViewFlipper = (ViewFlipper) this.findViewById(R.id.view_flipper);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for(int index = 0; index<data.size(); index++) {
            Log.d("ViewImageActivity", Integer.toString(index));
            ViewHolder holder = new ViewHolder();

            View view = inflater.inflate(R.layout.image_view_layout, null);
            holder.imageTitle = (TextView) view.findViewById(R.id.image_selected_title);
            holder.image = (ImageView) view.findViewById(R.id.image_view_selected);
            view.setTag(holder);
            ImageItem item = data.get(index);
            holder.imageTitle.setText(item.getTitle());
            holder.image.setImageBitmap(item.getImage());
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
        if(play_status == 1)
            slideShow(1);

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

    private void slideShow(int i) {
        if (i == 1){
            //sets auto flipping
            //mViewFlipper.setAutoStart(true);
            mViewFlipper.setFlipInterval(4000);
            mViewFlipper.startFlipping();
            play.setVisibility(View.INVISIBLE);
            stop.setVisibility(View.VISIBLE);
        }
        else{
            //stop auto flipping
            mViewFlipper.stopFlipping();
            stop.setVisibility(View.INVISIBLE);
            play.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play:
                slideShow(1);
                break;
            case R.id.stop:
                slideShow(0);
                break;
        }
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }


    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        final ArrayList<ViewHolder> viewItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));

            viewItems.add(new ViewHolder());
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }


    class SwipeGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                // right to left swipe
                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext, R.anim.left_out));
                    // controlling animation
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showNext();
                    return true;
                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(mContext, R.anim.right_in));
                    mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(mContext,R.anim.right_out));
                    // controlling animation
                    mViewFlipper.getInAnimation().setAnimationListener(mAnimationListener);
                    mViewFlipper.showPrevious();
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }
    }
}



