
1. 检测路径中是否有中文
2. 修改Gradle版本到新版本  

在Android studio中打印详细报错信息的方法：

在命令行中进入项目的根目录，或者可以在Android studio的Terminal中直接操作也可以，然后敲入一个命令：

> gradlew compileDebug --stacktrace

就可以输出较详细的信息，然后根据命令行给出的提示，还可以在后面加上-info或者-debug的选项得到更详细的信息，于是这个命令可以这样写：

> gradlew compileDebug --stacktrace -info

或者：

> gradlew compileDebug --stacktrace -debug    

或者:

> gradlew compileDebugSources  --stacktrace -info

或者:

首先进入命令行,输入命令

> gradlew processDebugManifest --stacktrace


[AAPT2 error: check logs for details](https://blog.csdn.net/u011618035/article/details/80574645)