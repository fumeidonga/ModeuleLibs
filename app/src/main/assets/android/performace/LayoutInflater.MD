
![](https://github.com/fumeidonga/markdownPic/blob/master/performance/createviw1.png?raw=true)

#### LyoutInflater 构造方法

<pre>

// 1
public View inflate(int resource, ViewGroup root) {
    // root不为空时，attachToRoot默认为true
    return inflate(resource, root, root != null);
}

// 2
public View inflate(int resource, ViewGroup root, boolean attachToRoot) {
    XmlResourceParser parser = getContext().getResources().getLayout(resource);
    try {
        return inflate(parser, root, attachToRoot);
    } finally {
        parser.close();
    }
}

// 3
public View inflate(XmlPullParser parser, ViewGroup root) {
    // root不为空时，attachToRoot默认为true
    return inflate(parser, root, root != null);
}

前三个方法最终调用的都是 4

// 4
public View inflate(XmlPullParser parser, ViewGroup root, boolean attachToRoot) {
    ...
}
</pre>

* root == null， attachToRoot无用
> 此时传进来的布局会被加载成为一个View并直接返回；  
> 布局根View的android:layout_xxx属性会被忽略,只能显示默认的大小

* root != null， attachToRoot == false
> 传进来的布局会被加载成为一个View并直接返回。  
> 布局根View的android:layout_xxx属性会被解析成LayoutParams并保留。  
> (root只用来参与生成布局根View的LayoutParams)

* root != null， attachToRoot == true
> 传进来的布局会被加载成为一个View并作为子View添加到root中，最终返回
root；  
> 而且这个布局根节点的android:layout_参数会被解析用来设置View的大小

#### 我们来看下源码
1
<pre>

    首先是对xml视图进行解析，XmlResourceParser 包含视图层次结构描述的XML dom节点

    public View inflate(@LayoutRes int resource, @Nullable ViewGroup root, boolean attachToRoot) {
        final Resources res = getContext().getResources();
        if (DEBUG) {
            Log.d(TAG, "INFLATING from resource: \"" + res.getResourceName(resource) + "\" ("
                    + Integer.toHexString(resource) + ")");
        }
        
        1
        final XmlResourceParser parser = res.getLayout(resource);
        try {
            
            6
            
            return inflate(parser, root, attachToRoot);
        } finally {
            parser.close();
        }
    }   
</pre>

Resources.java  ResourcesImpl.java
<pre>

    2
    public XmlResourceParser getLayout(@LayoutRes int id) throws NotFoundException {
        return loadXmlResourceParser(id, "layout");
    }
    
    3 加载指定文件的XML解析器
    XmlResourceParser loadXmlResourceParser(@AnyRes int id, @NonNull String type)
            throws NotFoundException {
        final TypedValue value = obtainTempTypedValue();
        try {
            final ResourcesImpl impl = mResourcesImpl;
            impl.getValue(id, value, true);
            if (value.type == TypedValue.TYPE_STRING) {
            
                4
                
                return impl.loadXmlResourceParser(value.string.toString(), id,
                        value.assetCookie, type);
            }
            ...
        } finally {
            releaseTempTypedValue(value);
        }
    }
    
    4 这是一个io操作
    XmlResourceParser loadXmlResourceParser(@NonNull String file, @AnyRes int id, int assetCookie,
            @NonNull String type)
            throws NotFoundException {
        if (id != 0) {
            try {
                ...

                5
                
                final XmlBlock block = mAssets.openXmlBlockAsset(assetCookie, file);
                。。。
            } catch (Exception e) {
            
            }
        }
        
        。。。
    }
</pre>

5

AssetManager.java
<pre>

    @NonNull XmlBlock openXmlBlockAsset(int cookie, @NonNull String fileName) throws IOException {
        Preconditions.checkNotNull(fileName, "fileName");
        synchronized (this) {
            
            ...
            final long xmlBlock = nativeOpenXmlAsset(mObject, cookie, fileName);
            ...
            return block;
        }
    }
</pre>

<pre>

    6
    
    public View inflate(XmlPullParser parser, @Nullable ViewGroup root, boolean attachToRoot) {
        synchronized (mConstructorArgs) {
            
            ...

            7
        
            // Temp is the root view that was found in the xml
            final View temp = createViewFromTag(root, name, inflaterContext, attrs);
    
            ...
    
            // Inflate all children under temp against its context.
            rInflateChildren(parser, temp, attrs, true);
    
    
            // We are supposed to attach all the views we found (int temp)
            // to root. Do that now.
            if (root != null && attachToRoot) {
                root.addView(temp, params);
            }
            ...
    }
    
    7
    
    View createViewFromTag(View parent, String name, Context context, AttributeSet attrs,
            boolean ignoreThemeAttr) {
            
        ...    
        
        8
        
        if (-1 == name.indexOf(‘.‘)) {
            view = onCreateView(parent, name, attrs);
        } else {
            view = createView(name, null, attrs);
        }
        
        ...
    }
    
    8 这里使用反射的方式创建了一个view
    
    public final View createView(String name, String prefix, AttributeSet attrs)
            throws ClassNotFoundException, InflateException {
         
            ...
            
            final View view = constructor.newInstance(args);
            if (view instanceof ViewStub) {
                // Use the same context when inflating ViewStub later.
                final ViewStub viewStub = (ViewStub) view;
                viewStub.setLayoutInflater(cloneInContext((Context) args[0]));
            }
            mConstructorArgs[0] = lastContext;
            return view;
            
            ...
    }
</pre>

所以这里是存在性能点的

#### LyoutInflater 原理

其实就是从根节点开始，递归解析xml的每个节点，每一步递归的过程是：
通过节点名称（全类名），使用ClassLoader创建对应类的实例，也就是View，
然后，将这个View添加到它的上层节点（父View）。并同时会解析对应xml
节点的属性作为View的属性。每个层级的节点都会被生成一个个的View，
并根据View的层级关系add到对应的直接父View（上层节点）中，最终返回一
个包含了所有解析好的子View的布局根View。


#### LyoutInflater Factory

我们再上门看inflate的代码时，可以看到如下的代码
<pre>
    View view;
    if (mFactory2 != null) {
        // ① 有mFactory2，则调用mFactory2的onCreateView方法
        view = mFactory2.onCreateView(parent, name, context, attrs);
    } else if (mFactory != null) {
        // ② 有mFactory，则调用mFactory的onCreateView方法
        view = mFactory.onCreateView(name, context, attrs);
    } else {
        view = null;
    }
</pre>
正常情况下这个Factory是空的,通过 LayoutInflater 创建View时候的一个回调，
可以通过LayoutInflater.Factory来改造 XML 中存在的 tag, 具体的可以看setContentView.md
里面关于支持包的介绍


Factory2 比Factory多一个参数， View parent,也就是创建的view的父view
<pre>

    public interface Factory {
        public View onCreateView(String name, Context context, AttributeSet attrs);
    }
    
    public interface Factory2 extends Factory {
        /**
         * Version of {@link #onCreateView(String, Context, AttributeSet)}
         * that also supplies the parent that the view created view will be
         * placed in.
         *
         * @param parent The parent that the created view will be placed 
         * in; <em>note that this may be null</em>.
         * @param name Tag name to be inflated.
         * @param context The context the view is being created in.
         * @param attrs Inflation attributes as specified in XML file.
         *
         * @return View Newly created view. Return null for the default
         *         behavior.
         */
        public View onCreateView(View parent, String name, Context context, AttributeSet attrs);
    }
</pre>

实际项目中的用处：

* 全局替换字体等属性

* 动态换肤功能

* 无需编写shape、selector，直接在xml设置值 等

* 提高view构建的效率

   例如 可以直接new出自定义view，避免系统反射调用，提高view创建速度

* 替换默认View的实现

* 在Activity的onCreateView()回调中实现我们自己的逻辑



##### LayoutInflater Factory 注意点

* LayoutInflater Factoty有一个限制，每个activity只能被设置一次。如果被多次设置会抛出异常

[LayoutInflater 源码解析](https://www.jianshu.com/p/f0f3de2f63e3)

[LayoutInflater Factory 源码解析](https://www.jianshu.com/p/9c16bbaee442)

[setContentView 源码解析](https://www.jianshu.com/p/00a6d391569e)

[AsyncLayoutInflater 源码解析](https://www.jianshu.com/p/a3a3bd314c45)