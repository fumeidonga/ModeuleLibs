package com.rebeau.technology.android.rxjava;

import android.support.annotation.NonNull;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.technology.R;
import com.rebeau.technology.android.rxjava.base.RxTestActivity;
import com.rebeau.technology.android.rxjava.base.Man;
import com.rebeau.technology.android.rxjava.base.Woman;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.OnClick;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class RxjavaActivity extends RxTestActivity {
    @Override
    protected void onPause() {
        super.onPause();
        if(disposable != null) {
            disposable.dispose();
        }
    }

    /**
     * Observable 三部曲
     * just创建发送指定值的Observerble，just只是简单的原样发射，将数组或Iterable当做单个数据。
     * 如果传递的值为null，则发送的Observable的值为null。参数最多为9个
     */
    @OnClick(R.id.bottom0)
    public void begin(){
        // Observable 三部曲
        //初始Observable有很多方式，我们比较常用的是create

        // 第一步：初始化一个Observable
        Observable<Integer> observable0 = Observable.just(1,2);
        Observable<Integer> observable = Observable.fromArray(1, 2, 3);
        Observable<Integer> observable1 = new Observable<Integer>() {
            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                RBLogUtil.dt();
                observer.onNext(1);
                observer.onNext(2);
                observer.onComplete();
            }
        };

        Observable<Integer> observable2 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onComplete();
            }
        });


        // 第二步：初始化一个Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

                RBLogUtil.dt();
            }

            @Override
            public void onNext(Integer o) {

                RBLogUtil.dt(o);
            }

            @Override
            public void onError(Throwable e) {

                RBLogUtil.dt();
            }

            @Override
            public void onComplete() {

                RBLogUtil.dt();
            }
        };

        Observer<Integer> observer1 = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

                RBLogUtil.dt();
            }

            @Override
            public void onNext(Integer o) {

                RBLogUtil.dt(o);
            }

            @Override
            public void onError(Throwable e) {

                RBLogUtil.dt();
            }

            @Override
            public void onComplete() {

                RBLogUtil.dt();
            }
        };

        Observer<Integer> observer2 = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {

                RBLogUtil.dt();
            }

            @Override
            public void onNext(Integer o) {

                RBLogUtil.dt(o);
            }

            @Override
            public void onError(Throwable e) {

                RBLogUtil.dt();
            }

            @Override
            public void onComplete() {

                RBLogUtil.dt();
            }
        };
        // 第三部：建立订阅关系
//        observable.subscribe(observer);
//        observable1.subscribe(observer1);
        observable2.subscribe(observer2);


    }


    /**
     * 上游可以发送无限个onNext, 下游也可以接收无限个onNext.
     * 当上游发送了一个onComplete后, 上游onComplete之后的 事件将会继续发送, 而下游收到onComplete事件之后将不再继续接收事件.
     * 当上游发送了一个onError后, 上游onError之后的 事件将继续发送, 而下游收到onError事件之后将 不再继续接收事件.
     * 上游可以不发送onComplete或onError.
     * 最为关键的是onComplete和onError必须唯一并且互斥,
     * 即不能发多个onComplete, 也不能发多个onError, 也不能先发一个onComplete, 然后再发一个onError, 反之亦然
     */
    @OnClick(R.id.bottom1)
    public void create1(){
        //1. 初始化一个Observable
        Observable.create(new ObservableOnSubscribe<Integer>(){

            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onComplete();//下游收到onComplete事件之后将不再继续接收事件
                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
                e.onComplete();
                RBLogUtil.dt();
            }
        }).subscribe(new Observer<Integer>() {//2,3,并订阅
            @Override
            public void onSubscribe(Disposable d) {
                RBLogUtil.dt();
            }

            @Override
            public void onNext(Integer integer) {

                RBLogUtil.dt(integer);
            }

            @Override
            public void onError(Throwable e) {
                RBLogUtil.dt();
            }

            @Override
            public void onComplete() {
                RBLogUtil.dt();
            }
        });
    }

    /**
     * onComplete和onError必须唯一并且互斥,
     */
    @OnClick(R.id.bottom2)
    public void create2(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onError(new Throwable("123"));//下游收到onComplete事件之后将不再继续接收事件
                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
                //e.onComplete(); //只能发同一种类型
                RBLogUtil.dt();
            }
        }).subscribe();

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                //e.onError(new Throwable("123"));//下游收到onComplete事件之后将不再继续接收事件
                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
                //e.onComplete(); //只能发同一种类型
                RBLogUtil.dt();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onError(new Throwable("123"));//下游收到onComplete事件之后将不再继续接收事件
                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
                //e.onComplete(); //只能发同一种类型
                RBLogUtil.dt();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

                RBLogUtil.dt();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

                RBLogUtil.dt();
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onError(new Throwable("123"));//下游收到onComplete事件之后将不再继续接收事件
                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
                //e.onComplete(); //只能发同一种类型
                RBLogUtil.dt();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {

            }
        });


        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onError(new Throwable("123"));//下游收到onComplete事件之后将不再继续接收事件
                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
                //e.onComplete(); //只能发同一种类型
                RBLogUtil.dt();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {

            }
        }, new Action() {
            @Override
            public void run() throws Exception {

            }
        }, new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {

            }
        });

    }

    /**
     * Consumer是简易版的Observer，他有多重重载，可以自定义你需要处理的信息，
     * 他只提供一个回调接口accept，由于没有onError和onCompete，无法再
     * 接受到onError或者onCompete之后，实现函数回调。
     * 无法回调，并不代表不接收，他还是会接收到onCompete和onError之后做出默认操作，
     * 也就是监听者（Consumer）不在接收
     */
    @OnClick(R.id.bottom3)
    public void create3(){

//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//
//                RBLogUtil.dt();
//                e.onNext(1);
//                e.onNext(2);
//                e.onComplete();//下游收到onComplete事件之后将不再继续接收事件
//                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
//                //e.onComplete(); //只能发同一种类型
//                RBLogUtil.dt();
//            }
//        }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//
//                RBLogUtil.dt();
//            }
//        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onError(new Throwable("123"));//下游收到onComplete事件之后将不再继续接收事件
                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
                //e.onComplete(); //只能发同一种类型
                RBLogUtil.dt();
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                RBLogUtil.dt(integer);

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                RBLogUtil.dt(throwable);

            }
        });

//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//
//                RBLogUtil.dt();
//                e.onNext(1);
//                e.onNext(2);
//                e.onComplete();//下游收到onComplete事件之后将不再继续接收事件
//                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
//                //e.onComplete(); //只能发同一种类型
//                RBLogUtil.dt();
//            }
//        }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                RBLogUtil.dt();
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                RBLogUtil.dt();
//
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//                RBLogUtil.dt();
//
//            }
//        });
//
//
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
//
//                RBLogUtil.dt();
//                e.onNext(1);
//                e.onNext(2);
//                e.onComplete();//下游收到onComplete事件之后将不再继续接收事件
//                e.onNext(3);// 事件将会继续发送, 但是订阅器里面并不会接受
//                //e.onComplete(); //只能发同一种类型
//                RBLogUtil.dt();
//            }
//        }).subscribe(new Consumer<Integer>() {
//            @Override
//            public void accept(Integer integer) throws Exception {
//                RBLogUtil.dt();
//
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(Throwable throwable) throws Exception {
//                RBLogUtil.dt();
//
//            }
//        }, new Action() {
//            @Override
//            public void run() throws Exception {
//                RBLogUtil.dt();
//
//            }
//        }, new Consumer<Disposable>() {
//            @Override
//            public void accept(Disposable disposable) throws Exception {
//                RBLogUtil.dt();
//
//            }
//        });

    }

    /**
     * map
     * Observable.create return Observable
     */
    @OnClick(R.id.bottom4)
    public void create4() {
        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                RBLogUtil.dt();
            }
        });

        observable.map(new Function<Integer, String>() {
            @Override
            public String apply(Integer integer) throws Exception {
                RBLogUtil.dt();
                return integer.toString();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

                RBLogUtil.dt(s);
            }
        });
    }

    /**
     * flatmap
     * 类型转换
     * 比如说服务端返回的数据我们再重新组装一下
     * woman -> man
     */
    @OnClick(R.id.bottom5)
    public void create5(){

        Disposable disposable = Observable.create(new ObservableOnSubscribe<Woman>() {
            @Override
            public void subscribe(ObservableEmitter<Woman> e) throws Exception {
                e.onNext(new Woman("woman11", 30, "woman"));
                e.onNext(new Woman("woman111", 31, "woman"));
            }
        }).flatMap(new Function<Woman, ObservableSource<Man>>() {
            @Override
            public ObservableSource<Man> apply(Woman t) throws Exception {
                final List<Man> list = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    Man man = new Man(t.getName(), t.getAge(), "man");
                    list.add(man);
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<Man>() {
            @Override
            public void accept(Man man) throws Exception {

                RBLogUtil.dt(man);
            }
        });

        Observable.create(new ObservableOnSubscribe<Woman>() {
            @Override
            public void subscribe(ObservableEmitter<Woman> e) throws Exception {

                e.onNext(new Woman("woman", 30, "woman"));
                e.onNext(new Woman("woman1", 31, "woman"));
            }
        }).flatMap(new Function<Woman, ObservableSource<Man>>() {
            @Override
            public ObservableSource<Man> apply(final Woman woman) throws Exception {
                return Observable.create(new ObservableOnSubscribe<Man>() {
                    @Override
                    public void subscribe(ObservableEmitter<Man> e) throws Exception {
                        e.onNext(new Man(woman.getName(), woman.getAge(), "man"));
                    }
                });
            }
        }).subscribe(new Observer<Man>() {
            @Override
            public void onSubscribe(Disposable d) {

                RBLogUtil.dt();
            }

            @Override
            public void onNext(Man man) {

                RBLogUtil.dt(man);
            }

            @Override
            public void onError(Throwable e) {

                RBLogUtil.dt();
            }

            @Override
            public void onComplete() {

                RBLogUtil.dt();
            }
        });
    }

    /**
     * zip 合并事件
     */
    @OnClick(R.id.bottom6)
    public void create6(){

        Observable integer = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {

                RBLogUtil.dt();
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                RBLogUtil.dt();
            }
        });

        Observable string = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {

                RBLogUtil.dt();
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
                RBLogUtil.dt();
            }
        });

        Observable.zip(string, integer, new BiFunction<String, Integer, String>() {
            @Override
            public String apply(String s, Integer i) throws Exception {
                return s + " " + i;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                RBLogUtil.dt("zip : accept : " + s);
            }
        });
    }

    /**
     *
     */
    @OnClick(R.id.bottom7)
    public void doOnNext() {
        Disposable disposable = Observable.just(1,2,3)
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        integer += 3;
                        RBLogUtil.dt(integer);
                    }
                })
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(final Integer integer) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<String>() {
                            @Override
                            public void subscribe(ObservableEmitter<String> e) throws Exception {
                                e.onNext("" + integer.intValue() + 3);
                            }
                        });
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String integer) throws Exception {
                        RBLogUtil.dt(integer);
                    }
                });
    }


    /**
     * 过滤,满足条件的事件才会往下传递
     */
    @OnClick(R.id.bottom8)
    public void filter() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                for (int i = 0;i < 4; i++) {
                    e.onNext(""+i);
                }
            }
        }).filter(new Predicate<String>() {
            @Override
            public boolean test(String integer) throws Exception {
                return integer.equals("3");
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String integer) throws Exception {
                RBLogUtil.dt(integer);
            }
        });
    }

    /**
     * delay
     */
    @OnClick(R.id.bottom9)
    public void timer(){

        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.newThread()) // 指定的是上游发送事件的线程
                .observeOn(Schedulers.io()) // 指定的是下游接收事件的线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        RBLogUtil.dt("timer :" + aLong);
                    }
                });
    }
    Disposable disposable ;
    /**
     * 每间隔 N s 执行一次
     */
    @OnClick(R.id.bottom10)
    public void interval(){

        /*disposable = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        RBLogUtil.dt("timer :" + aLong);
                    }
                });*/

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                RBLogUtil.dt();
                e.onNext(1);
                e.onComplete();
            }
        })
        .doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                RBLogUtil.dt();

            }
        })
        .delay(3, TimeUnit.SECONDS, true)       // 设置delayError为true，表示出现错误的时候也需要延迟5s进行通知，达到无论是请求正常还是请求失败，都是5s后重新订阅，即重新请求。
        .subscribeOn(Schedulers.io())
        .repeat()   // repeat保证请求成功后能够重新订阅。
        .retry()    // retry保证请求失败后能重新订阅
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
                RBLogUtil.dt();

            }

            @Override
            public void onNext(Object o) {

                RBLogUtil.dt();
            }

            @Override
            public void onError(Throwable e) {

                RBLogUtil.dt();
            }

            @Override
            public void onComplete() {

                RBLogUtil.dt();
            }
        });
    }

    /**
     * 将 observable 中的数据按 skip（步长）分成最长不超过 count 的 buffer，然后生成一个 observable
     */
    @OnClick(R.id.bottom11)
    public void buffer() {

        /**
         * 将12345分组，每组的个数不超过count 2， 每隔skip 3 来取值
         * 结果
         * [1, 2]，[4, 5]
         */
        Observable.just(1, 2, 3, 4, 5)
                .buffer(2, 3)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> integers) throws Exception {
                        RBLogUtil.dt(integers);
                    }
                });
    }

    @OnClick(R.id.bottom12)
    public void flowable() {
        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> e) throws Exception {
                e.onNext("a");
                e.onNext("b");
            }
        }, BackpressureStrategy.BUFFER);

        flowable.subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                RBLogUtil.dt();
            }
        });

//        flowable.subscribe(new Subscriber<String>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                s.request(Long.MAX_VALUE);
//                RBLogUtil.dt();
//            }
//
//            @Override
//            public void onNext(String s) {
//
//                RBLogUtil.dt();
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//                RBLogUtil.dt();
//            }
//
//            @Override
//            public void onComplete() {
//
//                RBLogUtil.dt();
//            }
//        });

//        flowable.subscribeOn(Schedulers.newThread())
//        .observeOn(Schedulers.io())
//        .subscribe(new FlowableSubscriber<String>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                //订阅时候的操作
//                 s.request(Long.MAX_VALUE);//请求多少事件，这里表示不限制
//                RBLogUtil.dt(s);
//            }
//
//            @Override
//            public void onNext(String s) {
//
//                RBLogUtil.dt(s);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//
//                RBLogUtil.dt(t);
//            }
//
//            @Override
//            public void onComplete() {
//
//                RBLogUtil.dt();
//            }
//        });

    }


    /**
     *
     */
    @OnClick(R.id.bottom13)
    public void compose(){

        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                RBLogUtil.dt();
                e.onNext("a");
            }
        });

        Observable observableCompose = observable.compose(applySchedulers());

        Observable observableCompose1 = observable.compose(new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        });

        Disposable disposable1 = observableCompose1.subscribe(new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                RBLogUtil.dt();
            }
        });
    }

    /**
     * 供给他一个Observable它会返回给你另一个Observable
     * @param <T>
     * @return
     */
    private <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
//                return upstream.compose(apiTransformer.<T>transformer()).
//                    subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread());
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * Observable 跟flowable的转换
     */
    @OnClick(R.id.bottom14)
    public void toflowable(){

        Observable observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                RBLogUtil.dt();
                e.onNext("a");
            }
        });

        observable = observable.compose(new ObservableTransformer() {
            @Override
            public ObservableSource apply(Observable upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        });

        Flowable flowable = observable.toFlowable(BackpressureStrategy.BUFFER);

        flowable.subscribe(new Subscriber() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(2);
                RBLogUtil.dt();
            }

            @Override
            public void onNext(Object o) {
                RBLogUtil.dt();

            }

            @Override
            public void onError(Throwable t) {
                RBLogUtil.dt();

            }

            @Override
            public void onComplete() {
                RBLogUtil.dt();

            }
        });
    }
}
