

## 1. memory allocation

这个比较简单

## 2. dump java heap

点击dump按钮， dump后可以进行保存，但是我们一般用mat来分析，

MAT无法直接打开android studio生成的hprof文件，需要经过转换成标准的hprof，

进行转换，如下

    platform-tools hprof-conv
    E:\sdk\platform-tools>hprof-conv E:\hrl\me.hprof E:\hrl\9041.hprof

mat的用法具体查看mat文档




