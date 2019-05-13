package com.rebeau.technology.android.testretrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface DefaultServerApi {

    String BASE_URL = "http://101.1.1/api/";
    Retrofit retrofit = new Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(new StringConverterFactory())
            .baseUrl(BASE_URL)
            .build();
    //http://101.1.1/api/


    //http://101.1.1/api/openapi.do
    @GET("openapi.do")                     //采用Get方法发送网络请求
    Call<ResponseBody> get();              //返回类型为Call<*>，*是接收数据的类
    //直接获得Responsebody中的内容


    /**
     * url 中带有参数
     * @param newid
     * @return
     */
    //http://101.1.1/api/new/1
    @GET("new/{newid}")
    Call<ResponseBody> path(@Path("newid") String newid);

    //http://101.1.1/api/new/1/ad
    @GET("new/{newid}/{type}")
    Call<ResponseBody> path1(@Path("newid") String newid, @Path("type") String type);

    /**
     * url后面带有参数, 多个参数后面多个query
     */
    //http://101.1.1/api/new/te?newid=1
    @GET("new/te")
    Call<ResponseBody> query(@Query("newid") String newid);


    /**
     * 一连串的参数就使用querymap
     * @param map
     * @return
     */
    //http://101.1.1/api/new/te?newid=1&type=o&pos=0
    @GET("new/te")
    Call<ResponseBody> queryMap(@QueryMap Map<String, String> map);


    //http://101.1.1/api/new/1
    @POST("new/{newid}")
    Call<ResponseBody> post(@Path("newid") String newid, @Field("field") String field);

    //http://101.1.1/api/new/1?id=1
    @POST("new/{newid}")
    Call<ResponseBody> post1(@Path("newid") String newid, @Query("id") String id, @Field("field") String field);

    //http://101.1.1/api/new/1
    @POST("new/{newid}")
    Call<RequestBody> post2(@Path("newid") String newid, @Body DefaultServerApi api);


    /**
     * 自定义converter
     * Converter<F, T>    FROM -> TO
     * Converter<RequestBody, String>
     */
    class StringConverter implements Converter<RequestBody, String> {

        @Override
        public String convert(RequestBody value) throws IOException {
            return value.toString();
        }
    }

    class StringConverterFactory extends Converter.Factory {
        // 这里创建从ResponseBody其它类型的Converter，如果不能处理返回null
        // 主要用于对响应体的处理
        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return super.responseBodyConverter(type, annotations, retrofit);
        }
        // 在这里创建 从自定类型到ResponseBody 的Converter,不能处理就返回null，
        // 主要用于对Part、PartMap、Body注解的处理
        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
        }
        // 这里用于对Field、FieldMap、Header、Path、Query、QueryMap注解的处理
        // Retrfofit对于上面的几个注解默认使用的是调用toString方法
        @Override
        public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return super.stringConverter(type, annotations, retrofit);
        }
    }


    class CustomCall <R> {
        public final Call<R> call;

        public CustomCall(Call<R> call) {
            this.call = call;
        }

        public R get() throws IOException {
            return call.execute().body();
        }
    }

    class CustomCallAdapter<R> implements CallAdapter<R,CustomCall<?>> {
        private final Type responseType;
        // 下面的 responseType 方法需要数据的类型
        CustomCallAdapter(Type responseType) {
            this.responseType = responseType;
        }
        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public CustomCall<R> adapt(Call<R> call) {
            // 由 CustomCall 决定如何使用
            return new CustomCall<>(call);
        }
    }
    class CustomCallAdapterFactory extends CallAdapter.Factory{

        @Override
        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            // 获取原始类型
            Class<?> rawType = getRawType(returnType);
            // 返回值必须是CustomCall并且带有泛型
            if (rawType == CustomCall.class && returnType instanceof ParameterizedType) {
                Type callReturnType = getParameterUpperBound(0, (ParameterizedType) returnType);
                return new CustomCallAdapter(callReturnType);
            }
            return null;
        }
    }

}
