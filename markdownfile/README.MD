
## 常量配置
BuildConfig就是根据build.gradle配置自动生成的
<pre>
public final class BuildConfig {
  public static final boolean DEBUG = Boolean.parseBoolean("true");
  public static final String BUILD_TYPE = "debug";
  public static final String FLAVOR = "";
  public static final int VERSION_CODE = 1;
  public static final String VERSION_NAME = "1.0";
}
</pre>

## android studio如何将多个module统一放在一个目录下
1.root工程下新建extra文件夹
2.将一个module拖进extra文件夹
3.在setting.gradle中将原有的:moduleName改为:extra:moduleName，同步一下, clean ,rebuild


## 参考
https://github.com/guiying712/AndroidModulePattern

https://github.com/TommyLemon/Android-ZBLibrary

https://github.com/yangguangfu520/BeijingNews_1020


## gradle
https://flyouting.gitbooks.io/gradle-plugin-user-guide-cn/content/configuring_the_structure.html
https://www.jianshu.com/p/cc98a6b4f52e
https://www.w3cschool.cn/gradle/ms7n1hu2.html
https://docs.gradle.org/3.3/userguide/build_lifecycle.html
https://www.jianshu.com/p/8250a5d2e109
https://www.jianshu.com/p/342e0a06116b
https://www.jianshu.com/search?q=gradle&page=1&type=note
https://developer.android.com/studio/build/index.html#build-process
https://developer.android.com/studio/build/build-variants.html


## studio 3.0 Gradle 关键字变化
<pre>
compile被废弃了，而是改成了这两个：
//当api接口发生变化时，需要重新编译本module以及所有使用本module的module
api：同compile作用一样，即认为本module将会泄露其依赖的module的内容。

//implementation 只编译一个module, 不会改动本module对外暴露的接口
implementation：本module不会通过自身的接口向外部暴露其依赖module的内容。
由此，我们可以明确的告诉gradle去重新编译一个module，若是这个使用的module
的接口发生变化的话。

2.x (3.0+)
compileOnly（provided）
只在编译时有效，不会参与打包
apk（runtimeOnly）
只在生成apk的时候参与打包，编译时不会参与，很少用。

testCompile（testImplementation）
testCompile 只在单元测试代码的编译以及最终打包测试apk时有效。

debugCompile（debugImplementation）
debugCompile 只在debug模式的编译和最终的debug apk打包时有效

releaseCompile（releaseImplementation）
Release compile 仅仅针对Release 模式的编译和最终的Release apk打包。

</pre>

## butterknife
目前最新的8.8.1在studio 3.0上是有问题的，可以降低点版本使用

examples : https://github.com/JetBrains/kotlin-examples/tree/master/gradle

1. using Kotlin, replace annotationProcessor with kapt.

2. <pre>classpath 'com.jakewharton:butterknife-gradle-plugin:8.5.1'

            apply plugin: 'kotlin-kapt'

            apply plugin: 'com.jakewharton.butterknife'

            implementation (deps.butterknife){
                    exclude group: 'com.android.support', module: 'support-compat'
                }
                //annotationProcessor deps.butterknife_compiler
                kapt deps.butterknife_compiler
</pre>



## 模块化
建一个统一的版本管理versions.gradle

apply from: 'versions.gradle'

先建一个正常的项目，直接建libmodule会有很多问题

在gradle.properties添加了一行代码 isLibaray = false

然后我们在组件的build.gradle文件中读出这行代码：
<pre>
if (isModule.toBoolean()) {
    apply plugin: 'com.android.application'
} else {
    apply plugin: 'com.android.library'
}

        if(!isLibaray.toBoolean()){
            applicationId "com.example.modulepermission"
        }

sourceSets {
    main {
        if (isModule.toBoolean()) {
            manifest.srcFile 'src/main/debug/AndroidManifest.xml'
        } else {
            manifest.srcFile 'src/main/release/AndroidManifest.xml'
            //release模式下排除debug文件夹中的所有Java文件
            java {
                exclude 'debug/**'
            }
        }
    }
}
</pre>


### module关系

  <p>
  <pre>
                                view database  qrcode  skin    https
                                                                    download  imageload  pay  share
               rebeau-commons-module

  </pre>


  </p>


## 检查依赖报告
gradlew :sample:dependencies查看库的依赖树

https://www.jianshu.com/p/a492bf61c0e6

https://stackoverflow.com/questions/44653261/android-dependency-has-different-version-for-the-compile-and-runtime

 <pre>
\--- com.jakewharton:butterknife:8.8.1
     +--- com.jakewharton:butterknife-annotations:8.8.1
     |    \--- com.android.support:support-annotations:25.3.0 -> 26.
     +--- com.android.support:support-annotations:25.3.0 -> 26.1.0
     \--- com.android.support:support-compat:25.3.0 -> 26.1.0 (*)

 V4 和 V7 的顶级依赖是 -> 25.1.1
 依赖报告中可以看到有些依赖标注了 *号，表示这个依赖被忽略了，
 这是因为其他顶级依赖中也依赖了这个传递的依赖，
 Gradle 会自动分析下载最合适的依赖。有些依赖形如22.2.1 ->23.1.1
 是由于默认会优先版本高的依赖.这个时候你想使用版本低的依赖的话
 需要排除掉高的依赖。因此这里需要排除Fresco里面的supportV4依赖。

        implementation (deps.butterknife){
            exclude group: 'com.android.support', module: 'support-compat'
        }
        annotationProcessor deps.butterknife_compiler
		
		强制使用某个版本
		dependencies {

			configurations.all {
				resolutionStrategy.eachDependency {
					DependencyResolveDetails details ->
						def requested = details.requested
						if (requested.group == 'com.android.support') {
							if (!requested.name.startsWith("multidex")) {
								details.useVersion ‘25.1.0’
							}
						}
				}
			}
		}
	
 </pre>
 
 
 ## 广告相关的东西
 http://fullstack.blog/2017/03/09/%E4%B8%9A%E5%8A%A1%E7%88%AC%E5%9D%91%E4%B8%8E%E6%80%BB%E7%BB%93%E2%80%94%E2%80%94%E5%BC%80%E5%B1%8F%E5%B9%BF%E5%91%8A%E7%83%AD%E5%90%AF%E5%8A%A8%E5%AE%9E%E7%8E%B0%E6%96%B9%E6%A1%88/
 
 
 ## 插件化
 https://www.jianshu.com/p/d9a823e94fb7
 
## module 单独混淆
https://blog.csdn.net/yuzhiqiang_1993/article/details/80676170
    buildTypes {
        release {
//            这里设置并不生效， 是否需要混淆是跟着主工程的配置来
            //minifyEnabled true
            consumerProguardFiles 'proguard-rules.pro'
        }

        debug {
//            这里设置并不生效， 是否需要混淆是跟着主工程的配置来
            //minifyEnabled false
            consumerProguardFiles 'proguard-rules.pro'
        }
    }

  proguard优化配置
            //proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
 
## material-components-android
 https://github.com/material-components/material-components-android

