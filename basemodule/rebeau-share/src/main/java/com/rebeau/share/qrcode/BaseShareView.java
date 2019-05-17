package com.rebeau.share.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by haowang on 17/8/30.
 */

public abstract class BaseShareView {

    protected final String APP_NAME = "免费小说";
    public static final String REDPAGEDATA = "RED_PAGER_DATA";
    //二维码图片地址
    public static final String QRCODE_URL = "QRCODE_URL";
    //邀请码
    public static final String INVITE_CODE = "INVITE_CODE";

    /**
     * 微信分享图片id
     */
    public int shareResourceId;
    /**
     * 微信分享图片下载地址
     */
    public String shareResourdePath;

    // 分享图片保存路径

    protected Context mContext;
    private Bundle mData;

    public BaseShareView(Context context, Bundle bundle) {
        mContext = context;
        mData = bundle;
    }

    public BaseShareView(Context mContext, Bundle mDataint, int shareResourceId, String shareResourdePath) {
        this.shareResourceId = shareResourceId;
        this.shareResourdePath = shareResourdePath;
        this.mContext = mContext;
        this.mData = mData;
    }

    /**
     * 创建分享图片的保存文件
     * createShareBm
     * drawBitmap
     *
     * @return
     */
    public File createShareFile(String imagesavedir) {
        Bitmap bm = createShareBm();
        if (null == bm) {
            return null;
        }

        File dirFile = new File(imagesavedir);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        File saveFile = new File(imagesavedir + getSaveFileName());
        try {
            FileOutputStream fos = new FileOutputStream(saveFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            RBLogUtil.et("error");
        }

        return saveFile;

    }

    protected int getDimensionWidth(int markWidth, int bmWidth) {
        return (markWidth * 2) * bmWidth / getUIWidth();
    }

    protected int getDimensionHeight(int markHeight, int bmHeigth) {
        return (markHeight * 2) * bmHeigth / getUIHeight();
    }

    private Bitmap createShareBm() {
        /**
         * 背景图
         */
        Bitmap shareBm = null;
        if(!TextUtils.isEmpty(shareResourdePath)) {
            shareBm = BitmapFactory
                    .decodeFile(getDrawablePath())
                    .copy(Bitmap.Config.ARGB_8888, true);
        } else {
            shareBm = BitmapFactory
                    .decodeResource(mContext.getResources(), getDrawableId())
                    .copy(Bitmap.Config.ARGB_8888, true);
        }
        /**
         *
         */
        drawBitmap(shareBm, mData);

        return shareBm;
    }

    protected abstract String getSaveFileName();

    protected abstract int getDrawableId();

    protected abstract String getDrawablePath();

    protected abstract void drawBitmap(Bitmap bm, Bundle bundle);

    protected abstract int getUIWidth();

    protected abstract int getUIHeight();


    public void drawText(Paint paint, Canvas canvas, String codeStr, int codeTextY, int width, int height){
        int index = codeStr.indexOf("_");
        String preString = codeStr.substring(0, index);
        Paint.FontMetrics codeFontMetrics = paint.getFontMetrics();
        Rect codeRect = new Rect();
        paint.getTextBounds(preString, 0, preString.length(), codeRect);
        int codeStrWidth = codeRect.width() + 10;
        int codeStrHeight = codeRect.height();
        //float codeTextX = (width - codeStrWidth) / 2;
        float codeTextX = width / 3 - 3;
        float codeTextTop = getDimensionHeight(codeTextY, height);
        float codeBaseline = codeTextTop
                + (codeStrHeight / 2)
                + (codeFontMetrics.bottom - codeFontMetrics.top) / 2
                - codeFontMetrics.bottom;
        canvas.drawText(preString, codeTextX, codeBaseline, paint);

        String endString = codeStr.substring(index+1);
        paint.setColor(Color.parseColor("#666666"));
        //Paint paint1 = new Paint();
        //Rect codeRect1 = new Rect();
        //paint1.getTextBounds(preString, 0, preString.length(), codeRect1);
        //codeTextX = (width - codeStrWidth - preString.length()) / 2;

        canvas.drawText(endString, codeTextX + codeStrWidth, codeBaseline, paint);
    }
}
