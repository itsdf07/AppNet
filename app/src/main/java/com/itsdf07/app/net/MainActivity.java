package com.itsdf07.app.net;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.itsdf07.lib.alog.ALog;
import com.itsdf07.lib.alog.ALogLevel;
import com.itsdf07.lib.alog.FileUtils;
import com.itsdf07.lib.net.okhttp3.NetCode;
import com.itsdf07.lib.net.okhttp3.OkHttp3ProgressCallback;
import com.itsdf07.lib.net.okhttp3.OkHttp3Utils;
import com.itsdf07.lib.net.okhttp3.RequestInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ALog.init().setLogLevel(ALogLevel.FULL);
        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUploadFileDebug();
            }
        });
    }


    /**
     * http接口调试
     */
    private void onUploadFileDebug() {
        String filePath = FileUtils.getInnerSDPath(this) + "/ALOG/123456.png";
        File file = new File(filePath);
        String url = "http://121.199.44.234:9381/ROPplatform/uploadPhoto";
        Map<String, String> params = new HashMap<>();
        params.put("imageName", "100210044.png");
        params.put("userName", "100210044");
        params.put("md5", MD5Utils.getFileMD5(file));
        OkHttp3Utils.getInstance().postAsyn2UploadFile(url, file, params, new OkHttp3ProgressCallback() {
            @Override
            public void onStart(RequestInfo requestInfo) {
                ALog.dTag(TAG, "开始上传，requestInfo:%s", requestInfo.toString());
            }

            @Override
            public void onProgress(long currentLen, long totalLen) {
                ALog.dTag(TAG, "上传中，totalLen:%s,currentLen:%s", totalLen, currentLen);
            }

            @Override
            public void onFailed(NetCode netCode, String errMsg) {
                ALog.dTag(TAG, "上传失败，netCode:%s", netCode.getInfo());
            }

            @Override
            public void onFinish() {
                ALog.dTag(TAG, "上传结束");
            }
        });
//
    }
}
