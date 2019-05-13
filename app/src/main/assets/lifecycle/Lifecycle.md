

[官方文档](https://developer.android.com/topic/libraries/architecture/lifecycle)

<pre>
<code>
public class MyObserver implements LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void connectListener() {
        ...
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectListener() {
        ...
    }
}

myLifecycleOwner.getLifecycle().addObserver(new MyObserver());
</code>
</pre>


- Lifecycle

Lifecycle是一个持有组件生命周期状态（如Activity或Fragment）的信息的类，并允许其他对象观察此状态

- Event

- State

- LifecycleOwner

- LifecycleObserver











