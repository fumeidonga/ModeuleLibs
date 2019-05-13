

#### RESTFUL API ####
[RESTful](http://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html) 是目前最流行的 API 设计规范，用于 Web 数据接口的设计

- 资源与URI
任何事物，只要有被引用到的必要，它就是一个资源

uri 就理解为一个网址好了

- 统一资源接口
get

post

put

delete

- 资源的表述
xml、json等格式，图片可以使用PNG或JPG展现



#### RETROFIT ####

[参考文章](https://blog.csdn.net/carson_ho/article/details/73732076)
[参考文章](https://www.jianshu.com/p/308f3c54abdd?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io)

![原理](https://github.com/fumeidonga/markdownPic/blob/master/http/2019-03-19_202239.png?raw=true)

![图片][id]

[id]:https://github.com/fumeidonga/markdownPic/blob/master/http/944365-2bd80b234ae9d155.png?raw=true

准确来说，Retrofit 是一个 RESTful 的 HTTP 网络请求框架的封装
网络请求的工作本质上是 OkHttp 完成，而 Retrofit 仅负责 网络请求接口的封装

app - retrofit - okhttp - server

### 注解类型 ###
#### 网络请求方法、参数 ####
下面的所有方法分别对应http中的网络请求方法，

Retrofit把 网络请求的URL 分成了几部分设置

    第1部分：定义网络请求接口的实例(eg：Translation，就是一个response对象)，并在网络请求接口的注解设置
    public interface TranslationService{
        @GET("openapi.do?keyfrom=")
        Call<Translation>  getCall();
    }

    第2部分：在创建Retrofit实例时通过.baseUrl()设置
    Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://fanyi.youdao.com/") //设置网络请求的Url地址, Retrofit2 的baseUlr 必须以 /（斜线） 结束
                    .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器，这样Retrofit就会使用Gson将ResponseBody转换我们想要的类型
                    .build();

    第3部分：创建网络请求接口的实例
    TranslationService request = retrofit.create(TranslationService.class);

    第4部分：创建请求，传入参数
    Call<Translation> call = request.getCall();

    第5部分：异步请求
    call.enqueue(new Callback<Translation>() {
        @Override
        public void onResponse(Call<Translation> call, Response<Translation> response) {

        }
        @Override
        public void onFailure(Call<Translation> call, Throwable t) {
        }
    });

    Retrofit支持动态改变网络请求根目录
    网络请求的完整 Url =在创建Retrofit实例时通过.baseUrl()设置 +网络请求接口的注解设置
    我们比较常用的是
    url='http://xxx.xxx.xxx/a/bc'
    path='bc'
    baseurl='http://xxx.xxx.xxx/a/'

![参数](https://github.com/fumeidonga/markdownPic/blob/master/http/httpparams.png?raw=true)

1. @GET

2. @POST

3. @PUT

4. @DELETE

5. @PATH

  所有url中问号前面的参数，都是属于path；
  http ://101.1.1/api/new/test    ?newid=1

6. @HEAD

7. @OPTIONS

8. @query、 @querymap

所有url中 问号 ？ 后面的参数

9. @HTTP
替换@GET、@POST、@PUT、@DELETE、@HEAD注解的作用 及 更多功能拓展

具体使用：通过属性method、path、hasBody进行设置

    public interface GetRequest_Interface {
        /**
         * method：网络请求的方法（区分大小写）
         * path：网络请求地址路径
         * hasBody：是否有请求体
         */
        @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
        Call<ResponseBody> getCall(@Path("id") int id);
        // {id} 表示是一个变量
        // method 的值 retrofit 不会做处理，所以要自行保证准确
    }

10. @Field

用于post中提交表单

#### 标记类 ####
1. @FromUrlEncoded
请求体是一个表单，表示发送form-encoded的数据，
每个键值对需要用@Filed来注解键名，随后的对象需要提供值


2. @Multipart
请求体是一个支持文件上传的表单，每个键值对需要用@Part来注解键名，随后的对象需要提供值

3. @Streaming
返回的数据以流的形式，如果没有改注解，默认把数据都载入内存

#### 网络请求参数 ####
1. @Header
不固定值的Header

2. @Headers
请求头

3. @URL

4. @Body
请求体

5. @Path

<pre>
    @GET("users/{user}/repos")
    Call<ResponseBody>  getBlog（@Path("user") String user ）;
    // 访问的API是：https://api.github.com/users/{user}/repos
    // 在发起请求时， {user} 会被替换为方法的第一个参数 user（被@Path注解作用）</pre>

6. @Field、@FieldMap

post表单键值对，数据体现在body上

7. @Part、@PartMap

post表单键值对，主要用于文件上传时

8. @Query、@QueryMap

post表单键值对，数据体现在url上

Query = Url 中 ‘?’ 后面的 key-value

    https://xxxx.com/api/v1/category/menu?type=boy&id=36&sign=xxx
    @GET("api/v1/category/menu")
    Observable<?> getClassifyFilterEntity(@QueryMap Map<String,String> map);

    如：url = http://www.println.net/?cate=android，其中，Query = cate
    @GET("/")
    Call<String> cate(@Query("cate") String cate);

### 数据解析器（Converter） ###
Converter 使用转换器将http的响应体转换成对应的类型

Retrofit支持多种数据解析方式,使用时需要在Gradle添加依赖

Gson           	com.squareup.retrofit2:converter-gson:2.0.2

Jackson	        com.squareup.retrofit2:converter-jackson:2.0.2

Simple XML	    com.squareup.retrofit2:converter-simplexml:2.0.2

Protobuf	    com.squareup.retrofit2:converter-protobuf:2.0.2

Moshi	        com.squareup.retrofit2:converter-moshi:2.0.2

Wire	        com.squareup.retrofit2:converter-wire:2.0.2

Scalars     	com.squareup.retrofit2:converter-scalars:2.0.2

在默认情况下Retrofit只支持将HTTP的响应体转换换为ResponseBody

但是可以用Converter将ResponseBody转换为我们想要的类型, 也可以自定义

#### 自定义Converter ####
![converter](https://github.com/fumeidonga/markdownPic/blob/master/http/2019-03-16_172859.png?raw=true)

### 网络请求适配器（CallAdapter） ###
CallAdapter简单来说就是将Call转换成对应的类型

eg：Call<Response> test(); =》 Flowable<Response> test();


Retrofit支持多种网络请求适配器方式：guava、Java8和rxjava

使用时如使用的是 Android 默认的 CallAdapter，则不需要添加网络请求适配器的依赖，
否则则需要按照需求进行添加 ，Retrofit 提供的 CallAdapter

guava	 com.squareup.retrofit2:adapter-guava:2.0.2

Java8	 com.squareup.retrofit2:adapter-java8:2.0.2

rxjava	 com.squareup.retrofit2:adapter-rxjava:2.0.2

com.squareup.retrofit2:retrofit:$versions.retrofit

com.squareup.retrofit2:converter-gson:$versions.retrofit

com.squareup.retrofit2:adapter-rxjava2:$versions.retrofit

#### 自定义Converter ####
![converter](https://github.com/fumeidonga/markdownPic/blob/master/http/2019-03-16_172859.png?raw=true)

### 使用 Retrofit 的步骤共有7个 ###

步骤1：添加Retrofit库的依赖

步骤2：创建 接收服务器返回数据 的类

步骤3：创建 用于描述网络请求 的接口
<pre>Retrofit将 Http请求 抽象成 Java接口：采用 注解 描述网络请求参数 和配置网络请求参数
1.用 动态代理 动态 将该接口的注解“翻译”成一个 Http 请求，最后再执行 Http 请求
2.注：接口中的每个方法的参数都需要使用注解标注，否则会报错
</pre>
<pre>
<code>
public interface GetRequest_Interface {
    @GET("openapi.do?keyfrom=")
    Call<Translation>  getCall();
    // @GET注解的作用:采用Get方法发送网络请求

    // getCall() = 接收网络请求数据的方法
    // 其中返回类型为Call<*>，*是接收数据的类（即上面定义的Translation类）
    // 如果想直接获得Responsebody中的内容，可以定义网络请求返回值为Call<ResponseBody>
}
</code>
</pre>

步骤4：创建 Retrofit 实例
<pre>
Retrofit retrofit = new Retrofit.Builder()
     .baseUrl("http://fanyi.youdao.com/") // 设置网络请求的Url地址
     .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
     .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
     .build();
</pre>


步骤5：创建 网络请求接口实例 并 配置网络请求参数
<pre>// 创建 网络请求接口 的实例
 GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);

 //对 发送请求 进行封装
 Call<Reception> call = request.getCall();
 </pre>

步骤6：发送网络请求（异步 / 同步）

封装了 数据转换、线程切换的操作
<pre>
//发送网络请求(异步)
 call.enqueue(new Callback<Translation>() {
     //请求成功时回调
     @Override
     public void onResponse(Call<Translation> call, Response<Translation> response) {
         //请求处理,输出结果
         response.body().show();
     }

     //请求失败时候的回调
     @Override
     public void onFailure(Call<Translation> call, Throwable throwable) {
         System.out.println("连接失败");
     }
 });

// 发送网络请求（同步）
Response<Reception> response = call.execute();
 // 对返回数据进行处理
  response.body().show();
</pre>


步骤7： 处理服务器返回的数据

见步骤6




### 动态改变网络请求根目录 ###







