#

FileProvider是ContentProvider的一个特殊子类，本质上还是基于ContentProvider的实现，
FileProvider会把"file:///"的路径转换为特定的"content://"形式的content uri，接收方
通过这个uri再使用ContentResolver去媒体库查询解析

```
    <provider     
      android:name="android.support.v4.content.FileProvider" //指向v4包里的FileProvider类
      android:authorities="com.example.myapp.fileprovider" //对应你的content uri的基础域名，生成的uri将以content://com.example.myapp.fileprovider作为开头
      android:grantUriPermissions="true" //设置允许获取访问uri的临时权限
      android:exported="false"//设置不允许导出，我们的FileProvider应该是私有的
    >            
    <meta-data
      android:name="android.support.FILE_PROVIDER_PATHS" 
      android:resource="@xml/filepaths" //用于设置FileProvider的文件访问路径
    />        
    </ provider> 
    
    name ：配置当前 FileProvider 的实现类。
    authorities：配置一个 FileProvider 的名字，它在当前系统内需要是唯一值。
    exported：表示该 FileProvider 是否需要公开出去，这里不需要，所以是 false。
    granUriPermissions：是否允许授权文件的临时访问权限。这里需要，所以是 true。

    
    xml目录下新建一个filepaths.xml
    <paths xmlns:android="http://schemas.android.com/apk/res/android"> 
        <files-path name="my_images" path="images/"/> 
        ...
    </paths>
```

|  |  |  |
| :---------| :---------| :-------- | 
|root-path                 |                                           |  代表/ 也就是Android设备的根目录,该目录下包含着手机内部存储器，外置SD卡等所有文件的目录 |
|files-path                | getFilesDir()                             | /data/data/com.xxxxx.xxx/files/ |
|cache-path                | getCacheDir()                             | /data/data/com.xxxxx.xxx/cache | 
|external-path             | Environment.getExternalStorageDirectory() | /storage/emulated/0 | 
|external-files-path       | getExternalFilesDir(null)                 | /storage/emulated/0/Android/data/com.xxx.xxx/files | 
|external-cache-path       | getExternalCacheDir(null)                 | /storage/emulated/0/Android/data/com.xxx.xxx/cache | 
|external-media-path       | getExternalMediaDirs()                    | /storage/emulated/0/Android/media/com.xxx.xxx | 


- name属性：指明了FileProvider在content uri中需要添加的部分，这个值隐藏了分享文件的子目录，具体的文件真实路径在path字段中保存，
  这里的name为my_images,所以对应的content uri为

        content://com.example.myapp.fileprovider/my_images
        
- path属性：path属性的值是子路径，分享文件的真实路径，这里的path值为"images/"，那组合起来的路径如下所示    
    
        content://com.example.myapp.fileprovider/my_images/images/ 