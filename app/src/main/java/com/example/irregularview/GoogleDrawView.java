package com.example.irregularview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class GoogleDrawView extends View {

    private static final String TAG = "GoogleDrawView";
    private int[] colors = new int[]
            {0xFFD21D22,0xFFFBD109,0xFF4BB748,0xFF398ED5,Color.WHITE};  //定义google的颜色
    private int cx;    //google圆心x坐标
    private int cy;    //google圆心y坐标
    private int mWidth;  //控件宽度
    private int mHeight; //控件高度

    private Paint mPaint;  //画笔

    private Path mPath;   //线段


    private int wholeRadius = 300; //最外圆半径

    private int outerRadius = 150; //外圆半径

    private float innerRadius = 120; //内圆半径

    private RectF wholeRectF;  //最外圆内接矩形
    private RectF outerRectF;  //外圆内接矩形

    private Bitmap mBitmap; //画板

    private Canvas mCanvas;

    private Matrix matrix;  //用于旋转的变换矩阵


    public GoogleDrawView(Context context) {
        super(context);
    }

    public GoogleDrawView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public GoogleDrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        init();
    }

    public void init(){
        cx = getLeft() + mWidth / 2;
        cy = getTop() + mHeight / 2;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //生成画笔对象并设置抗锯齿

        mPath = new Path();

        wholeRectF = new RectF(cx - wholeRadius,cy - wholeRadius,cx + wholeRadius,cy + wholeRadius);
        outerRectF = new RectF(cx - outerRadius,cy - outerRadius, cx + outerRadius, cy + outerRadius);

        mBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        matrix = new Matrix();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制红色
        mPaint.setColor(colors[0]);
        mPath.addArc(outerRectF,150,120);
        mPath.lineTo((float) (cx + Math.sqrt(3) / 2 * wholeRadius),cy - outerRadius);
        mPath.addArc(wholeRectF,-30,-120);
        mPath.lineTo((float) (cx - Math.sqrt(3) / 2 * outerRadius),cy + outerRadius / 2f);
        mPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawPath(mPath,mPaint);

        //绘制黄色
        mPaint.setColor(colors[1]);
        matrix.setRotate(120,cx,cy);
        mPath.transform(matrix);
        mCanvas.drawPath(mPath,mPaint);

        //绘制绿色
        mPaint.setColor(colors[2]);
        matrix.setRotate(120,cx,cy);
        mPath.transform(matrix);
        mCanvas.drawPath(mPath,mPaint);


        //绘制蓝色
        mPaint.setColor(colors[3]);
        mCanvas.drawCircle(cx,cy,innerRadius,mPaint);

        //绘制内白环
        mPaint.setColor(colors[4]);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outerRadius - innerRadius);
        mCanvas.drawCircle(cx,cy,(outerRadius + innerRadius) / 2,mPaint);
        canvas.drawBitmap(mBitmap,0,0,mPaint);

        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_UP){
            return false;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();

        if(mBitmap == null || x < 0 || y < 0 || x > mWidth || y > mHeight){
            return false;
        }

        int color = mBitmap.getPixel(x,y);
        if(color == Color.TRANSPARENT){
            return false;
        }else if(color == colors[0]){
            setTag(getId(),"红色");
        }else if(color == colors[1]){
            setTag(getId(),"黄色");
        }else if(color == colors[2]){
            setTag(getId(),"绿色");
        }else if(color == colors[3]){
            setTag(getId(),"蓝色");
        }
        return super.onTouchEvent(event);
    }
}
