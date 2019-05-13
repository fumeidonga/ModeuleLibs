
##### 文档 #####
[GITHUB](https://github.com/google/dagger)

[官方文档](https://google.github.io/dagger/)

[API](http://google.github.io/dagger/api/latest/)

android gradle 引入

    // Add Dagger dependencies
    dependencies {
        // Dagger dependencies
            annotationProcessor "com.google.dagger:dagger-compiler:$rootProject.daggerVersion"
            compile "com.google.dagger:dagger:$rootProject.daggerVersion"
            compile "com.google.dagger:dagger-android:$rootProject.daggerVersion"
            compile "com.google.dagger:dagger-android-support:$rootProject.daggerVersion"
            annotationProcessor "com.google.dagger:dagger-android-processor:$rootProject.daggerVersion"

    }
#### 理解 ####
如果在 Class A 中，有 Class B 的实例，则称 Class A 对 Class B 有一个依赖
平常我们需要一个类，都是通过new 一个对象来实现，那么现在我们将这个new的过程交易一个容器去管理，
我们只需要从里面得到这个new出来的对象就好。

注释处理器 是一种在构建期间读取已编译文件以生成要在项目中使用的源代码文件的方法


依赖注入的实现方式：
1. 构造函数注入：在构造对象时注入所依赖的对象，public Man(Car car)
2. 字段注入：注入成员变量（不得为私有）。
3. 方法注入：注入方法参数，基于 set 方法，使用 setter 方法，public void setCar(Car car)
4. 接口注入：同方法注入，实现注入的方法
5. 注解注入：Dagger 2，本次要学习的，createMouse 后可以在build/generated/source/apt
目录下看到 Dagger 2 编译时生成的成员属性注入类

依赖注入顺序:
构造函数 -
领域 -
方法

### 关键字 ###

1. @Retention(保留)注解说明,这种类型的注解会被保留到那个阶段. 有三个值:
- RetentionPolicy.SOURCE —— 这种类型的Annotations只在源代码级别保留,编译时就会被忽略
- RetentionPolicy.CLASS —— 这种类型的Annotations编译时被保留,在class文件中存在,但JVM将会忽略
- RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.

2. @Inject、@Module 、@Component 、@Provides 、@Scope 、@SubComponent 等
想要了解Dagger2，就必须要知道依赖注入的基础和这其中的每一个概念：

- @Inject: 通常在需要依赖的地方使用这个注解。换句话说，你用它告诉Dagger这个类或者字段需要依赖注入。
这样，Dagger就会构造一个这个类的实例并满足他们的依赖。

@Inject标注构造函数时，Dagger 2 会完成实例的创建


- @Module: Modules类里面的方法专门提供依赖，所以我们定义一个类，用@Module注解，
这样Dagger在构造类的实例的时候，就知道从哪里去找到需要的 依赖。
modules的一个重要特征是它们设计为分区并组合在一起（比如说，在我们的app中可以有多个组成在一起的modules）。

    /**
     *返回值为被依赖的类类型
     *方法名必须是provideXxxx, 以provide开始 后面随意
     *@return
     */

- @Provide: 在modules中，我们定义的方法是用这个注解，以此来告诉Dagger我们想要构造对象并提供这些依赖。

- @Component: Components从根本上来说就是一个注入器，也可以说是@Inject和@Module的桥梁，它的主要作用就是连接这两个部分。
Components可以提供所有定义了的类型的实例，比如：我们必须用@Component注解一个接口然后列出所有的@Modules组成该组件，
如 果缺失了任何一块都会在编译的时候报错。所有的组件都可以通过它的modules知道依赖的范围。

- @Scope: Scopes可是非常的有用，Dagger2可以通过自定义注解限定注解作用域。后面会演示一个例子，
这是一个非常强大的特点，因为就如前面说的一样，没 必要让每个对象都去了解如何管理他们的实例。
在scope的例子中，我们用自定义的@PerActivity注解一个类，所以这个对象存活时间就和 activity的一样。
简单来说就是我们可以定义所有范围的粒度(@PerFragment, @PerUser, 等等)。
Dagger2可以通过自定义Scope注解，来限定通过Module和Inject方式创建的类的实例的生命周期能够与目标类的生命周期相同。
或者可以这样理解：通过自定义Scope注解可以更好的管理创建的类实例的生命周期

如果没有使用 Scope 注解，Component 每次调用 Module 中的 provide 方法或 Inject 构造函数生成的工厂时都会创建一个新的实例，
而使用 Scope 后可以复用之前的依赖实例

Scope 注解要生效的话，需要同时标注在 Component 和提供依赖实例的 Module 或目标类上。Module 中 provide 方法中的 Scope
注解必须和 与之绑定的 Component 的 Scope 注解一样



- @Qualifier: 当类的类型不足以鉴别一个依赖的时候，我们就可以使用这个注解标示。用于限定依赖项,
可用于区分相同类型但具有不同实例的对象
例如：在Android中，我们会需要不同类型的context，因此，为了让Dagger2找出要提供哪些变量，我们必须明确指定它的标识符
所以我们就可以定义 ActivityContext并且ApplicationContext因此Context被注入的对象可以参考相应的Context类型，
这样当注入一个context的时候，我们就可以告诉 Dagger我们想要哪种类型的context。

- dependencies 继承 不能继承有作用域的compeonent eg:@Singleton

### demo ###
- oneactivity  直接注入
- twoactivity  使用provider
- threeactivity  使用binds
- fouractivity   inject构造方法

#### @Module负责提供可以注入的对象 ####
这些类定义了注释的方法@Provides。这些方法返回的对象可用于依赖注入
或者 @Binds 或者直接在构造方法进行注入

    @Module                                                                 @Module
    public class ModuleA {                                                  public class ModuleB {
        /**                                                                     /**
         * 返回值为被依赖的类类型                                                * 返回值为被依赖的类类型
         * 方法名必须是provideXxxx,                                              * 方法名必须是provideXxxx,
         * 以provide开始 后面随意                                                * 以provide开始 后面随意
         * @return                                                               * @return
         */                                                                      */
        @Provider                                                                @Provider
        @Singleton                                                               @Singleton
        public A provideA() {                                                    public A provideA() {
            return new A();                                                          return new A();
        }                                                                        }

        @Provider                                                                @Provider
        @Singleton                                                               @Singleton
        public B provideB() {                                                    public B provideB() {
            return new B();                                                          return new B();
        }                                                                        }

        @Provider                                                                @Provider
        @Singleton                                                               @Singleton
        public C provideC(A a) {                                                 public C provideC(A a) {
            return new C(a);                                                         return new C(a);
        }                                                                        }
    }                                                                        }

##### 连接消费者和生产者：@Component #####
Component便是注入器，对象由Module提供或是通过构造方法注入来获得

    @Singleton
    @KnifeScope
    @Component(modules = {
        AndroidSupportInjectionModule.class, //必须 ，固定写法
        ActivityBindingModule.class,  //必须 ，固定写法
        ModuleA.class, ModuleA.class}) //指明要在哪些modules里面寻找依赖
    public interface ComponentA {
        /**
         * modules里面的方法
         * 1. 下面这三个方法 返回值必须是 modules里面指定的依赖库中的对象，
         * 2. 方法名建议一直，方便阅读
         * 3.  下面三方方法可以写，也可以不写，不写时代表不向其他的Component 暴露
         */
        A provideA();
        B provideB();
        C provideC();

        /**
         * 多个构造函数
         */
        @Type("normal")  //名字不重要， 重要的是靠这里面的值来区分
        Apple providerNormalApple();

        @Type("color")   //名字不重要， 重要的是靠这里面的值来区分
        Apple providerTestApple();

        /**
         * 这个方法表示要将 modules 中的依赖类注入到 MainActivityFragment
         */
        void inject(MainActivityFragment fragment);
    }

#### 依赖消费：@Inject ####
    DaggerFragment
    DaggerApplication
    public class ActivityA extends DaggerActivity {

        /**
         * 表示注入这个依赖
         */
        @Inject
        A a;
    }

当然我们不用集成daggerActivity, 但是我们需要自己inject，请看
        AndroidInjection.inject(this);



























