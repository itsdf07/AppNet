package com.itsdf07.lib.net.okhttp3;

/**
 * @Description
 * @Author itsdf07
 * @E-Mail 923255742@qq.com
 * @Github https://github.com/itsdf07
 * @Date 2020/1/8
 */

public interface OkHttp3ProgressCallback {
    /**
     * 开始请求
     *
     * @param requestInfo
     */
    void onStart(RequestInfo requestInfo);

    /**
     * 下载/上传进度
     *
     * @param currentLen 已经上传的字节大小
     * @param totalLen   文件的总字节大小
     */
    void onProgress(long currentLen, long totalLen);

    /**
     * 应答错误码
     *
     * @param netCode
     */
    void onFailed(NetCode netCode, String errMsg);

    /**
     * 请求结束
     */
    void onFinish();
}