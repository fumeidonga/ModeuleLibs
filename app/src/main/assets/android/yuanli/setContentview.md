

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
            throw new RuntimeException("Window couldn t find content container view");
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

## 我们再讲讲 支持类，AppCompatActivity

<pre>
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
      getDelegate().setContentView(layoutResID);
    }
    
    @NonNull
    public AppCompatDelegate getDelegate() {
      if (mDelegate == null) {
          mDelegate = AppCompatDelegate.create(this, this);
      }
      return mDelegate;
    }
    
    public static AppCompatDelegate create(Activity activity, AppCompatCallback callback) {
      return create(activity, activity.getWindow(), callback);
    }
    private static AppCompatDelegate create(Context context, Window window,
          AppCompatCallback callback) {
      final int sdk = Build.VERSION.SDK_INT;
      if (sdk >= 23) {
          return new AppCompatDelegateImplV23(context, window, callback);
      } else if (sdk >= 14) {
          return new AppCompatDelegateImplV14(context, window, callback);
      } else if (sdk >= 11) {
          return new AppCompatDelegateImplV11(context, window, callback);
      } else {
          return new AppCompatDelegateImplV7(context, window, callback);
      }
    }

</pre>
可以看到最终进入到了AppCompatDelegate的create方法,这个函数通过new  23  14 11  7就可以看出是为了兼容不同的版本,
我们点进去就可以看到AppCompatDelegateImplV23–>AppCompatDelegateImplV14–>AppCompatDelegateImplV11–>AppCompatDelegateImplV7
依次继承的，我们最终查看到AppCompatDelegateImplV7的setContentView函数:
<pre>
    @Override
    public void setContentView(View v) {
        ensureSubDecor();
        ViewGroup contentParent = (ViewGroup) mSubDecor.findViewById(android.R.id.content);
        contentParent.removeAllViews();
        contentParent.addView(v);
        mOriginalWindowCallback.onContentChanged();
    }

    @Override
    public void setContentView(int resId) {
        ensureSubDecor();
        ViewGroup contentParent = (ViewGroup) mSubDecor.findViewById(android.R.id.content);
        contentParent.removeAllViews();
        LayoutInflater.from(mContext).inflate(resId, contentParent);
        mOriginalWindowCallback.onContentChanged();
    }

    @Override
    public void setContentView(View v, ViewGroup.LayoutParams lp) {
        ensureSubDecor();
        ViewGroup contentParent = (ViewGroup) mSubDecor.findViewById(android.R.id.content);
        contentParent.removeAllViews();
        contentParent.addView(v, lp);
        mOriginalWindowCallback.onContentChanged();
    }
</pre>
其实也就是把得到的view添加到contentParent里面。
比如说，在android21以前一般是使用控件TextView等控件，在21以后出了相关的AppCompat控件，这个时候怎么让
开发者写的TextView自动转换为AppCompatTextView呢？所以在AppCompatDelegateImplV7重写了这样一个函数函数
<pre>
    @Override
    public void installViewFactory() {
       LayoutInflater layoutInflater = LayoutInflater.from(mContext);
       if (layoutInflater.getFactory() == null) {
           LayoutInflaterCompat.setFactory(layoutInflater, this);
       } else {
           if (!(LayoutInflaterCompat.getFactory(layoutInflater)
                   instanceof AppCompatDelegateImplV7)) {
               Log.i(TAG, “The Activity‘s LayoutInflater already has a Factory installed“
                       + “ so we can not install AppCompat‘s“);
           }
       }
   }
</pre>
给LayoutInflater设置一个Factory,可以拦截View的生成，通过这个Factory我们可以自己给inflate写一套解析layout.xml的规则

AppCompatViewInflater.java
<pre>
 switch (name) {
            case “TextView“:
                view = new AppCompatTextView(context, attrs);
                break;
            case “ImageView“:
                view = new AppCompatImageView(context, attrs);
                break;
            ...
        }
</pre>
这样就达到了将以前的TextView等转换为相关的AppCompat控件，达到兼容





