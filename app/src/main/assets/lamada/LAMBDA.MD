

lambda没有声明的方法，也即没有方法的修饰符(public、private), 没有返回值(void、其他)，没有方法名。

其实Lambda表达式的本质只是一个"语法糖",由编译器推断并帮你转换包装为常规的代码,因此你可以使用更少的代码来实现同样的功能

### FunctionalInterface ###
@FunctionalInterface 是 Java 8 新加入的一种接口，用于指明该接口类型声明是根据 Java 语言规范定义的函数式接口。

使用Lambda表达式简化匿名内部类的书写，但Lambda表达式并不能取代所有的匿名内部类，只能用来取代**函数接口（Functional Interface）**的简写

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
() -> 42 不需要参数,返回值为 42

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



### 变量作用域
lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误


### 其他函数 
|操作类型|接口方法|
|--------|--------|
|中间操作|concat() distinct() filter() flatMap() limit() map() peek() skip() sorted() parallel() sequential() unordered()|
|结束操作|allMatch() anyMatch() collect() count() findAny() findFirst() forEach() forEachOrdered() max() min() noneMatch() reduce() toArray()|

我们来列出一些比较常用的

###### forEach()
<pre>Java
// 使用Stream.forEach()迭代
Stream<String> stream = Stream.of("I", "love", "you", "too");
stream.forEach(str -> System.out.println(str));
</pre>

###### filter()
函数原型为`Stream<T> filter(Predicate<? super T> predicate)`，作用是返回一个只包含满足`predicate`条件元素的`Stream`。

<pre>ava
// 保留长度等于3的字符串
Stream<String> stream= Stream.of("I", "love", "you", "too");
stream.filter(str -> str.length()==3)
    .forEach(str -> System.out.println(str));
</pre>

上述代码将输出为长度等于3的字符串`you`和`too`。注意，由于`filter()`是个中间操作，如果只调用`filter()`不会有实际计算，因此也不会输出任何信息。

###### distinct()

函数原型为`Stream<T> distinct()`，作用是返回一个去除之后的`Stream`。

<pre>Java
Stream<String> stream= Stream.of("I", "love", "you", "too", "too");
stream.distinct()
    .forEach(str -> System.out.println(str));
</pre>

上述代码会输出去掉一个`too`之后的其余字符串。

###### sorted()

排序函数有两个，一个是用自然顺序排序，一个是使用自定义比较器排序，

函数原型分别为`Stream<T>　sorted()`和

`Stream<T>　sorted(Comparator<? super T> comparator)`。

<pre>Java
Stream<String> stream= Stream.of("I", "love", "you", "too");

//1
stream.sorted((str1, str2) -> str1.length()-str2.length())
    .forEach(str -> System.out.println(str));

//2
stream.sorted(new Comparator<String>() {

    @Override
    public int compare(String str1, String str2) {

        return str1.length() - str2.length();
    }
})
    .forEach(str -> System.out.println(str));
</pre>

上述代码将输出按照长度升序排序后的字符串

###### map()

函数原型为`<R> Stream<R> map(Function<? super T,? extends R> mapper)`，
作用是返回一个对当前所有元素执行执行`mapper`之后的结果组成的`Stream`。

直观的说，就是对每个元素按照某种操作进行`转换`，转换前后`Stream`中元素
的个数不会改变，但元素的类型取决于转换之后的类型。

<pre>Java
Stream<String> stream　= Stream.of("I", "love", "you", "too");
stream.map(str -> str.toUpperCase())
    .forEach(str -> System.out.println(str));
</pre>
上述代码将输出原字符串的大写形式。

###### flatMap()
跟map类似，但也有不同，都是进行转换，flatmap可以对多个stream进行转换
<pre>
Stream<List<Integer>> stream = Stream.of(Arrays.asList(1,2), Arrays.asList(3, 4, 5));
stream.flatMap(list -> list.stream())
    .forEach(i -> System.out.println(i));

    最终将输出1~5这5个数字
</pre>

###### reduce()
*reduce*操作可以实现从一组元素中生成一个值，`sum()`、`max()`、`min()`、`count()`等都是*reduce*操作

<pre>

// 找出最长的单词
Stream<String> stream = Stream.of("I", "love", "you", "too");
Optional<String> longest = stream.reduce((s1, s2) -> s1.length()>=s2.length() ? s1 : s2);
//Optional<String> longest = stream.max((s1, s2) -> s1.length()-s2.length());
System.out.println(longest.get());
</pre>

###### >>> 终极武器collect() <<<
不夸张的讲，如果你发现某个功能在*Stream*接口中没找到，十有八九可以
通过`collect()`方法实现。`collect()`是*Stream*接口方法中最灵活的一个，
学会它才算真正入门Java函数式编程



### 方法引用 ###




### 默认方法 ###



### Stream ###










### java 多线程编程 ###