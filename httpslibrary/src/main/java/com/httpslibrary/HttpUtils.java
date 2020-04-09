package com.httpslibrary;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import common.library.LogPrint;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtils {

    String TAG = HttpUtils.class.getSimpleName();

    private static HttpUtils instance;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public Retrofit.Builder getBuilder(String baseUrl) {
        Retrofit.Builder builder  = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder;
    }
    private OkHttpClient getOkClient() {
        OkHttpClient  mClient = new OkHttpClient.Builder()
                .addInterceptor(REWRITE_HEADER_CONTROL_INTERCEPTOR)
                .addNetworkInterceptor(new LoggingInterceptor())
                .connectTimeout(30, TimeUnit.SECONDS)// 请求超时10秒
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                .build();
        return mClient;
    }

    Interceptor REWRITE_HEADER_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("Content-Type", "charset=utf-8")
                    .build();
            return chain.proceed(request);
        }
    };

    class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();
            long t1 = System.nanoTime();//请求发起的时间
            LogPrint.logE(TAG, String.format("---发送请求 %s on %s%n%s",
                    request.url(),
                    chain.connection(),
                    request.headers()));
            Response response = chain.proceed(request);
            long t2 = System.nanoTime();//收到响应的时间
            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);

            LogPrint.logE(TAG, String.format("----%.1fms%n%s 接收响应: [%s] %n返回json:【%s】 ",
                    (t2 - t1) / 1e6d,
                    response.request().url(),
                    responseBody.string(),
                    response.headers()));

            return response;
        }
    }
}
