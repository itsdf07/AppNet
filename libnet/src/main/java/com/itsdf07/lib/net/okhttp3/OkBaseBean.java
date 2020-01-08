package com.itsdf07.lib.net.okhttp3;

/**
 * @Description:
 * @Author itsdf07
 * @E-Mail 923255742@qq.com
 * @Github https://github.com/itsdf07
 * @Date 2020/1/8
 */
public class OkBaseBean {
    //应答码
    private String code;
    //应答信息
    private String desc;
    //是否有加密
    private boolean isEncrpty;
    //加密结果串，待解密
    private String encrptyResult;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isEncrpty() {
        return isEncrpty;
    }

    public void setEncrpty(boolean encrpty) {
        isEncrpty = encrpty;
    }

    public String getEncrptyResult() {
        return encrptyResult;
    }

    public void setEncrptyResult(String encrptyResult) {
        this.encrptyResult = encrptyResult;
    }

    @Override
    public String toString() {
        if (isEncrpty) {
            return "code:" + code + ",desc:" + desc + ",encrptyResult" + encrptyResult;
        }
        return "code:" + code + ",desc:" + desc;
    }
}
