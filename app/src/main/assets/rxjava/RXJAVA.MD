

### WIKI ###
[rxjava](https://github.com/ReactiveX/RxJava)
[rxandroid](https://github.com/ReactiveX/RxAndroid)
[document](http://reactivex.io/documentation/operators.html)

rxjava 基于观察者模式，分成4个大的部分， 观察者，被观察者，订阅， 事件，默认是在主线程执行

我们可以指定线程:

    .subscribeOn(Schedulers.newThread()) // 指定的是上游发送事件的线程
    .observeOn(Schedulers.io()) // 指定的是下游接收事件的线程

也可以在observable & flowable 间进行切换

    我们可以把Schedulers看作线程控制符，一共五种线程控制符，可以通过这些线程控制符切换不同的线程。
    以下是五种线程控制符的区别：

    Schedulers.immediate() 在当前线程运行，相当于不切换线程。这是默认的 Scheduler。

    Schedulers.newThread()总是启用新线程，并在新线程执行操作。

    Schedulers.io() I/O 操作（读写文件、数据库、网络信息交互等）所使用的 Scheduler。
    行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，
    可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，
    可以避免创建不必要的线程。

    Schedulers.computation() 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，
    即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，
    大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。

    AndroidSchedulers.mainThread() 切换到主线程，指定的操作将在Android 主线程运行。
    其实我们常用的就2种：Schedulers.io()和AndroidSchedulers.mainThread()

### 参考 ###

[文章1](https://www.jianshu.com/p/0cd258eecf60)

### 几个概念 ###
- 背压 (Backpressure)

[概念](https://www.zhihu.com/question/49618581)

简单来说就是上游数据生成的速度大于下游处理的速度，导致下游buffer溢出。

生产速度大于消费速度，所以需要 Buffer；

外部条件有限制，所以 Buffer 需要有上限；

Buffer 达到上限这个现象，有一个简化的等价词叫做 Backpressure；

Backpressure 的出现其实是一种危险边界，唯一的选择是丢弃新事件。

### 相关类 ###
CompositeDisposable

Disposable

被观察者 Flowable Observable



### 关键字 ###

- just

just创建发送指定值的Observerble，just只是简单的原样发射，将数组或Iterable当做单个数据。

如果传递的值为null，则发送的Observable的值为null。参数最多为9个

- doOnNext

让订阅者在接收到数据前干点事情的操作符


- create

create操作符，主要用于生产一个Observable, 我们称之为被观察者、发射器、上游事件等

另外还有一个Observer, 也就是观察者

- Observable

可接受可变参数

Observable不支持背压，使用Flowable代替，

- Flowable

支持背压，

subscribe()

subscribe(Consumer<? super T> onNext)

subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError)

subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete)

subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
            Action onComplete, Consumer<? super Disposable> onSubscribe)

subscribe(Subscriber<? super T> s)

- Observer

观察者

subscribe()

subscribe(Consumer<? super T>           onNext,
          Consumer<? super Throwable>   onError)

subscribe(Consumer<? super T>           onNext,
          Consumer<? super Throwable>   onError,
          Action                        onComplete)

subscribe(Consumer<? super T>           onNext,
          Consumer<? super Throwable>   onError,
          Action                        onComplete,
          Consumer<? super Disposable>  onSubscribe)

subscribe(Observer<? super T> observer)

- Consumer

Consumer是简易版的Observer,只提供一个回调接口accept(相当于onnext)，我们看下面的参数，
对于Observer的处理我们都是需要单独处理的

Observable在提交观察subscribe时是有几个参数的，如下面的

subscribe(Consumer<? super T> onNext)

subscribe(Consumer<? super T>           onNext,
          Consumer<? super Throwable>   onError)

subscribe(Consumer<? super T>           onNext,
          Consumer<? super Throwable>   onError,
          Action                        onComplete)

subscribe(Consumer<? super T>           onNext,
          Consumer<? super Throwable>   onError,
          Action                        onComplete,
          Consumer<? super Disposable>  onSubscribe)

- Map

类型转换，就是将上游发来的事件转换为任意想要的类型,eg:

将integer 转换成String 类型：

map(new Function<Integer, String>() {
    @Override
    public String apply(Integer integer) throws Exception {
        return integer.toString();
    }
})

- flatmap / concatMap

将一个Observable转换成另一个Observable, 我们看例子

- zip

合并事件，分别从连个Observable中依次取出事件进行处理, 以最少的个数为准

结果是A1 B2 C3

    @Override
    protected void doSomething() {
        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return s + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                mRxOperatorsText.append("zip : accept : " + s + "\n");
                Log.e(TAG, "zip : accept : " + s + "\n");
            }
        });

    }

    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext("A");
                    e.onNext("B");
                    e.onNext("C");
                }
            }
        });
    }

    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(1);
                    e.onNext(2);
                    e.onNext(3);
                    e.onNext(4);
                    e.onNext(5);
                }
            }
        });
    }

- filter 、 debounce

对事件进行过滤，返回的是满足条件的，

满足条件的事件才会往下传递

debounce 则是将发射过快的过滤掉

- Timer 、 Interval

延迟

间隔轮询

轮询的停止可以直接compose 绑定生命周期，也可以直接unsubscribe

-distinct

去重操作符，其实就是简单的去重

- buffer 、 window

buffer将数据按照规定的大小做一下缓存，然后将缓存的数据作为一个集合发射出去

window()函数和buffer()很像，但是它发射的是Observable而不是列表

- publishSubject、AsyncSubject

onNext() 会通知每个观察者

AsyncSubject

- Completable

只关心结果

- defer

简单的说就是每次订阅都会创建一个新的Observable

并且如果该Observable没有被订阅，就不会生成新的Observable

- last

取出最后一个值，参数是没有值的时候的默认值

- merge

将多个Observable合起来，接受可变参数，也支持使用迭代器集合

- reduce、scan

Reduce操作符应用一个函数接收Observable发射的数据和函数的计算结果作为下次计算的参数，输出最后的结果。

跟scan操作符很类似，只是scan会输出每次计算的结果，而reduce只会输出最后的结果

eg: 加法 1+ 2 + 3

    Observable.just(1, 2, 3)
            .reduce(new BiFunction<Integer, Integer, Integer>() {
                @Override
                public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                    return integer + integer2;
                }
            }).subscribe(new Consumer<Integer>() {
        @Override
        public void accept(@NonNull Integer integer) throws Exception {
            mRxOperatorsText.append("reduce : " + integer + "\n");
            Log.e(TAG, "accept: reduce : " + integer + "\n");
        }
    });

- skip

接受一个long型参数，代表跳过多少个数目的事件再开始接收

- take

用于指定订阅者最多收到多少数据



- Single/ Completable/ Maybe

single 只能发送一个事件，

Completable 侧重观察结果

Maybe

- 线程调度




