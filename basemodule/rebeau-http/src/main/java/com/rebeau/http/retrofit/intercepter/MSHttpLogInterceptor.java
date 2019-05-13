package com.rebeau.http.retrofit.intercepter;


import com.rebeau.base.utils.RBJsonUtil;
import com.rebeau.base.utils.RBLogUtil;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * 使用HttpLoggingInterceptor 进行日志的拦截
 * com.squareup.okhttp3:logging-interceptor:3.14.0
 */
public class MSHttpLogInterceptor implements HttpLoggingInterceptor.Logger {

    private StringBuffer mMessage = new StringBuffer();
    @Override
    public void log(String message) {

        // 请求或者响应开始
        if (message.startsWith("--> POST")) {
            mMessage.setLength(0);
            mMessage.append(" ");
            mMessage.append("\r\n");
        }
        if (message.startsWith("--> GET")) {
            mMessage.setLength(0);
            mMessage.append(" ");
            mMessage.append("\r\n");
        }
        // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
        if ((message.startsWith("{") && message.endsWith("}"))
                || (message.startsWith("[") && message.endsWith("]"))) {
            message = RBJsonUtil.formatJson(message);
        }
        mMessage.append(message.concat("\n"));
        // 请求或者响应结束，打印整条日志
        if (message.startsWith("<-- END HTTP")) {
            RBLogUtil.d(mMessage.toString());
        }
    }
}
