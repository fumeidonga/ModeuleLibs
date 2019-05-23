

## 类的关系

Activity -> ContextThemeWrapper -> ContextWrapper -> Context (abstract class)

PhoneWindow -> Window (abstract class)

DecorView -> FrameLayout

## setContentView 流程

我们从源码开始，如下，有三个方法
<pre>
    public void setContentView(@LayoutRes int layoutResID) {
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
    }
    
    public void setContentView(View view) {
        getWindow().setContentView(view);
        initWindowDecorActionBar();
    }
    
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getWindow().setContentView(view, params);
        initWindowDecorActionBar();
    }
    
    // mWindow = new PhoneWindow(this, window, activityConfigCallback);
    public Window getWindow() {
        return mWindow;
    }
    
</pre>
继续往下看，到了 PhoneWindow，这里才是setContentView实现的地方

android.view.window.java
<pre>
    public abstract void setContentView(@LayoutRes int layoutResID);
    public abstract void setContentView(View view);
    public abstract void setContentView(View view, ViewGroup.LayoutParams params);
</pre>

com.android.internal.policy.PhoneWindow.java
<pre>
    我们看其中一个方法
    @Override
    public void setContentView(int layoutResID) {
        
        /*
         * 1 
         * mContentParent: null
         * 由于当前activity是新创建的，所以mContentParent为null
         */
        if (mContentParent == null) {
            installDecor();
        } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            mContentParent.removeAllViews();
        }
        ...
    }
    
    2 由于当前activity是新创建的，所以mContentParent为null
    private void installDecor(){
        if (mDecor == null) {
            mDecor = generateDecor(-1);
            ...
        } else {
            mDecor.setWindow(this);
        }
        
        if(mContentParent == null){
            mContentParent = generateLayout(mDecor);
        }
    }
    
    
    3 新建一个decor view， decorview其实是一个Framelayout
    protected DecorView generateDecor(int featureId) {
        ...
        return new DecorView(context, featureId, this, getAttributes());
    }
    
    4 通过一系列的判断，得到相对于的layoutResource,然后通过mLayoutInflater.inflate(layoutResource, null);
    得到这个View,将其加入到mDecor,其中mContentParent最终为一个ID_ANDROID_CONTENT = 
    com.android.internal.R.id.content的一个ViewGroup， 
    
    protected ViewGroup generateLayout(){
    
        ...
        //根据style属性做一些列的判断...

        //在做一些列的判断得到layoutResource
        layoutResource= .... //这里用R.layout.screen_simple来分析
       
        mDecor.startChanging();

        //layoutResource 加入到 mDecor view里面
        mDecor.onResourcesLoaded(mLayoutInflater, layoutResource);

        ViewGroup contentParent = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
        
        if (contentParent == null) {
            throw new RuntimeException(Window couldn't find content container view);
        }
        
        ...
        
        return contentParent;
    
    }
    
    /*
     * 5  得到contentParent 的viewgroup后，调用mLayoutInflater.inflate(layoutResID, mContentParent);
     * setContentView其实就是把那个View加入到一个id为com.android.internal.R.id.content的viewgroup里面，
     */
    @Override
    public void setContentView(int layoutResID) {
        
        if (mContentParent == null) {
            installDecor();
        } else if (!hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            mContentParent.removeAllViews();
        }

        if (hasFeature(FEATURE_CONTENT_TRANSITIONS)) {
            final Scene newScene = Scene.getSceneForLayout(mContentParent, layoutResID,
                    getContext());
            transitionTo(newScene);
        } else {
            /*
             * 5
             */
            mLayoutInflater.inflate(layoutResID, mContentParent);
        }
        ...
    }
    
    
</pre>

Window是一个抽象类，用来描述Activity视图最顶端的窗口显示和行为操作

1. PhoneWindow 持有一个DecorView对象， new DecorView
2. 新建一个mContentParent，这是一个id为@android:id/content的viewGroup，并将mContentParent 添加到DecorView里面
    <pre>
        R.layout.screen_simple
 
         LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:fitsSystemWindows="true"
            android:orientation="vertical"
             ViewStub android:id="@+id/action_mode_bar_stub"
                      android:inflatedId="@+id/action_mode_bar"
                      android:layout="@layout/action_mode_bar"
                      android:layout_width="match_parent"
                      android:layout_height="wrap_content"
                      android:theme="?attr/actionBarTheme" 
             FrameLayout
                 android:id="@android:id/content"
                 android:layout_width="match_parent"
                 android:layout_height="match_parent"
                 android:foregroundInsidePadding="false"
                 android:foregroundGravity="fill_horizontal|top"
                 android:foreground="?android:attr/windowContentOverlay" 
        LinearLayout
    </pre>
3. 将setContentView对应的view添加到mContentParent

![](https://github.com/fumeidonga/markdownPic/blob/master/yuanli/setcontentview.png?raw=true)






















