# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontoptimize
-dontshrink
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#忽略警告
-ignorewarnings

#---------------------------------------------------------------------------------------
-dontwarn android.support.**
-dontwarn android.webkit.WebView

#---------------------------------------------------------------------------------------
# 避免混淆泛型
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepattributes Exceptions,InnerClasses
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#---------------------------------------------------------------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }

-keepclassmembers class **.R$* {
     public static <fields>;
}
#---------------------------------------------------------------------------------------
-keep class **.R {*;}
-keep class **.R$* {*;}
-keep class **.R$*
-keep class **.R
-keep public class **.R$*{
    public static final int *;
}
# 保留support下的所有类及其内部类
-keep class android.support.** { *; }
-keep public class javax.**
-keep public class android.webkit.**


# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
#---------------------------------------------------------------------------------------
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}


#-----------------------------------fastjson---
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }



