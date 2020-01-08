package com.itsdf07.lib.net.okhttp3;

/**
 * @Description: 服务器业务状态码
 * 长度为5的int型
 * 万位数：状态码分类，2-成功类，4-异常类
 * 百位数、千位数：业务码分类，0-未知数据，1-缺失/已存在类（如无数据、无效数据、重复数据），2-数据校验失败类（如密码不正确），3-数据异常类（如数据不可删除、不可变更）,5-URL异常，6-Socket异常
 * 个位数、十位数：细节分类
 * @Author itsdf07
 * @E-Mail 923255742@qq.com
 * @Github https://github.com/itsdf07
 * @Date 2020/1/8
 **/
public enum NetCode {
    CODE_20000(20000, "处理成功", "success"),
    CODE_40000(40000, "未知错误", "unknown error"),
    CODE_40101(40101, "数据丢失", "miss data"),
    CODE_40102(40102, "重复数据", ""),
    CODE_40104(40104, "无效数据", ""),
    CODE_40201(40201, "数据校验失败", ""),
    CODE_40301(40301, "数据不可删除", ""),
    CODE_40302(40302, "数据不可更新", ""),
    CODE_40303(40303, "数据添加失败", ""),
    CODE_40304(40304, "数据失效", ""),
    CODE_40305(40305, "数据解析失败", ""),
    CODE_40505(40505, "URL请求错误", ""),
    CODE_40506(40506, "未知服务器", ""),
    CODE_40601(40601, "网络请求超时", ""),
    CODE_40602(40602, "网络请求异常", "");

    int status;
    String desc;
    String info;

    /**
     * @param status 业务状态码
     * @param desc   中文业务响应消息
     * @param info   英文业务响应消息
     */
    NetCode(int status, String desc, String info) {
        this.status = status;
        this.desc = desc;
        this.info = info;
    }

    public static String getDesc(NetCode netCode) {
        return netCode.desc;
    }

    public static int getStatus(NetCode netCode) {
        return netCode.status;
    }

    public String getInfo() {
        return info;
    }

    public NetCode setInfo(String info) {
        this.info = info;
        return this;
    }
}
