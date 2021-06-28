package com.fastproject.demo.data.remote;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.alibaba.fastjson.JSON;
import com.fastproject.demo.app.AppApplication;
import com.fastproject.demo.app.AppExecutors;
import com.fastproject.demo.data.remote.model.TestData;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RemoteDataSource {

    private static final String TAG = "RemoteDataSource";

    private static RemoteDataSource INSTANCE = null;
    private static AppExecutors mAppExecutors;
    public OkHttpClient httpClient;

    public static RemoteDataSource getInstance() {
        mAppExecutors = AppApplication.getInstance().getAppExecutors();
        if (INSTANCE == null) {
            synchronized (RemoteDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RemoteDataSource();
                }
            }
        }
        return INSTANCE;
    }

    private RemoteDataSource() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.sslSocketFactory(createSSLSocketFactory());
        builder.addInterceptor(new LogInterceptor());
        builder.hostnameVerifier(new TrustAllHostnameVerifier());
        httpClient = builder.connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
    }


    public class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    public class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.e("LogInterceptor", "request:" + request);
            Log.e("LogInterceptor", "params:" + JSON.toJSONString(request.toString()));
            Log.e("LogInterceptor", "System.nanoTime():" + System.nanoTime());
            Response response = chain.proceed(request);
            Log.e("LogInterceptor", "request:" + request);
            Log.e("LogInterceptor", "System.nanoTime():" + System.nanoTime());
            return response;
        }
    }

    public class TrustAllCerts implements X509TrustManager {

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


    public MutableLiveData<List<TestData.TestDataChildBean>> getNetTest(String info) {
        final MutableLiveData<List<TestData.TestDataChildBean>> testLiveData = new MutableLiveData<>();
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String URL = "testUrl";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("testCode", info);
                try {
                    Log.d(TAG, "test: request \n" + "URL:" + URL);
                    Response response = syncPost(URL, params);
                    String responseStr = response.body().string();
                    Log.d(TAG, "test: response " + responseStr + "\n");
                    TestData responseBean = JSON.parseObject(responseStr, TestData.class);
                    if (null != responseBean && null != responseBean.getData()) {
                        List<TestData.TestDataChildBean> dataChildBeanList = responseBean.getData();
                        testLiveData.postValue(dataChildBeanList);
                    } else {
                        testLiveData.postValue(null);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    testLiveData.postValue(null);
                }
            }
        });
        return testLiveData;
    }


    /**
     * post JSON
     *
     * @param url
     * @param json
     */
    private Response syncPostJson(String url, String json) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Request request = new Request.Builder()
                .url(url)
                //.addHeader("wx_token","d3eb9a9233e52948740d7eb8c3062d14")
                .post(body)
                .build();
        Response response;
        try {
            response = httpClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            response = null;
        }
        return response;
    }

    /**
     * 同步post请求
     *
     * @param url
     * @param params
     */
    private Response syncPost(String url, HashMap<String, String> params) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return httpClient.newCall(request).execute();
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param params
     * @param callback
     */
    private void post(String url, HashMap<String, String> params, Callback callback) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        FormBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(callback);
    }
}
