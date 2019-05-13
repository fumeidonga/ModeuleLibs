package com.rebeau.share.qrcode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;


/**
 * Created by haowang on 17/8/29.
 */

public class QRCodeShareView extends BaseShareView {

    private final String IMG_SAVE_FILE_NAME = "qrcode_share.jpeg";

    public QRCodeShareView(Context context, Bundle bundle) {
        super(context, bundle);
    }

    public QRCodeShareView(Context context, Bundle bundle, int shareResourceId, String shareResourdePath) {
        super(context, bundle);
        this.shareResourceId = shareResourceId;
        this.shareResourdePath = shareResourdePath;
    }

    @Override
    protected String getSaveFileName() {
        return IMG_SAVE_FILE_NAME;
    }

    @Override
    protected int getDrawableId() {
        //return R.drawable.share_img_cdk;
        return shareResourceId;
    }

    @Override
    protected String getDrawablePath() {
        return shareResourdePath;
    }

    @Override
    protected void drawBitmap(Bitmap bm, Bundle bundle) {
        if (null == bm || null == bundle) {
            return;
        }

        String url = bundle.getString(QRCODE_URL, "");
        String code = bundle.getString(INVITE_CODE, "");
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(code)) {
            return;
        }

        int width = bm.getWidth();
        int height = bm.getHeight();

        Canvas canvas = new Canvas(bm);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int testsize = 32;
        int testHeight = 310;

        int qrwidth = 108;
        int qrheight = 455;
        if(TextUtils.isEmpty(shareResourdePath)) {

        } else {
            testsize = 22;
            testHeight = 310;
            qrwidth = 107;
            qrheight = 450;
        }

        // 绘制畅读卡
        paint.setColor(Color.parseColor("#FF4337"));
        paint.setTextSize(spToPx(mContext, testsize));
        Paint.FontMetrics codeFontMetrics = paint.getFontMetrics();
        Rect codeRect = new Rect();
        String codeStr = code;
        paint.getTextBounds(codeStr, 0, codeStr.length(), codeRect);
        int codeStrWidth = codeRect.width();
        int codeStrHeight = codeRect.height();
        float codeTextX = (width - codeStrWidth) / 2;
        float codeTextTop = getDimensionHeight(testHeight, height);
        float codeBaseline = codeTextTop
                + (codeStrHeight / 2)
                + (codeFontMetrics.bottom - codeFontMetrics.top) / 2
                - codeFontMetrics.bottom;
        canvas.drawText(codeStr, codeTextX, codeBaseline, paint);

        // 绘制二维码
        Bitmap qrCodeBm = null;
        /*Bitmap qrCodeBm = QRCodeUtil.createQRCodeBitmap(
                url,
                getDimensionWidth(147, width),
                getDimensionHeight(140, height), "0");
        if (null == qrCodeBm) {
            return;
        }*/
        canvas.drawBitmap(
                qrCodeBm,
                getDimensionWidth(qrwidth, width),
                getDimensionHeight(qrheight, height),
                null);

        // 保存画布
        canvas.save();
        canvas.restore();
    }

    @Override
    protected int getUIWidth() {
        return 720;
    }

    @Override
    protected int getUIHeight() {
        return 1280;
    }

    /**
     * sp转化为像素.
     */
    public static int spToPx(Context context, float sp) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }
}
