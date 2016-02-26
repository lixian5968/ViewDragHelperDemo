# ViewDragHelperDemo
demo1:最基本的移动 demo2：图片移动 参考 http://www.devdiv.com/Android-_viewflipper_-thread-125943-1-1.html demo3：图片移动原理解析


1：图片划动的demo 
判断是否拦截 监听：

onInterceptTouchEvent

demo：如果一个button点击事件，
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
return mDragger.shouldInterceptTouchEvent(event); 会判断是否拦截（true 拦截）

这里是在 
new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                if (child == My_Button){
                    return false;
                }


                return true;
            }

ViewDragHelper.Callback()里面的 tryCaptureView判断的 ，如果返回true 表示监听拦截



二：
在onInterceptTouchEvent的时候已经重新定位图片的显示层次了，这样方便 数据移动与加载 同步