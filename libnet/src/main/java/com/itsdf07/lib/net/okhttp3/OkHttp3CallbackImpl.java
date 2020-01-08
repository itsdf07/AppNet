package com.itsdf07.lib.net.okhttp3;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @Description:
 * @Author itsdf07
 * @E-Mail 923255742@qq.com
 * @Github https://github.com/itsdf07
 * @Date 2020/1/8
 */
public abstract class OkHttp3CallbackImpl<Result extends OkBaseBean> implements OkHttp3Callback {
    @Override
    public void onSuccess(String result, boolean isEncrpty) {
        if (isEncrpty) {
            //如果数据是加密的，则接收到后不处理直接抛出，由外部解密后进行解析
            OkBaseBean bean = new OkBaseBean();
            bean.setEncrpty(true);
            bean.setEncrptyResult(result);
            onSuccess((Result) bean);
            return;
        }
        Gson gson = new Gson();
        try {
            Class<?> clz = analysisClassInfo(this);
            Result bean = (Result) gson.fromJson(result, clz);
            if (null == bean) {
                onFailure(NetCode.CODE_40305, "Gson解析失败");
                return;
            }
            if (TextUtils.isEmpty(bean.getCode())) {
                onFailure(NetCode.CODE_40305, "应答码解析异常");
                return;
            }
            if (bean.getCode().equals(NetCode.CODE_20000.status + "")) {
                bean.setEncrpty(false);
                onSuccess(bean);
            } else {
                onFailure(NetCode.CODE_40305, "code:" + bean.getCode() + ",desc:" + bean.getDesc());
            }
        } catch (JsonSyntaxException e) {
            onFailure(NetCode.CODE_40305, "Gson解析异常,e:" + e.toString());
        } catch (Exception e) {
            onFailure(NetCode.CODE_40000, "未知错误,e:" + e.toString());

        }
    }

    @Override
    public void onFailure(NetCode code, String errMsg) {
        OkBaseBean bean = new OkBaseBean();
        bean.setCode(code.status + "");
        bean.setDesc(errMsg);
        onFailed(bean);
    }


    /**
     * @param object
     * @return
     */
    private Class<?> analysisClassInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

    //--------------------------------------

    /**
     * 开始请求
     *
     * @param requestInfo
     */
    public abstract void onStart(RequestInfo requestInfo);

    /**
     * 成功请求http并成功解析响应的Json结果的回调
     *
     * @param result gson解析对象
     */
    public abstract void onSuccess(Result result);

    /**
     * 应答错误码
     *
     * @param bean code + desc
     */
    public abstract void onFailed(OkBaseBean bean);

    /**
     * 请求结束
     */
    public abstract void onFinish();
}
