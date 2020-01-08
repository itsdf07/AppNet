package com.itsdf07.lib.net.okhttp3;

/**
 * @Description:
 * @Author itsdf07
 * @E-Mail 923255742@qq.com
 * @Github https://github.com/itsdf07
 * @Date 2020/1/8
 */
public interface OkHttp3Callback {
    /**
     * 请求成功
     *
     * @param result    请求结果
     * @param isEncrpty 结果是否有加密
     */
    void onSuccess(String result, boolean isEncrpty);

    /**
     * 请求失败
     *
     * @param netCode 错误码
     * @param errMsg  错误信息
     */
    void onFailure(NetCode netCode, String errMsg);
}
