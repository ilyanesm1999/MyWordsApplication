package soft.example.com.mywordsapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

/**
 * Created by admin on 27.12.2015.
 */
public class MyView extends Button {


    public String letter;
    Context context;
    public static final int VIEW_WIDTH = 120, VIEW_HEIGHT = VIEW_WIDTH;
    public boolean touched = false;
    public boolean filled = false;
    public boolean founded = false;

    public int looseColor;

    public MyView(Context context)
    {
        super(context);
        this.context = context;
        setWidth(VIEW_WIDTH);
        setHeight(VIEW_HEIGHT);
    }



    public void setLetter(String letter, int color){
        this.letter = letter;
        setText(letter);
        setTextColor(color);
    }




}
