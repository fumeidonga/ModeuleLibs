

















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


#### LyoutInflater 原理

其实就是从根节点开始，递归解析xml的每个节点，每一步递归的过程是：
通过节点名称（全类名），使用ClassLoader创建对应类的实例，也就是View，
然后，将这个View添加到它的上层节点（父View）。并同时会解析对应xml
节点的属性作为View的属性。每个层级的节点都会被生成一个个的View，
并根据View的层级关系add到对应的直接父View（上层节点）中，最终返回一
个包含了所有解析好的子View的布局根View。


#### LyoutInflater Factory

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

[LayoutInflater Factory 源码解析](https://www.jianshu.com/p/9c16bbaee442)

[LayoutInflater 源码解析](https://www.jianshu.com/p/f0f3de2f63e3)

[setContentView 源码解析](https://www.jianshu.com/p/00a6d391569e)

[AsyncLayoutInflater 源码解析](https://www.jianshu.com/p/a3a3bd314c45)