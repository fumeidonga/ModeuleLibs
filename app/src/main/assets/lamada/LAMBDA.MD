

lambda没有声明的方法，也即没有方法的修饰符(public、private), 没有返回值(void、其他)，没有方法名。

其实Lambda表达式的本质只是一个"语法糖",由编译器推断并帮你转换包装为常规的代码,因此你可以使用更少的代码来实现同样的功能

### FunctionalInterface ###
@FunctionalInterface 是 Java 8 新加入的一种接口，用于指明该接口类型声明是根据 Java 语言规范定义的函数式接口。

Java 8 还声明了一些 Lambda 表达式可以使用的函数式接口，当你注释的接口不是有效的函数式接口时，可以使用 @FunctionalInterface 解决编译层面的错误。
<pre>
以下是一种自定义的函数式接口： 

@FunctionalInterface
public interface WorkerInterface {

   public void doSomeWork();

}
</pre>


### Lambda表达式结构
(argument)     ->           {body}

(方法参数) lambda运算符   {方法的实现}

<pre>

我们翻译一下 FunctionalInterface fi = (argument) ->  {body}
fi将会持有一个实现了FunctionalInterface接口的匿名类的实例，匿名类中一个方法的定义为{body; }

eg: () -> System.out.println("Hello World"); 等价于
FunctionalInterface fi = new FunctionalInterface() {
    @Override
    public void theOneMethod() {
        System.out.println("Hello world");
    }
};

eg: () -> 1;等价于
FunctionalInterface fi = new FunctionalInterface() {
    @Override
    public int theOneMethod() {
        return 1;
    }
};
所有的这种只有一个方法的接口都可以这么转换

</pre>

* 一个 Lambda 表达式可以有零个或多个参数
* 所有参数需包含在圆括号内，参数之间用逗号相隔
* 参数的类型既可以明确声明，也可以根据上下文来推断，例如：(int a)与(a)效果相同
	
<pre>

argument多个,

(argument, argument1, ...) -> {body}

(type1 arg1, type2 arg2...) -> { body }
</pre>


也可以为空，代表参数集为空

	() -> {body}

* 如果 Lambda 表达式的*主体只有一条语句*，花括号{}可省略

    // 不需要参数,返回值为 1

	() -> 1
* 当*只有一个参数*，且其类型可推导时，圆括号（）可省略
  
    接收一个参数(数字类型),返回其 a * a 的值

    例如：a -> return a*a

我们列出常见的：
<pre>
() -> 42

() -> System.out.println("Hello World");

() -> { return 3.1415 };

(String s) -> { System.out.println(s); }

(int a, int b) -> {  return a + b; }

</pre>

### 函数式接口
函数式接口是只包含一个抽象方法声明的接口. 

根据定义，函数式接口只能有一个抽象方法，如果你尝试添加第二个抽象方法，将抛出编译时错误

举个例子，
<pre>
java.lang.Runnable
在 Runnable 接口中只声明了一个方法
public interface Runnable {
	public abstract void run();
}
</pre>

* 每个 Lambda 表达式都能隐式地赋值给函数式接口，当不指明函数式接口时，编译器会自动解释这种转化

<pre>
Runnable r = () -> System.out.println("hello world");

等同于

Runnable r = new Runnable(){
    public void run(){
      System.out.println("hello world");
    }
}

编译器会自动推断：根据线程类的构造函数签名 
public Thread(Runnable r) { }，将该 Lambda 表达式赋给 Runnable 接口
new Thread(
   () -> System.out.println("hello world")
).start();
</pre>

######接收lambdas

因为lambda是一个接口的实现，去使一个方法接收lambda并没有什么特别的要做：任何函数只要是函数式接口都能够接收一个lambda

eg：
<pre>
new Thread(Runnable r).start();

这里面的runnable参数我们就可以用下面的方式来替代
Runnable r = () -> System.out.println("hello world");

等同于

Runnable r = new Runnable(){
    public void run(){
      System.out.println("hello world");
    }
}

new Thread(() -> System.out.println("hello world")).start();

</pre>


### 其他函数 

map

filter





### 方法引用 ###




### 默认方法 ###



### Stream ###










### java 多线程编程 ###