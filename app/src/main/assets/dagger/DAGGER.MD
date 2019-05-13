
介绍：
http://www.jianshu.com/p/cd2c1c9f68d4

http://www.jianshu.com/p/1d42d2e6f4a5

http://www.jianshu.com/p/65737ac39c44


http://blog.csdn.net/android_study_ok/article/details/52384247

http://blog.csdn.net/android_study_ok/article/details/52398946

依赖注入，简单说就是目标类中所依赖的其他的类的初始化过程，往往一个类（目标类）里面，为了使用其他类（被依赖类）的一些方法，
需要先引入其他类的对象（称之为目标类依赖其他被依赖类），这时，你就需要new 其他类的对象，有时候后new的这个对象需要准备很多参数，
你在new之前就要一个一个地准备这些参数，这样就使得现在的这个目标类，越来越臃肿。
通俗地讲，你要吃一个苹果，你只需一个依赖注入，一个洗干净、削好皮、切好块、扎好牙签的苹果就呈现在你面前了，
至于这个苹果是怎么洗的、怎么削皮的、怎么切的、如何买的牙签，你统统不需要知道，这些工作都交给了另一个类，充分地解耦！！！

    @Retention(保留)注解说明,这种类型的注解会被保留到那个阶段. 有三个值:
    1.RetentionPolicy.SOURCE —— 这种类型的Annotations只在源代码级别保留,编译时就会被忽略
    2.RetentionPolicy.CLASS —— 这种类型的Annotations编译时被保留,在class文件中存在,但JVM将会忽略
    3.RetentionPolicy.RUNTIME —— 这种类型的Annotations将被JVM保留,所以他们能在运行时被JVM或其他使用反射机制的代码所读取和使用.

@Inject、@Module 、@Component 、@Provides 、@Scope 、@SubComponent 等
想要了解Dagger2，就必须要知道依赖注入的基础和这其中的每一个概念：

    @Inject: 通常在需要依赖的地方使用这个注解。换句话说，你用它告诉Dagger这个类或者字段需要依赖注入。
    这样，Dagger就会构造一个这个类的实例并满足他们的依赖。

    @Module: Modules类里面的方法专门提供依赖，所以我们定义一个类，用@Module注解，
    这样Dagger在构造类的实例的时候，就知道从哪里去找到需要的 依赖。
    modules的一个重要特征是它们设计为分区并组合在一起（比如说，在我们的app中可以有多个组成在一起的modules）。

    @Provide: 在modules中，我们定义的方法是用这个注解，以此来告诉Dagger我们想要构造对象并提供这些依赖。

    @Component: Components从根本上来说就是一个注入器，也可以说是@Inject和@Module的桥梁，它的主要作用就是连接这两个部分。
    Components可以提供所有定义了的类型的实例，比如：我们必须用@Component注解一个接口然后列出所有的@Modules组成该组件，
    如 果缺失了任何一块都会在编译的时候报错。所有的组件都可以通过它的modules知道依赖的范围。

    @Scope: Scopes可是非常的有用，Dagger2可以通过自定义注解限定注解作用域。后面会演示一个例子，
    这是一个非常强大的特点，因为就如前面说的一样，没 必要让每个对象都去了解如何管理他们的实例。
    在scope的例子中，我们用自定义的@PerActivity注解一个类，所以这个对象存活时间就和 activity的一样。
    简单来说就是我们可以定义所有范围的粒度(@PerFragment, @PerUser, 等等)。
    Dagger2可以通过自定义Scope注解，来限定通过Module和Inject方式创建的类的实例的生命周期能够与目标类的生命周期相同。
    或者可以这样理解：通过自定义Scope注解可以更好的管理创建的类实例的生命周期

    @Qualifier: 当类的类型不足以鉴别一个依赖的时候，我们就可以使用这个注解标示。用于限定依赖项,
    可用于区分相同类型但具有不同实例的对象
    例如：在Android中，我们会需要不同类型的context，因此，为了让Dagger2找出要提供哪些变量，我们必须明确指定它的标识符
    所以我们就可以定义 ActivityContext并且ApplicationContext因此Context被注入的对象可以参考相应的Context类型，
    这样当注入一个context的时候，我们就可以告诉 Dagger我们想要哪种类型的context。

    dependencies 继承 不能继承有作用域的compeonent eg:@Singleton

在继承的时候的例子：
<pre>
<code>
@PerFragment
@Component(dependencies = ApplicationComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {
  mFragmentComponent = DaggerFragmentComponent.builder()
                  .applicationComponent(TestApplication.getmInstance().component())
                  .fragmentModule(new FragmentModule(mActivity))
                  .build();
</code>
</pre>


##### 1. @Module
Modules类里面的方法专门提供依赖

    @Module
    public class FatherModule {
        /**
         * 返回值为被依赖的类类型
         * 方法名必须是provideXxxx, 以provide开始 后面随意
         * @return
         */
        @Provider
        @Singleton
        public Father providerFather() {
            return new Fahter();
        }
        @Provider
        @Singleton
        public C providerC() {
            return new C();
        }
    }

    @Module
    public class ApplicationModule {
        private Application application;
        public ApplicationModule(Application application){
            this.application = application;
        }

        /**
         * 返回值为被依赖的类类型
         * 方法名必须是provideXxxx, 以provide开始 后面随意
         * @return
         */
        @Provides
        @Singleton
        ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
            return jobExecutor;
        }
    }
但是 如何让它们建立直接的联系呢 ，这时 @Component (桥梁，注入器)出场了

注意：对于直接返回的类JobExecutor、RetrofitManager，它们类的构造函数一定要加上@Inject的注解：

    @Inject
    public JobExecutor() {
        // 初始化
        // ......
    }
##### 2. @Component  后面一定要带上modules参数，指明要在哪里找依赖
Component也是一个注解类，一个类要想是Component，必须用Component注解来标注该类，并且该类是接口或抽象类。
Component需要引用到目标类的实例，Component会查找目标类中用Inject注解标注的属性，查找到相应的属性后
会接着查找该属性对应的用Inject标注的构造函数（这时候就发生联系了）。

    @Scope
    @Retention(RUNTIME)
    public @interface PerActivity {}

    @Singleton
    @Component(modules=ApplicationModule.class)
    public interface ApplicationComponent(){
         /**
         * modules里面的方法
         * 1. 下面这1个方法 返回值必须是 modules里面指定的依赖库中的对象，
         * 2. 方法名建议一致，方便阅读
         * 3.  下面方法可以写，也可以不写，不写时代表不向其他的Component 暴露
         */
        Context getContext();
    }

    @PerActivity
    @Component(dependencies=ApplicationComponent.class, modules={FatherModule.class})
    public interface FatherComponent(){
        void inject(FatherModule fatherModule)
        FatherModule fatherModule();
    }

##### 3. @Inject
如下面类 Human 中用到一个 Father 对象，我们就说类 Human 对类 Father 有一个依赖

    public class Human {//目标类
        ...
        Father father;
        ...
        public Human() {
            father = new Father();
        }
    }

    public class Father { //目标类依赖的其他类

        public Father(){
            ...
        }
        ...
    }

    那么，如果用注解来写这段代码，
    public class Human {//目标类
        @Inject
        Father father;
        @Inject
        public Human() {
            ...
        }
    }

    public class Father {//目标类依赖的其他类
        @Inject
        public Father(){
            ...
        }
        ...
    }
这样我们就将目标类跟所依赖的类建立了某种联系，但是，如果我们有众多的依赖类，又该如何管理呢，我们用@Module
##### 4.  @Qualifier
我们暂且称之为限定符吧，用苹果来举例

    public class Apple {
        String color;
        public Apple() {
            Log.e("TAG", "我是一个普通的苹果");
        }
        public Apple(String color) {
            this.color = color;
            Log.e("TAG", "我是一个有颜色的苹果");
        }
    }
    /**
     * 首先，自定义一个限定符， 用来区分是哪个构造函数new出的对象
     */
    @Qualifier//限定符
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Type {
        String value() default "";//默认值为""
    }



##### 1. 开始dagger  test包
官方文档： https://github.com/google/dagger/blob/master/README.md

默认情况下，Android Studio不允许您生成的Dagger 2代码作为合法类，因为它们通常不会添加到源路径，
但添加android-apt插件将添加这些文件到IDE类路径，并使您能够有更多的可见性 。
从 Android Gradle plugin 2.2 开始，android-apt已弃用， android-apt不支持新的jack createMouse toolchain

   首先就是导入包

    // Add Dagger dependencies
    dependencies {
        // apt command comes from the android-apt plugin
        annotationProcessor 'com.google.dagger:dagger-compiler:2.8'
        compile 'com.google.dagger:dagger:2.8'
    }
    And Android gradle plugin below 2.2:
    If you're using a version of the Android gradle plugin below 2.2, see https://bitbucket.org/hvisser/android-apt.

##### 2.被依赖的类构造方法不带参数  (dagger/test)
    要制作一盘美味的水果沙拉Salad。
    Salad需要什么原料呢？
    需要梨（Pear）、香蕉（Banana）和沙拉酱 （SaladSauce）
    我们就说Salad依赖Pear、Banana、 SaladSauce

    2.1 首先定义好三个类、分别是Pear、Banana、SaladSauce
        定义Salad类，Salad类有一个方法叫做makeSalad（Pear，Banana， SaladSauce），这个方法需要传入三个参数，分别是Pear、Banana、 SaladSauce。
        所以此时必须在Salad类内部创建出Pear、Banana、 SaladSauce的对象，然后传参调用makeSalad（Pear，Banana， SaladSauce）方法来制作好沙拉
    2.2 新建一个Module类 SaladModule, 管理所有依赖的类,用来提供原料
    2.3 新建一个Component接口 @SalsdComponent  后面一定要带上modules参数，指明要在哪里找依赖, 说到底就是建一座桥梁
    2.4 然后通过这座桥梁来沟通MainActivityFragment中测试

##### 3. 被依赖的类构造方法带参数
    要制作加橘子的沙拉。但是这个橘子有个小刀的参数
    添加Orange 类
    修改对应的类, SaladModule  SalsdComponent

##### 4.   @Qualifier          被依赖的类有多个构造方法
    当被依赖的类有多个构造方法时，我们就需要区分 @Qualifier
    4.1 例如再加入苹果 Apple 类
    4.2 @Qualifier定义一个限定符  Type.java
    修改对应的类, SaladModule  SalsdComponent
    特别注意下面的方法
        //    多个构造方法
        //    由于我们的Apple构造函数里使用了String,所以这里要管理这个String(★否则报错)
        //    int等基本数据类型是不需要这样做的
        @Provides
        public String provideString(){
            return new String();
        }

##### 5.  @Singleton, 我们要使用单例
    比如说，Banana,  必须同时加上

    @Provides
    @Singleton
    public Banana provideBanana(){
        return new Banana();
    }

    @Singleton
    @Component(modules = SaladModule.class) //指明要在哪些modules里面寻找依赖
    public interface SaladComponent {}

##### 6.  @Scope 作用域，也可以用作单例
    比如，我们要让苹果、梨使用同一把刀，让香蕉使用另外一把刀
    6.1 自定义Scope来实现局部单例 KnifeScope.java
    6.2 新建Module, 指定目前这个Module的使用范围

        @Module
        public class KnifeModule {
            @Provides
            @KnifeScope
            public Knife provideKnife(){
                return new Knife();
            }
        }
    6.3 新建一个KnifeComponent ，并指明目前这个Component的使用范围

            @KnifeScope
            @Component
            public interface KnifeComponent {

            }
    6.4
    public Apple(){
        UtilsLog.d("苹果");
        TestApplication.getmInstance().getmKnifeComponent().inject(this);
    }

### 注意
<pre>
1. Provide 如果是单例模式 对应的Compnent 也要是单例模式
2. inject(Activity act) 不能放父类
3. 即使使用了单利模式，在不同的Activity 对象还是不一样的
4. 依赖component， component之间的Scoped 不能相同
5. 子类component 依赖父类的component ，子类component的Scoped 要小于父类的Scoped，Singleton的级别是Application
6. 多个Moudle 之间不能提供相同的对象实例
7. Moudle 中使用了自定义的Scoped 那么对应的Compnent 使用同样的Scoped
</pre>

































