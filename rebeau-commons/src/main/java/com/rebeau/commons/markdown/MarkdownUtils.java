package com.rebeau.commons.markdown;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * MarkdownUtils.setData()
 */
public class MarkdownUtils {

    public static String fileName= "";

    public static void setData(Context context, String data){
        fileName = data;
        startActivitys(context);
    }

    public static String getData(Context context) throws Exception {
//        全文无法解析为MarkDown，显示的是MarkDown源文件字符串
//        解决方案：在读取的源字符串的每一行末尾加上一个‘\n’
// eg:

        InputStream is = context.getAssets().open(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder sb=new StringBuilder();
        //BufferedReader br=new BufferedReader(new FileReader(filename));
        String line;
        while ((line=br.readLine())!=null){
            sb.append(line).append("\\n");//注意这一行，通常应该是sb.append(line);
        }

        //result为从MarkDown源文件中读取出的字符串
//        部分解析异常，排版部分错乱
//                这个问题是由于源文件中存在特殊字符导致javascript库解析异常
//        解决方案：直接上代码
        String ss = sb.toString();
//        ss=ss.replace("\"","\\"+"\"");
//        ss=ss.replace("\'","\\"+"\'");
//        ss=ss.replace("//","\\/\\/");
        return ss;
    }

    public static void startActivitys(Context context){
        context.startActivity(new Intent(context, MarkdownWebviewActivity.class));
    }
}
