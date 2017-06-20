package com.android.test.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

import com.android.test.R;

/**
 * 图片轮播的时候，打点的
 */

public class PointView extends TextView{
	
	private int currentPoint;
	private int totalPoint;
	private int itemSpacing;
	private int radius;
	private int normalColor;
	private int selectedColor;

	public PointView(Context context) {
		super(context);
	}

	public PointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		float density = getContext().getResources().getDisplayMetrics().density;
		itemSpacing = (int) (density*5);
		radius = (int) (density*4);
		selectedColor = R.color.white;
		normalColor = R.color.black;
	}
	
	public void setPosition(int currentPosition,int totalPoint){
		this.currentPoint = currentPosition;
		this.totalPoint = totalPoint;
		invalidate();
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public void setItemSpacing(int itemSpacing) {
		this.itemSpacing = itemSpacing;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		invalidate();
	}
	
	public void setNormalColor(int normalColor) {
		this.normalColor = normalColor;
	}

	public void setSelectedColor(int selectedColor) {
		this.selectedColor = selectedColor;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(getMeasuredWidth() > 0 && currentPoint < totalPoint){
			int gravity = getGravity();
			int startPoint = 0;
			if(gravity == Gravity.LEFT){
				startPoint = getPaddingLeft() + radius;
			}else if(gravity == Gravity.RIGHT){
				startPoint = getMeasuredWidth() - totalPoint * 2 * radius
						- (totalPoint - 1) * itemSpacing - getPaddingRight();
			}else {
				startPoint = (getMeasuredWidth() - totalPoint * 2 * radius
						- (totalPoint - 1) * itemSpacing) / 2;
			}
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			for(int i = 0; i < totalPoint; i ++){
				if(i == currentPoint){
					paint.setColor(getContext().getResources().getColor(selectedColor));
				}else{
					paint.setColor(getContext().getResources().getColor(normalColor));
				}
				int start = startPoint + i * (2*radius+itemSpacing)+radius;
				canvas.drawCircle(start, getMeasuredHeight()/2, radius, paint);
			}
		}
	}
}
