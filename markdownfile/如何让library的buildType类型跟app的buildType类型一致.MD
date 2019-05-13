
参考 https://www.jianshu.com/p/3751f95a6480
官方文档 https://developer.android.com/studio/projects/android-library

## 一. 问题
library module的gradle配置里面有这样两个默认配置:
<pre>
<code>
android {
    defaultPublishConfig "release"
    publishNonDefault false
}</code>
 </pre>

## 解决
    debugImplementation
    releaseImplementation
    debugApi
    releaseApi

## 其他
https://juejin.im/post/5a3a70d46fb9a044fe46855d
