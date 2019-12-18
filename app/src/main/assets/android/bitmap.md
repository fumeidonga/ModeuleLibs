

### Bitmap.Config类

#### ALPHA_8
每个像素占用1字节（8位），存储的是透明度信息，只保存透明度，不保存颜色。

#### RGB_565
 每像素占用2字节 ，即R=5，G=6，B=5，没有透明度，一个像素点占5+6+5=16位

#### ARGB_4444
已经废弃

#### ARGB_8888
每像素占用4字节 ，即A=8，R=8，G=8，B=8，一个像素点占8+8+8+8=32位
质量最高，内存占用也高

比如960 * 1796  * 4 / 1024 /1024 = 3M

#### RGBA_F16

#### HARDWARE



### 方式 二

#### Application.ActivityLifecycleCallbacks

看例子 DVActivityLifecycleCallbacks










