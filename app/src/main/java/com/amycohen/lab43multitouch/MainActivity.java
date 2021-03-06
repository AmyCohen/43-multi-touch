package com.amycohen.lab43multitouch;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        View.OnTouchListener,
        View.OnLayoutChangeListener{

    @BindView(R.id.image) public ImageView image;
    protected Bitmap bitmap;
    protected Canvas canvas;
    protected MultiTouchEngine engine;
    protected MultiTouchEngineDrawer drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        image.addOnLayoutChangeListener(this);
        image.setOnTouchListener(this);
    }

    @Override
    public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        initBitmpap(view);
    }

    public void initBitmpap(View view) {
        int width = view.getWidth();
        int height = view.getHeight();

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        engine = new MultiTouchEngine();
        drawer = new MultiTouchEngineDrawer(engine, bitmap, canvas, image);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d("ACTIONZZ", "onTouch");
        boolean isActive = false;
        int pointers = motionEvent.getPointerCount();

        int action = motionEvent.getAction();
        int masked = motionEvent.getActionMasked();

        for (int i = 0; i < pointers; i++) {
            Log.d("ACTIONYY", "for loop");
            float xx = motionEvent.getX(i);
            float yy = motionEvent.getY(i);
            int id = motionEvent.getPointerId(i);

            if (action == MotionEvent.ACTION_DOWN || masked == MotionEvent.ACTION_POINTER_DOWN) {
                Log.d("ACTION", "down");
                engine.add(id, xx, yy);
                isActive = true;

            } else if (action == MotionEvent.ACTION_UP || masked == MotionEvent.ACTION_POINTER_UP) {
                Log.d("ACTION", "up");
                engine.remove(id);
                isActive = false;

            } else if (action == MotionEvent.ACTION_MOVE) {
                Log.d("ACTION", "move");
                engine.update(id, xx, yy);
                isActive = true;
            }
        }
        drawer.clear();
        drawer.draw();

        return isActive;
    }
}
