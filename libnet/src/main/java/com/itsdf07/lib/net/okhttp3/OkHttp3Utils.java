package com.itsdf07.lib.net.okhttp3;

import android.text.TextUtils;

import java.io.File;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * @Description:
 * @Author itsdf07
 * @E-Mail 923255742@qq.com
 * @Github https://github.com/itsdf07
 * @Date 2020/1/8
 */
public class OkHttp3Utils {
    public static OkHttp3Utils getInstance() {
        return OkHttp3UtilsHolder.instance;
    }

    private static class OkHttp3UtilsHolder {
        private static final OkHttp3Utils instance = new OkHttp3Utils();
    }

    /**
     * 校验URL的合法性
     *
     * @param url
     * @return
     */
    private boolean checkUrl(String url) {
        HttpUrl httpUrl = HttpUrl.parse(url);
        return httpUrl != null && !TextUtils.isEmpty(url);
    }

    /**
     * 数据请求
     * 备注:目前未处理网络是否可用问题，所以需要先保证手机的网络情况
     *
     * @param url
     * @param json
     */
    public void postAsyn2Data(String url, String json, OkHttp3CallbackImpl callback) {
        postAsyn2Data(url, json, callback, false);
    }

    /**
     * 数据请求
     * 备注:目前未处理网络是否可用问题，所以需要先保证手机的网络情况
     *
     * @param url
     * @param json
     * @param isEncrpty 是否加密过的数据,true-已加密,false-未加密
     */
    public void postAsyn2Data(String url, String json, OkHttp3CallbackImpl callback, boolean isEncrpty) {
        if (!checkUrl(url)) {
            if (null != callback) {
                callback.onFailure(NetCode.CODE_40505, "URL不合法");
            }
            return;
        }
        if (TextUtils.isEmpty(json)) {
            callback.onFailure(NetCode.CODE_40305, "请求格式不正确");
            return;
        }
        RequestInfo requestInfo = new RequestInfo();
        Request request = OkHttp3Helper.getInstance().builderRequest2Data(OkHttp3Helper.HttpMethodType.POST, url, null, json, requestInfo);
        OkHttp3Helper.getInstance().doEnqueue(request, callback, isEncrpty, requestInfo);
    }

    /**--------------------    文件上传    --------------------**/
    /**
     * 单文件上传：header带参，body带文件流
     *
     * @param url      请求地址
     * @param file     上传文件
     * @param callback 请求回调
     * @return Call
     */
    public void postAsyn2UploadFile(String url, File file, Map<String, String> params, OkHttp3ProgressCallback callback) {
        if (!checkUrl(url)) {
            if (null != callback) {
                callback.onFailed(NetCode.CODE_40505, "URL不合法");
            }
            return;
        }
        if (!NetFileUtils.isFileExists(file)) {
            if (null != callback) {
                callback.onFailed(NetCode.CODE_40104, "上传的文件不存在");
            }
            return;
        }
        RequestInfo requestInfo = new RequestInfo();
        Request request = OkHttp3Helper.getInstance().builderFileMapRequest(url, file, params, callback, requestInfo);
        OkHttp3Helper.getInstance().doEnqueue(request, callback, requestInfo);
    }

    /**--------------------    文件下载    --------------------**/
    /**
     * Post异步请求:文件下载(如果文件已存在，会先删除)
     * 备注:目前未处理权限问题，所以需要先保证app的读写手机存储的权限已开启
     *
     * @param url          请求地址
     * @param destFileDir  目标文件存储的文件夹路径，如：Environment.getExternalStorageDirectory().getAbsolutePath()
     * @param destFileName 目标文件存储的文件名，如：xxx.apk
     * @param callback     请求回调
     * @Description 文件下载
     */
    public void postAsyn2DownloadFile(String url, String destFileDir, String destFileName, OkHttp3ProgressCallback callback) {
        if (!checkUrl(url)) {
            if (null != callback) {
                callback.onFailed(NetCode.CODE_40505, "URL不合法");
            }
            return;
        }
        if (TextUtils.isEmpty(destFileDir)) {
            callback.onFailed(NetCode.CODE_40104, "文件存储目录不能为空");
            return;
        }
        if (TextUtils.isEmpty(destFileName)) {
            callback.onFailed(NetCode.CODE_40104, "文件名不能为空");
            return;
        }
        RequestInfo requestInfo = new RequestInfo();
        Request request = OkHttp3Helper.getInstance().builderRequest2Data(OkHttp3Helper.HttpMethodType.POST, url, null, null, requestInfo);
        OkHttp3Helper.getInstance().doDownloadEnqueue(request, destFileDir, destFileName, callback, requestInfo);
    }
}
