
## 相关概念

强（strong）、软（soft）、弱（weak）、虚（phantom）引用

- Strong reference ： 就是我们new出来的对象，但是还是被持有的应用，垃圾回收时不会回收这个应用
- soft reference   ： 我们new出来的对象，但是已经不被具体对象持有，常见的就是缓存中的对象引用，垃圾回收在内存还够的时候不会回收该部分内存信息，只有在内存不够时才会回收这块引用的对象
- weak reference   ： 当对象不在有强引用时候，垃圾回收时立刻回收。
- Phantom reference : 不会在内存中出现，因为它一般是同ReferenceQueue一起出现，来跟踪对象是否还是保持强引用。

GC Roots：所有的对象引用refer chains的起点

JVM在进行GC的时候是通过使用可达性来判断对象是否存活，通过GC Roots（GC根节点）
的对象作为起始点，从这些节点开始进行向下搜索，搜索所走过的路径成为Reference
Chain（引用链），当一个对象到GC Roots没有任何引用链相连（用图论的话来说就是
从GC Roots到这个对象不可达）时，则证明此对象是不可用的