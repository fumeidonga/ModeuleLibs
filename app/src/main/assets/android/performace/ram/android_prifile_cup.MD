

  ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/android_profiler_cpu.png?raw=true)


1. Call Chart

Call Chart是Traceview和systrace默认使用的展示方式。它按照应用程序的函数执行顺序来展示，适合用于分析整个流程的调用


 ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/android_profiler_cpu1.png?raw=true)


2. Flame Chart

A函数调用B函数，B函数调用C函数，循环三次


 ![](https://github.com/fumeidonga/markdownPic/blob/master/performance/android_profiler_cpu2.png?raw=true)
 
当我们不想知道应用程序的整个调用流程，只想直观看出哪些代码路径花费的CPU时间较多时，火焰图就是一个非常好的选择
例如，之前我的一个反序列化实现非常耗时，通过火焰图发现耗时最多的是大量Java字符串的创建和拷贝，通过将核心实现转为Native，最终使性能提升了很多倍。

火焰图还可以使用在各种各样的维度，例如内存、I/O的分析。有些内存可能非常缓慢地泄漏，通过一个内存的火焰图，我们就知道哪些路径申请的内存最多，有了火焰图我们根本不需要分析源代码，也不需要分析整个流程。

最后我想说，每个工具都可以生成不同的展示方式，我们需要根据不同的使用场景选择合适的方式。

