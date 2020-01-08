package com.itsdf07.lib.net.okhttp3;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @Description:
 * @Author itsdf07
 * @E-Mail 923255742@qq.com
 * @Github https://github.com/itsdf07
 * @Date 2020/1/8
 */
public class OkHttp3Helper {

    /**
     * @Description 请求方式
     */
    enum HttpMethodType {
        GET,
        POST,
    }

    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");//JSON数据格式
    private static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");//二进制流数据

    private OkHttpClient okHttpClient;

    public OkHttp3Helper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //默认15秒连接超时
        builder.connectTimeout(NetConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(NetConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NetConfig.DEFAULT_TIMEOUT, TimeUnit.SECONDS);

//        File httpCacheDirectory = new File(FFileUtils.INNERSDPATH, "http_cache");
//        Cache cache = new Cache(httpCacheDirectory, 10240 * 1024 * 100); //100M
//        builder.cache(cache);

        okHttpClient = builder.build();
    }

    public static OkHttp3Helper getInstance() {
        return OkHttp3HelperHolder.instance;
    }

    private static class OkHttp3HelperHolder {
        private static final OkHttp3Helper instance = new OkHttp3Helper();
    }

    /**
     * Post数据请求
     *
     * @param request  Request对象
     * @param callback 请求回调
     * @Description 异步请求
     */
    /**
     * Post数据请求
     *
     * @param request     Request对象
     * @param callback    请求回调
     * @param isEncrpty   请求数据是否加密过
     * @param requestInfo 请求信息容器
     * @return
     */
    public Call doEnqueue(final Request request, final OkHttp3CallbackImpl callback, final boolean isEncrpty, RequestInfo requestInfo) {
        if (null != callback) {
            callback.onStart(requestInfo);
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NetCode netCode = NetCode.CODE_40000;
                if (e instanceof UnknownHostException) {
                    netCode = NetCode.CODE_40506;
                } else if (e instanceof SocketTimeoutException) {
                    netCode = NetCode.CODE_40601;
                }
                paseFailResult(netCode.setInfo("\n\t\ttag:" + request.url().toString() + "\n\t\terr->" + e.getMessage()), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == response) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->null == response"), callback);
                    return;
                }
                if (null == response.body()) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->null == response.body()"), callback);
                    return;
                }
                String result = response.body().string();
                if (TextUtils.isEmpty(result)) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->body is empty"), callback);
                    return;
                }
                try {
                    if (response.isSuccessful()) {
                        paseSuccessResult(request.url().toString(), result, callback, isEncrpty);
                    } else {
                        paseFailResult(NetCode.CODE_40000.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\tcode:" + response.code() + ",\n\t\terr->:" + response.message()), callback);
                    }
                } catch (Exception e) {
                    paseFailResult(NetCode.CODE_40000.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->" + e.getMessage()), callback);
                }
            }
        });
        return call;
    }


    /**
     * Post数据请求
     *
     * @param request  Request对象
     * @param callback 请求回调
     * @Description 异步请求
     */
    public Call doEnqueue(final Request request, final OkHttp3ProgressCallback callback, RequestInfo requestInfo) {
        if (null != callback) {
            callback.onStart(requestInfo);
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NetCode netCode = NetCode.CODE_40000;
                if (e instanceof UnknownHostException) {
                    netCode = NetCode.CODE_40506;
                } else if (e instanceof SocketTimeoutException) {
                    netCode = NetCode.CODE_40601;
                }
                paseFailResult(netCode.setInfo("\n\t\ttag:" + request.url().toString() + "\n\t\terr->" + e.getMessage()), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == response) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->null == response"), callback);
                    return;
                }
                if (null == response.body()) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->null == response.body()"), callback);
                    return;
                }
                String result = response.body().string();
                if (TextUtils.isEmpty(result)) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->body is empty"), callback);
                    return;
                }
                try {
                    if (response.isSuccessful()) {
                        paseSuccessResult(request.url().toString(), result, callback);
                    } else {
                        paseFailResult(NetCode.CODE_40000.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\tcode:" + response.code() + ",\n\t\terr->:" + response.message()), callback);
                    }
                } catch (Exception e) {
                    paseFailResult(NetCode.CODE_40000.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->" + e.getMessage()), callback);
                }
            }
        });
        return call;
    }

    /**--------------------    异步文件下载    --------------------**/

    /**
     * Post请求文件下载
     *
     * @param request      Request对象
     * @param destFileDir  目标文件存储的文件目录
     * @param destFileName 目标文件存储的文件名
     * @param callback     请求回调
     * @Description 异步下载请求
     */
    public Call doDownloadEnqueue(final Request request, final String destFileDir, final String destFileName, final OkHttp3ProgressCallback callback, RequestInfo requestInfo) {
        if (null != callback) {
            requestInfo.setBody(destFileDir + destFileName);
            callback.onStart(requestInfo);
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                NetCode netCode = NetCode.CODE_40000;
                if (e instanceof UnknownHostException) {
                    netCode = NetCode.CODE_40506;
                } else if (e instanceof SocketTimeoutException) {
                    netCode = NetCode.CODE_40601;
                }
                paseFailResult(netCode.setInfo("\n\t\ttag:" + request.url().toString() + "\n\t\terr->" + e.getMessage()), callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (null == response) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->null == response"), callback);
                    return;
                }
                if (null == response.body()) {
                    paseFailResult(NetCode.CODE_40101.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->null == response.body()"), callback);
                    return;
                }
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    byte[] buffer = new byte[2048];
                    int len;
                    long currentTotalLen = 0L;
                    long totalLen = response.body().contentLength();

                    File file = new File(destFileDir, destFileName);
                    NetFileUtils.createFileByDeleteOldFile(file);
                    fileOutputStream = new FileOutputStream(file);
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                        currentTotalLen += len;
                        paseProgressResult(currentTotalLen, totalLen, callback, false);
                    }
                    fileOutputStream.flush();
                    paseProgressResult(currentTotalLen, totalLen, callback, true);
                } catch (IOException e) {
                    if (e instanceof SocketException) {
                        paseFailResult(NetCode.CODE_40602.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->" + e.getMessage()), callback);
                    } else {
                        paseFailResult(NetCode.CODE_40000.setInfo("\n\t\ttag:" + call.request().url().toString() + "\n\t\terr->" + e.getMessage()), callback);
                    }
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        return call;
    }

    /**
     * 数据请求成功
     * 将http请求成功结果转至主线程
     *
     * @param tag
     * @param result
     * @param callback
     * @param isEncrpty
     */
    public static void paseSuccessResult(final String tag, final String result, final OkHttp3CallbackImpl callback, final boolean isEncrpty) {
        Platform.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    Log.v(NetConfig.TAG, "\nisEncrpty:" + isEncrpty + "\ntag:" + tag + "\nresult:" + result);
                    callback.onSuccess(result, isEncrpty);
                    callback.onFinish();
                }

            }
        });
    }


    /**
     * 数据请求成功
     * 将http请求成功结果转至主线程
     *
     * @param tag
     * @param result
     * @param callback
     */
    public static void paseSuccessResult(final String tag, final String result, final OkHttp3ProgressCallback callback) {
        Platform.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    Log.v(NetConfig.TAG, "\ntag:" + tag + "\nresult:" + result);
                    callback.onFinish();
                }

            }
        });
    }

    /**
     * 文件下载进度
     * 线程转为主线程
     *
     * @param currentLen
     * @param totalLen
     * @param callback
     * @param isFinish   是否下载完成
     */
    public static void paseProgressResult(final long currentLen, final long totalLen, final OkHttp3ProgressCallback callback, final boolean isFinish) {
        Platform.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                if (isFinish) {
                    callback.onFinish();
                } else {
                    Log.v(NetConfig.TAG, "下载中,totalLen:" + totalLen + ",currentLen:%s" + currentLen);
                    callback.onProgress(currentLen, totalLen);
                }
            }
        });
    }

    /**
     * 数据请求失败
     * 将http请求失败结果转至主线程
     *
     * @param netCode
     * @param callback
     */
    public static void paseFailResult(final NetCode netCode, final OkHttp3CallbackImpl callback) {
        Platform.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Log.v(NetConfig.TAG, "NetCode:" + netCode.status + ",\ndesc:" + netCode.desc + ",\ninfo:" + netCode.info);
                if (callback != null) {
                    callback.onFailure(netCode, netCode.getDesc(netCode) + "\n[" + netCode.info + "]");
                    callback.onFinish();
                }

            }
        });
    }


    /**
     * 数据请求失败
     * 将http请求失败结果转至主线程
     *
     * @param netCode
     * @param callback
     */
    public static void paseFailResult(final NetCode netCode, final OkHttp3ProgressCallback callback) {
        Platform.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                Log.v(NetConfig.TAG, "NetCode:" + netCode.status + ",\ndesc:" + netCode.desc + ",\ninfo:" + netCode.info);
                if (callback != null) {
                    callback.onFailed(netCode, netCode.getInfo());
                    callback.onFinish();
                }

            }
        });
    }

    /**
     * builder json/map 格式的data数据请求体：Request
     *
     * @param methodType  请求方式
     * @param url         请求地址
     * @param params      请求参数
     * @param json        json数据格式
     * @param requestInfo 请求信息容器
     * @return
     */
    public Request builderRequest2Data(HttpMethodType methodType, String url, Map<String, String> params, String json, RequestInfo requestInfo) {
        Request request;
        Request.Builder builder = new Request.Builder().url(url);
        String logBody = "";
        if (methodType == HttpMethodType.POST) {
            if (json != null) {
                logBody = json;
                RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, json);
                builder.post(body);
            } else {
                RequestBody body = builderRequestFormMapData(params);
                builder.post(body);
                logBody = buildLogBodyFromMap(params);
            }
        } else if (methodType == HttpMethodType.GET) {
            builder.get();
            logBody = "这是get提交方式，没有body内容";
        }
        request = builder.build();
        Log.v(NetConfig.TAG, "\nURL:" + request.url().toString() + "\n" +
                "Method:" + request.method() + "\n" +
                "header:[" + request.headers().toString() + "]\n" +
                "body:" + logBody);
        requestInfo.setUrl(request.url().toString());
        requestInfo.setMethod(request.method());
        requestInfo.setHeaders(request.headers().toString());
        requestInfo.setBody(logBody);
        requestInfo.setTag(request.tag().toString());
        return request;
    }


    /**
     * builder map 格式的文件数据请求体：Request
     * <br/>内含上传进度
     *
     * @param url      请求地址
     * @param file     上传文件
     * @param params   header参数
     * @param callback 请求回调
     * @Description Request对象
     */
    public static Request builderFileMapRequest(String url, File file, Map<String, String> params, OkHttp3ProgressCallback callback, RequestInfo requestInfo) {
        Request request;
        String filePath = "";
        Request.Builder builder = new Request.Builder().url(url);
        if (null != params && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addHeader(key, params.get(key));
            }
        }
        if (NetFileUtils.isFileExists(file)) {
            RequestBody body = builderFormFileData(file, callback);
            builder.post(body);
            filePath = file.getPath();
        }
        request = builder.build();
        Log.v(NetConfig.TAG, "\nURL:" + request.url().toString() + "\n" +
                "Method:" + request.method() + "\n" +
                "header:[" + request.headers().toString() + "]\n" +
                "body:" + filePath);
        requestInfo.setUrl(request.url().toString());
        requestInfo.setMethod(request.method());
        requestInfo.setHeaders(request.headers().toString());
        requestInfo.setBody(filePath);
        requestInfo.setTag(request.tag().toString());
        return request;
    }

    /**
     * 将Map数据封装成RequestBody对象
     *
     * @param params 请求参数
     * @Description RequestBody对象
     */
    private RequestBody builderRequestFormMapData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                builder.add(key, value);
            }
        }
        return builder.build();
    }

    /**
     * 获取存放上传文件的请求载体RequestBody，并实现进度数据透传
     *
     * @param file
     * @param callback
     * @return RequestBody对象
     */
    private static RequestBody builderFormFileData(File file, final OkHttp3ProgressCallback callback) {
        ProgressRequestBody progressRequestBody = null;
        if (null != file) {
            RequestBody requestBody = RequestBody.create(MEDIA_TYPE_STREAM, file);
            progressRequestBody = new ProgressRequestBody(requestBody, new ProgressRequestBody.Listener() {
                @Override
                public void onRequestProgress(final long byteWrited, final long contentLength) {
                    Platform.getInstance().execute(new Runnable() {
                        @Override
                        public void run() {
                            callback.onProgress(byteWrited, contentLength);
                        }
                    });
                }
            });
        }

        return progressRequestBody;
    }

    /**
     * body log
     *
     * @param params
     * @return
     */
    private String buildLogBodyFromMap(Map<String, String> params) {
        String log = "";
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                String value = params.get(key);
                log += key + ":" + value + ",";
            }
        }
        return log;
    }
}
