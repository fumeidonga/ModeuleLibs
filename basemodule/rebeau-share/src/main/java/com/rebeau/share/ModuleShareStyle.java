package com.rebeau.share;

import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangfei on 2018/1/23.
 */

public class ModuleShareStyle {

    public final static String SHARE_MEDIA_QQ = "QQ";
    public final static String SHARE_MEDIA_QZONE = "QZONE";
    public final static String SHARE_MEDIA_WEIXIN = "WEIXIN";
    public final static String SHARE_MEDIA_WEIXIN_CIRCLE = "WEIXIN_CIRCLE";

    /**
     * 纯文本
     */
    public final static String TEXT = "text";
    /**
     * 纯图片本地
     */
    public final static String IMAGELOCAL = "local_image";
    /**
     * 纯图片http
     */
    public final static String IMAGEURL = "http_image";
    /**
     * 图文
     */
    public final static String TEXT_AND_IMAGE = "text_and_image";
    /**
     * 链接（有标题，有内容）
     */
    public final static String LINK = "link";
    /**
     * 音乐（有标题，有内容）
     */
    public final static String MUSIC = "music";
    /**
     * 视频（有标题，有内容）
     */
    public final static String VIDEO = "video";
    /**
     * 链接（无标题，无内容）
     */
    public final static String LINK_NO_DISCRIPT = "link_no_discript";
    /**
     * 微信表情
     */
    public final static String EMOJI = "emoji";
    /**
     * 文件
     */
    public final static String FILE = "file";
    /**
     * 小程序（测试）
     */
    public final static String MINAPP = "minapp";

    /**
     * 微信，朋友圈， 微信收藏
     * sina，qq， qqzone
     * alipay, renren, douban
     * sms, email, youdaoyun
     * yingxiang, diandianchong, diandianchong
     * linkedin, yixin, yixincircle
     * ...
     */
    public static final List shareList = new ArrayList(Arrays.asList(
            SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
            SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
            SHARE_MEDIA.ALIPAY, SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,
            SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.YNOTE,
            SHARE_MEDIA.EVERNOTE, SHARE_MEDIA.LAIWANG, SHARE_MEDIA.LAIWANG_DYNAMIC,
            SHARE_MEDIA.LINKEDIN, SHARE_MEDIA.YIXIN, SHARE_MEDIA.YIXIN_CIRCLE,
            SHARE_MEDIA.TENCENT, SHARE_MEDIA.FACEBOOK, SHARE_MEDIA.TWITTER,
            SHARE_MEDIA.WHATSAPP, SHARE_MEDIA.GOOGLEPLUS, SHARE_MEDIA.LINE,
            SHARE_MEDIA.INSTAGRAM, SHARE_MEDIA.KAKAO, SHARE_MEDIA.PINTEREST,
            SHARE_MEDIA.POCKET, SHARE_MEDIA.TUMBLR, SHARE_MEDIA.FLICKR,
            SHARE_MEDIA.FOURSQUARE, SHARE_MEDIA.MORE));

    public static final SHARE_MEDIA[] defaultArray = {SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
    SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};

    public static final SHARE_MEDIA[] defaultArray2 = {SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL, SHARE_MEDIA.MORE};
    /**
     * 获取SHARE_MEDIA 所支持的分享类型
     *
     * @param share_media
     * @param styles
     */
    public static void initStyles(SHARE_MEDIA share_media, ArrayList<String> styles) {
        styles.clear();
        if (share_media == SHARE_MEDIA.QQ) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.QZONE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.SINA) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            //            styles.add(ModuleShareStyle.MUSIC11);
            //            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.WEIXIN) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
            styles.add(ModuleShareStyle.EMOJI);
            styles.add(ModuleShareStyle.MINAPP);
        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.WEIXIN_FAVORITE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);

        } else if (share_media == SHARE_MEDIA.TENCENT) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.DOUBAN) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.RENREN) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.ALIPAY) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.FACEBOOK) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.FACEBOOK_MESSAGER) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.TWITTER) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.EMAIL) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.SMS) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.YIXIN) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.YIXIN_CIRCLE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.LAIWANG) {

            //            styles.add(ModuleShareStyle.IMAGELOCAL);
            //            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.LAIWANG_DYNAMIC) {
            //
            //            styles.add(ModuleShareStyle.IMAGELOCAL);
            //            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.INSTAGRAM) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
        } else if (share_media == SHARE_MEDIA.PINTEREST) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
        } else if (share_media == SHARE_MEDIA.TUMBLR) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);

        } else if (share_media == SHARE_MEDIA.LINE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);

        } else if (share_media == SHARE_MEDIA.WHATSAPP) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.KAKAO) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.GOOGLEPLUS) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.EVERNOTE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.YNOTE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.FLICKR) {

            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);

        } else if (share_media == SHARE_MEDIA.LINKEDIN) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.POCKET) {
            styles.add(ModuleShareStyle.LINK_NO_DISCRIPT);
        } else if (share_media == SHARE_MEDIA.FOURSQUARE) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
        } else if (share_media == SHARE_MEDIA.MORE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.TEXT_AND_IMAGE);
        } else if (share_media == SHARE_MEDIA.DINGTALK) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.VKONTAKTE) {
            styles.add(ModuleShareStyle.TEXT);
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
            styles.add(ModuleShareStyle.LINK);
            styles.add(ModuleShareStyle.MUSIC);
            styles.add(ModuleShareStyle.VIDEO);
        } else if (share_media == SHARE_MEDIA.DROPBOX) {
            styles.add(ModuleShareStyle.IMAGELOCAL);
            styles.add(ModuleShareStyle.IMAGEURL);
        }

    }
}