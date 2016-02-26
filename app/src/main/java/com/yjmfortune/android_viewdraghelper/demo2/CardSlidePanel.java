package com.yjmfortune.android_viewdraghelper.demo2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yjmfortune.android_viewdraghelper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixian on 2016/2/17.
 */
public class CardSlidePanel extends ViewGroup {

    private ViewDragHelper mDragger;
    private Object obj1 = new Object();
    private OnClickListener btnListener;      // 点击监听
    boolean ClickOnce = true;

    public CardSlidePanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        mDragger = ViewDragHelper.create(this, 10f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (child == My_Button){
                    return false;
                }


                return true;
            }


            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

                int index = viewList.indexOf(changedView);
                if (index + 2 >= viewList.size()) {
                    return;
                }

                processLinkageView(changedView);
            }

            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild instanceof ImageView) {
                    //移动 Image
                    MoveImage(releasedChild, xvel, yvel);
                }

            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return 256;
            }


            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                return left;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                return top;
            }

        });
        btnListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ClickOnce) {
                    ImageView view = viewList.get(0);
                    int finalX = -view.getWidth();
                    if (finalX != 0) {
                        synchronized (obj1) {
//                        if (mDragger.smoothSlideViewTo(view, finalX, view.getWidth() + allHeight)) {
                            if (mDragger.smoothSlideViewTo(view, 0, allHeight)) {
                                Log.e("lx", "finalX:" + 0 + ",finalY:" + finalX);
                                ViewCompat.postInvalidateOnAnimation(CardSlidePanel.this);
                                RemoveviewList.add(view);
//                                ClickOnce = false;
                                Log.e("lxClickOnce_onClick",ClickOnce+"");
                            }
                        }
                    }
                }

            }
        };


    }

    //移动 布局
    private void MoveImage(View releasedChild, float xvel, float yvel) {
        //移动的距离
        int dy = releasedChild.getTop() - imageHight;
        int dx = releasedChild.getLeft() - imageWidth;
        if (Math.abs(dy) + Math.abs(dx) > 100) {
            int finalX = 0;
            int finalY = 0;
            if (dx > 0) {
                //向右
                if (dy > 0) {
                    //向下
                    finalX = getWidth();
                    finalY = getHeight();
                } else {
                    //向上
                    finalX = getWidth();
                    finalY = 0 - releasedChild.getHeight();

                }
            } else {
                //向左
                if (dy > 0) {
                    //向下
                    finalX = 0;
                    finalY = getHeight();
                } else {
                    //向上
                    finalX = 0 - releasedChild.getWidth();
                    finalY = 0 - releasedChild.getHeight();

                }
            }
            // 2. 启动动画
            if (mDragger.smoothSlideViewTo(releasedChild, finalX, finalY)) {
                Log.e("lx", "finalX:" + finalX + ",finalY:" + finalY);
                ViewCompat.postInvalidateOnAnimation(CardSlidePanel.this);
                RemoveviewList.add((ImageView) releasedChild);
            }
        }
    }

    @SuppressLint("WrongCall")
    @Override
    public void computeScroll() {
        //正在滑动  重新加载图片
        if (mDragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        } else {
            // 动画结束
            synchronized (this) {

                if (mDragger.getViewDragState() == ViewDragHelper.STATE_IDLE) {
                    //设置位置
                    SetPosition();
//                    ClickOnce = true;
                    Log.e("lxClickOnce_compute",ClickOnce+"");
                }


            }
        }
    }

    //重新设置位置
    private void SetPosition() {
        if (RemoveviewList.size() > 0) {
            for (int i = viewList.size() - 1; i > 0; i--) {
                View tempView = viewList.get(i);
                tempView.bringToFront();
                //显示到最前面
            }
            ImageView view = RemoveviewList.get(0);
            viewList.remove(view);
            if (show >= imageIds.length) {
                show = 0;
            }
//            view.setImageResource(imageIds[show]);
            ImageLoader.getInstance().displayImage(imagePaths[show], view);
            show++;
            viewList.add(view);
//            onLayout(false, 0, 0, getWidth(), getHeight());
            RemoveviewList.remove(0);

        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(
                resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
    }

    public static int resolveSizeAndState(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }


    int imageWidth;
    int imageHight;


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        allHeight = bottom;
        imageHight = bottom / 5;
        imageWidth = right / 4;
        for (int i = 0; i < viewList.size(); i++) {
            int childHeight = viewList.get(i).getMeasuredHeight();
            viewList.get(i).layout(imageWidth, imageHight, imageWidth * 3, imageHight * 3);
            viewList.get(i).offsetTopAndBottom(i * 40);
        }
        My_Button.layout((int) (imageWidth * 1.5), 4 * imageHight, (int) (imageWidth * 2.5), imageHight * 5);
    }

    View My_Button;
    int allHeight = 0;
    List<ImageView> viewList = new ArrayList<ImageView>();
    List<ImageView> RemoveviewList = new ArrayList<ImageView>();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        viewList.clear();
        int number = getChildCount();
        for (int i = number - 1; i >= 0; i--) {
            View childView = getChildAt(i);
            if (childView.getId() == R.id.my_Button) {
                My_Button = childView;
                My_Button.setOnClickListener(btnListener);
            } else {
                viewList.add((ImageView) childView);
            }
        }
    }

    int[] imageIds;
    int show = 0;
    String imagePaths[];

    public void setImageID(int[] imageIds, String[] imagePaths) {
        this.imageIds = imageIds;
        this.imagePaths = imagePaths;
        for (int i = 0; i < viewList.size(); i++) {
//            viewList.get(i).setImageResource(imageIds[i]);
            ImageLoader.getInstance().displayImage(imagePaths[i], viewList.get(i));
        }
        show = viewList.size();

    }


    private void processLinkageView(View changedView) {
        int changeIndex = viewList.indexOf(changedView);
        View ajustView = viewList.get(changeIndex + 1);
        View ajustView2 = viewList.get(changeIndex + 2);
        if (imageHight != ajustView.getTop()) {
            ajustView.offsetTopAndBottom(-40);
        }
        if ((imageHight + 40) != ajustView2.getTop()) {
            ajustView2.offsetTopAndBottom(-40);
        }


    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            // ACTION_DOWN的时候就对view重新排序
            SetPosition();

            // 保存初次按下时arrowFlagView的Y坐标
            // action_down时就让mDragHelper开始工作，否则有时候导致异常
            mDragger.processTouchEvent(event);
        }

        return mDragger.shouldInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }


}
