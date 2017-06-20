package com.android.test.banner;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.test.ImagePath;
import com.android.test.R;
import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by AiMr on 2017/6/15.
 */
public class Banner extends FrameLayout{
    private ViewPager viewPager;
    private PointView pointView;
    private ImageView defaultView;

    private Context context;
    private int density;
    private List<HashMap<String,String>> mList;
    private Handler handler;
    private PagerAdapter adapter;
    public Banner(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView(){
        setClipChildren(false);
        density = (int) context.getResources().getDisplayMetrics().density;
        mList = new ArrayList<>();
        handler = new Handler();

        viewPager = new ViewPager(context);
        pointView = new PointView(context);
        defaultView = new ImageView(context);

        //设置默认图片
        defaultView.setImageResource(R.drawable.a);

        //参数设置
        FrameLayout.LayoutParams layoutParams1 = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        layoutParams1.leftMargin = 50 * density;
        layoutParams1.rightMargin = 50 * density;
        layoutParams1.topMargin = 20 * density;
        layoutParams1.bottomMargin = 20 * density;
        addView(viewPager,layoutParams1);

        layoutParams1 = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,10 * density);
        layoutParams1.gravity = Gravity.BOTTOM;
        addView(pointView,layoutParams1);

        addView(defaultView,new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
        defaultView.setVisibility(GONE);

        //表示页与页之间的距离
        viewPager.setPageMargin(density*30);
        //是否重加载 共显示3个页面  viewPager有预加载的机制
        viewPager.setOffscreenPageLimit(3);
        adapter = new PageAdapter();
        viewPager.setAdapter(adapter);
        //设置滑出去的视图效果
        viewPager.setPageTransformer(true,new ScaleInTransformer());
        //打点控件设置选中和没选择的颜色
        pointView.setNormalColor(R.color.white);
        pointView.setSelectedColor(R.color.colorAccent);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //ViewPager滑动变化通知打点
                pointView.setPosition(
                        mList.size() == 0 ? 0 : (position) % mList.size(),
                        mList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    public void setData(List<HashMap<String,String>> list){
        stopRun();
        if(list != null && list.size()>0){
            setBannerVisible(GONE);
            mList.clear();
            mList.addAll(list);
            adapter.notifyDataSetChanged();

            //死循环轮播算法
            int mod = mList.size() == 0?0:
                    (adapter.getCount() / 2)%mList.size();
            viewPager.setCurrentItem(adapter.getCount() / 2 - mod);
            //设置当前位置
            pointView.setPosition(0,mList.size());
        }else {
            setBannerVisible(VISIBLE);
        }
    }

    class PageAdapter extends PagerAdapter {
        private List<RoundImageView> imageViews;

        public PageAdapter(){
            imageViews = new ArrayList<>();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //配合预加载 使用矩阵圆角控件
            RoundImageView imageView;
            GradientDrawable gd = new GradientDrawable();//创建drawable
            gd.setColor(getResources().getColor(R.color.white));
            gd.setCornerRadius(3*5*density);
            if(imageViews.size() > 0){
                imageView = imageViews.get(0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.setBackground(gd);
                } else {
                    imageView.setElevation(20*density);
                    imageView.setBackgroundDrawable(gd);
                }
                imageViews.remove(0);
            }else {
                imageView = new RoundImageView(context);
                imageView.setType(RoundImageView.TYPE_ROUND);
                imageView.setBorderRadius(5*density);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            }


            container.addView(imageView);

            //默认的ViewPager中，会是有限的视图，虚拟成为10000张，因为有预加载机制，所以不会内存溢出，左右都可以不停的滑，无底洞的效果
            final int pos = mList.size() != 0 ? position % mList.size() : 0;
            //解决滑动的时候，停止自动轮播，松开继续自动轮播
            imageView.setOnTouchListener(new View.OnTouchListener() {
                private long downTime;
                private int downX;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.removeCallbacksAndMessages(null);
                            // 按下去时，记录按下的坐标和时间，用于判断是否是点击事件
                            downX = (int) event.getX();
                            downTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            // 抬起手指时，判断落下抬起的时间差和坐标，符合以下条件为点击
                            if (System.currentTimeMillis() - downTime < 500
                                    && Math.abs(downX - event.getX()) < 30) {
                                //跳转、点击事件被触发
                                Toast.makeText(context,"被选中了"+pos,Toast.LENGTH_SHORT).show();
                            }else{
                                handler.postDelayed(runnable,1500);
                            }
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            handler.postDelayed(runnable,1500);
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            });
            //Glide库加载图片
            Glide.with(context).load(ImagePath.SERVER_IMAGE+mList.get(pos).get("picUrl")).into(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
            imageViews.add((RoundImageView)object);
        }

        @Override
        public int getCount() {
            //模拟10000张 无底洞滑
            return mList.size() < 2?mList.size():100000;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    //轮播
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                //自动轮播速度
                Field field = viewPager.getClass().getDeclaredField("mScroller");
                field.setAccessible(true);
                FixedSpeedScroller scroller = new FixedSpeedScroller(
                        context, new AccelerateInterpolator());
                field.set(viewPager, scroller);
                scroller.setmDuration(500);
            }catch (Exception e) {
                e.printStackTrace();
            }
            //页面改变
            if(viewPager != null){
                if(viewPager.getAdapter().getCount() > 1){
                    int nextPosition = viewPager.getCurrentItem() + 1;
                    if (nextPosition >= viewPager.getAdapter().getCount()) {
                        int mod = mList.size() == 0 ? 0 :
                                (adapter.getCount() / 2) % mList.size();
                        nextPosition = adapter.getCount() / 2 + mod;
                    }
                    viewPager.setCurrentItem(nextPosition);
                }
            }
            handler.postDelayed(this,1500);
        }
    };

    /** 开始轮播  */
    public void startRun(){
        if(handler != null && mList.size() > 0){
            //先清掉再继续任务 配合生命周期
            handler.removeCallbacksAndMessages(null);
            handler.postDelayed(runnable,1500);
        }
    }

    /** 停止轮播 */
    public void stopRun(){
        if(handler != null)
            handler.removeCallbacksAndMessages(null);
    }

    /** 默认图片显示与隐藏  */
    public void setBannerVisible(int state){
        if(state == INVISIBLE)
            state = GONE;
        defaultView.setVisibility(state);
    }
}
