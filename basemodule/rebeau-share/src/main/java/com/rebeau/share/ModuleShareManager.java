package com.rebeau.share;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.android.moduleshare.bdialog.ModuleShareDialogBuilder;
import com.android.moduleshare.bdialog.ModuleShareItem;
import com.android.moduleshare.bdialog.ModuleShareItemClickListener;
import com.android.moduleshare.qrcode.BaseShareView;
import com.android.moduleshare.qrcode.QRCodeShareView;
import com.android.moduleshare.utils.DVLogUtils;
import com.android.moduleshare.utils.DVStringUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 *
 * QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加：
 @Override
 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 super.onActivityResult(requestCode, resultCode, data);
 UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
 }
 注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
 */
public class ModuleShareManager {

    public static final int HORIZONTAL_SINGLE = 0;
    public static final int HORIZONTAL_MULTI = 1;
    public static final int VERTICAL = 2;
    public static final int GRID = 3;
    public static final String SD_PATH = Environment.getExternalStorageDirectory().getPath();
    public String DEFAULT_SAVEPATH = "/WtzwReader/image/";

    private Activity activity;
    /**
     * 邀请码
     */
    private String invite_code;

    /**
     * 微信分享图片id
     */
    private int shareResourceId;
    /**
     * 微信分享图片下载地址
     */
    private String shareResourdePath;

    //使用volatile关键字防止重排序，因为 new Instance()是一个非原子操作，可能创建一个不完整的实例
    private static volatile ModuleShareManager singleton3;

    private ModuleShareManager() {
    }

    public static ModuleShareManager getSingleton() {
        // Double-Check idiom
        if (singleton3 == null) {
            synchronized (ModuleShareManager.class) {       // 1
                // 只需在第一次创建实例时才同步
                if (singleton3 == null) {       // 2
                    singleton3 = new ModuleShareManager();      // 3
                }
            }
        }
        return singleton3;
    }

    /**
     * 针对每一种的分享不同的处理
     * @param activity
     * @param dataBean 需要分享的对象，每一个的点击都可能不一样
     */
    public void showHorizontalDialog(final Activity activity, final InviteDataEntity dataBean, final IShareCallBack iShareCallBack){
        if(dataBean == null) {
            return;
        }
        List<ModuleShareItem> moduleShareItems = new ArrayList<>();
        invite_code = dataBean.getInvite_code();
        for (ModuleShareEntity shareEntity : dataBean.getShare_list()){
            moduleShareItems.add(new ModuleShareItem(shareEntity.getShare_type(), shareEntity.getShare_title().toString(), getDrawable(activity, shareEntity.getShare_type())));
        }

        /*new ModuleShareDialogBuilder(activity, 0)
                .orientation(ModuleShareDialogBuilder.HORIZONTAL)
                .addItems(moduleShareItems, listener)
                .show();*/

        new ModuleShareDialogBuilder(activity, 0)
                .title(R.string.module_share_share_title)
                .layout(ModuleShareDialogBuilder.GRID)
                .orientation(ModuleShareDialogBuilder.VERTICAL)
                .addItems(moduleShareItems, new ModuleShareItemClickListener() {
                    @Override
                    public void click(ModuleShareItem moduleShareItem, int position) {
                        DVLogUtils.d(position);
                        //Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (moduleShareItem.getId()){
                            case 0: // 微信好友
                                postShare(activity, 0, dataBean.getShare_list().get(position), iShareCallBack);
                                break;
                            case 1: // 微信朋友圈
                                postShare(activity, 1, dataBean.getShare_list().get(position), iShareCallBack);
                                break;
                            case 2: // 二维码
                                if(iShareCallBack != null) {
                                    iShareCallBack.shareOther(dataBean.getShare_list().get(position));
                                }
                                break;
                            case 3: // QQ好友
                                postShare(activity, 3, dataBean.getShare_list().get(position), iShareCallBack);
                                break;
                            case 4: // QQ空间
                                postShare(activity, 4, dataBean.getShare_list().get(position), iShareCallBack);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    private void postShare(Activity activity, int pos, String inviteCode, String qrcodeUrl, IShareCallBack callback){
        this.activity = activity;
        File file = getQrCodeShareFile(activity, qrcodeUrl, inviteCode);
        if (null == file || !file.exists()) {
            return;
        }
        UMImage image = new UMImage(activity, file);
        image.setThumb(new UMImage(activity, file));
        new ShareAction(activity)
                .setPlatform(getShareMedia(pos))
                .withMedia(image)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    public Drawable getDrawable(Activity mContext, int type) {
        switch (type) {
            case 0: // 微信好友
                return mContext.getResources().getDrawable(R.drawable.module_share_ic_invite_share_wechat);
            case 1: // 微信朋友圈
                return mContext.getResources().getDrawable(R.drawable.module_share_ic_invite_share_moments);
            case 2: // 二维码
                return mContext.getResources().getDrawable(R.drawable.module_share_ic_invite_share_qrcode);
            case 3: // QQ好友
                return mContext.getResources().getDrawable(R.drawable.module_share_ic_invite_share_qq);
            case 4: // QQ空间
                return mContext.getResources().getDrawable(R.drawable.module_share_ic_invite_share_qzone);
            default:
                return mContext.getResources().getDrawable(R.drawable.module_share_default_icon);
        }
    }
    public SHARE_MEDIA getShareMedia(int type) {
        switch (type) {
            case 0: // 微信好友home
                return SHARE_MEDIA.WEIXIN;
            case 1: // 微信朋友圈
                return SHARE_MEDIA.WEIXIN_CIRCLE;
            case 3: // QQ好友
                return SHARE_MEDIA.QQ;
            case 4: // QQ空间
                return SHARE_MEDIA.QZONE;
            case 2: // 二维码
            default:
                return null;
        }
    }

    /**
     * 分享的内容相同, 直接分享的
     * @param activity
     * @param iShareCallBack
     */
    public void showHorizontalDialog(final Activity activity, final ModuleShareEntity shareEntity, final IShareCallBack iShareCallBack){

        this.activity = activity;
        new ModuleShareDialogBuilder(activity, 0)
                .title(R.string.module_share_share_title)
                .layout(ModuleShareDialogBuilder.GRID)
                .orientation(ModuleShareDialogBuilder.VERTICAL)
                .inflateMenu(R.menu.module_share_menu_share, shareEntity.getShow_id(), new ModuleShareItemClickListener() {
                    @Override
                    public void click(ModuleShareItem moduleShareItem, int position) {
                        Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (moduleShareItem.getTitle()){
                            case "微信好友":
                                postShare(activity, 0, shareEntity, iShareCallBack);
                                break;
                            case "朋友圈":
                                postShare(activity, 1, shareEntity, iShareCallBack);
                                break;
                            case "面对面":
                                iShareCallBack.shareOther(shareEntity);
                                break;
                            case "QQ":
                                postShare(activity, 3, shareEntity, iShareCallBack);
                                break;
                            case "QQ空间":
                                postShare(activity, 4, shareEntity, iShareCallBack);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * 分享的内容相同, 直接分享的
     * @param activity
     * @param listener
     */
    public void showHorizontalDialog(final Activity activity, ModuleShareItemClickListener listener){
        showHorizontalDialog(activity, "", listener);
    }

    /**
     * 分享的内容相同, 直接分享的
     * @param activity
     * @param listener
     */
    public void showHorizontalDialog(final Activity activity, String showId, ModuleShareItemClickListener listener){

        this.activity = activity;
        /*new ModuleShareDialogBuilder(activity, 0)
                .title(R.string.module_share_share_title)
                .orientation(ModuleShareDialogBuilder.HORIZONTAL)
                .inflateMenu(R.menu.module_share_menu_share, listener)
                .show();*/
        new ModuleShareDialogBuilder(activity, 0)
                .title(R.string.module_share_share_title)
                .layout(ModuleShareDialogBuilder.GRID)
                .orientation(ModuleShareDialogBuilder.VERTICAL)
                .inflateMenu(R.menu.module_share_menu_share, showId, listener)
                .show();
    }

    /**
     * just for test
     * @param activity
     * @param shareEntity
     */
    private void showHorizontalDialog(final Activity activity, final ModuleShareEntity shareEntity){

        new ModuleShareDialogBuilder(activity, 0)
                .title(R.string.module_share_share_title)
                .orientation(ModuleShareDialogBuilder.HORIZONTAL)
                .inflateMenu(R.menu.module_share_menu_share, new ModuleShareItemClickListener() {
                    @Override
                    public void click(ModuleShareItem moduleShareItem, int position) {
                        Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                        switch (moduleShareItem.getTitle()){
                            case "微信好友":
                                break;
                            case "朋友圈":
                                break;
                            case "面对面":
                                break;
                            case "QQ":
                                break;
                            case "QQ空间":
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     *
     * @param activity
     * @param shareEntity
     */
    public void showCustomDialog(final Activity activity, ModuleShareEntity shareEntity){
        showCustomDialog(GRID, activity, shareEntity, 3);
    }

    /**
     *
     * @param type 需要显示的方式，横的，竖的，网格
     * @param activity
     * @param shareEntity
     */
    public void showCustomDialog(int type, final Activity activity, ModuleShareEntity shareEntity){
        showCustomDialog(type, activity, shareEntity, 0);
    }

    /**
     *
     * @param type 需要显示的方式，横的，竖的，网格
     * @param activity
     * @param shareEntity
     * @param layoutNumber 每一行的个数
     */
    private void showCustomDialog(int type, final Activity activity, ModuleShareEntity shareEntity, int layoutNumber){

        switch (type){
            case HORIZONTAL_SINGLE:

                new ModuleShareDialogBuilder(activity, layoutNumber)
                        .title(R.string.module_share_share_title)
                        .orientation(ModuleShareDialogBuilder.HORIZONTAL)
                        .inflateMenu(R.menu.module_share_menu_share, new ModuleShareItemClickListener() {
                            @Override
                            public void click(ModuleShareItem moduleShareItem, int position) {
                                Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                                switch (moduleShareItem.getId()){
                                    case 0: // 微信好友
                                        break;
                                    case 1: // 微信朋友圈
                                        break;
                                    case 2: // 二维码
                                        break;
                                    case 3: // QQ好友
                                        break;
                                    case 4: // QQ空间
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
                break;
            case HORIZONTAL_MULTI:
                new ModuleShareDialogBuilder(activity, layoutNumber)
                        .title(R.string.module_share_share_title)
                        .orientation(ModuleShareDialogBuilder.HORIZONTAL)
                        .inflateMenu(R.menu.module_share_menu_share, new ModuleShareItemClickListener() {
                            @Override
                            public void click(ModuleShareItem moduleShareItem, int position) {
                                Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .inflateMenu(R.menu.module_share_menu_main, new ModuleShareItemClickListener() {
                            @Override
                            public void click(ModuleShareItem moduleShareItem, int position) {
                                Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case VERTICAL:
                new ModuleShareDialogBuilder(activity, layoutNumber)
                        .title(R.string.module_share_title_item)
                        .orientation(ModuleShareDialogBuilder.VERTICAL)
                        .inflateMenu(R.menu.module_share_menu_share, new ModuleShareItemClickListener() {
                            @Override
                            public void click(ModuleShareItem moduleShareItem, int position) {
                                Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
            case GRID:
            default:
                new ModuleShareDialogBuilder(activity, layoutNumber)
                        .title(R.string.module_share_share_title)
                        .layout(ModuleShareDialogBuilder.GRID)
                        .orientation(ModuleShareDialogBuilder.VERTICAL)
                        .inflateMenu(R.menu.module_share_menu_grid, new ModuleShareItemClickListener() {
                            @Override
                            public void click(ModuleShareItem moduleShareItem, int position) {
                                Toast.makeText(activity, moduleShareItem.getTitle(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
        }
    }

    public void shareDefineViewGroup(Activity activity, InviteDataEntity inviteDataEntity, IShareCallBack iShareCallBack) {

    }

    public void postShare(Activity activity, ModuleShareEntity shareEntity, IShareCallBack callback){
        if(shareEntity.getType().equals(ModuleShareStyle.IMAGELOCAL)) {
            postShare(activity, shareEntity.getShare_type(), shareEntity.getInvite_code(), shareEntity.getQrcode_url(), callback);
        } else {
            postShare(activity, getShareMedia(shareEntity.getShare_type()), shareEntity, callback);
        }
    }

    public void postShare(Activity activity, int pos, ModuleShareEntity shareEntity, IShareCallBack iShareCallBack){
        //postShare(activity, getShareMedia(pos), shareEntity, iShareCallBack);

        if(shareEntity.getType().equals(ModuleShareStyle.IMAGELOCAL)) {
            postShare(activity, pos, shareEntity.getInvite_code() != null ? shareEntity.getInvite_code() : invite_code, shareEntity.getQrcode_url(), iShareCallBack);
        } else {
            postShare(activity, getShareMedia(pos), shareEntity, iShareCallBack);
        }
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    public void postShare(Activity activity, SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack iShareCallBack) {
        this.activity = activity;
        switch (shareEntity.getType()) {
            case ModuleShareStyle.TEXT:
                shareText(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.IMAGELOCAL:
                shareImageLocal(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.IMAGEURL:
                shareImageNet(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.TEXT_AND_IMAGE:
                shareTextAndImage(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.MUSIC:
                shareMusic(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.VIDEO:
                shareVideo(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.LINK:
                shareUrl(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.FILE:
                shareFile(share_media, shareEntity, iShareCallBack);
                break;
            case ModuleShareStyle.MINAPP:
                break;
            case ModuleShareStyle.EMOJI:
                break;
            case ModuleShareStyle.LINK_NO_DISCRIPT:
                break;
            default:
                break;
        }
    }

//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    /**
     * UMNEG的分享对话框
     *
     * @param activity
     * @param shareEntity
     * @param callback
     */
    public void shareUmeng(Activity activity, ModuleShareEntity shareEntity, IShareCallBack callback) {

        this.activity = activity;
        ShareAction shareAction = new ShareAction(activity)
                .setDisplayList(ModuleShareStyle.defaultArray2);
        switch (shareEntity.getType()) {
            case ModuleShareStyle.TEXT:
                shareAction.withText(shareEntity.getDesc());
                break;
            case ModuleShareStyle.IMAGELOCAL:
                shareAction.withMedia(getUMImage(activity, shareEntity));
                break;
            case ModuleShareStyle.IMAGEURL:
                shareAction.withMedia(getUMImage(activity, shareEntity));
                break;
            case ModuleShareStyle.TEXT_AND_IMAGE:
                shareAction.withText(shareEntity.getDesc())
                        .withMedia(getUMImage(activity, shareEntity));
                break;
            case ModuleShareStyle.MUSIC:
                UMusic music = new UMusic(shareEntity.getMusicUrl());
                music.setTitle(shareEntity.getTitle());
                if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
                    music.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
                } else {
                    music.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
                }
                music.setDescription(shareEntity.getDesc());
                music.setmTargetUrl(shareEntity.getMusicTargetUrl());
                shareAction.withMedia(music);
                break;
            case ModuleShareStyle.VIDEO:
                UMVideo video = new UMVideo(shareEntity.getMusicUrl());
                video.setTitle(shareEntity.getTitle());
                if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
                    video.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
                } else {
                    video.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
                }
                video.setDescription(shareEntity.getDesc());
                shareAction.withMedia(video);
                break;
            case ModuleShareStyle.LINK:
                UMWeb web = new UMWeb(shareEntity.getLink());
                web.setTitle(shareEntity.getTitle());
                if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
                    web.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
                } else {
                    web.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
                }
                web.setDescription(shareEntity.getDesc());
                shareAction.withMedia(web);
                break;
            case ModuleShareStyle.FILE:
                shareAction.withFile(new File(shareEntity.getFilePath()))
                        .withText(shareEntity.getDesc())
                        .withSubject(shareEntity.getTitle());
                break;
            case ModuleShareStyle.MINAPP:
                break;
            case ModuleShareStyle.EMOJI:
                break;
            case ModuleShareStyle.LINK_NO_DISCRIPT:
                break;
            default:
                break;
        }

        shareAction.setCallback(new CustomShareListener(activity, callback))
                .open();
    }
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    private UMImage getUMImage(Activity activity, ModuleShareEntity shareEntity) {

        switch (shareEntity.getType()) {
            case ModuleShareStyle.IMAGELOCAL:
                UMImage umImage = new UMImage(activity, shareEntity.getLocalimage());
                if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
                    umImage.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
                } else {
                    umImage.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
                }
                return umImage;
            case ModuleShareStyle.IMAGEURL:
                UMImage urlImage = new UMImage(activity, shareEntity.getImg_url());
                if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
                    urlImage.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
                } else {
                    urlImage.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
                }
                return urlImage;
            case ModuleShareStyle.TEXT_AND_IMAGE:
                UMImage textImage = new UMImage(activity, shareEntity.getImg_url());
                if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
                    textImage.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
                } else {
                    textImage.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
                }
                return textImage;
            default:
                return null;
        }
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareText(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {
        new ShareAction(activity).withText(shareEntity.getDesc())
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareImageLocal(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {
        UMImage imagelocal = getUMImage(activity, shareEntity);
        new ShareAction(activity).withMedia(imagelocal)
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareImageNet(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {
        UMImage imagelocal = getUMImage(activity, shareEntity);
        new ShareAction(activity).withMedia(imagelocal)
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareUrl(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {

        UMWeb web = new UMWeb(shareEntity.getLink());
        web.setTitle(shareEntity.getTitle());
        if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
            web.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
        } else {
            web.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
        }
        web.setDescription(shareEntity.getDesc());
        new ShareAction(activity).withMedia(web)
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareMusic(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {

        UMusic music = new UMusic(shareEntity.getMusicUrl());
        music.setTitle(shareEntity.getTitle());
        if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
            music.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
        } else {
            music.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
        }
        music.setDescription(shareEntity.getDesc());
        music.setmTargetUrl(shareEntity.getMusicTargetUrl());

        new ShareAction(activity).withMedia(music)
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareVideo(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {

        UMVideo video = new UMVideo(shareEntity.getMusicUrl());
        video.setTitle(shareEntity.getTitle());
        if (DVStringUtils.isEmpty(shareEntity.getThumbimage())) {
            video.setThumb(new UMImage(activity, R.drawable.module_share_default_icon));
        } else {
            video.setThumb(new UMImage(activity, shareEntity.getThumbimage()));
        }
        video.setDescription(shareEntity.getDesc());

        new ShareAction(activity).withMedia(video)
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareTextAndImage(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {
        UMImage imagelocal = getUMImage(activity, shareEntity);
        new ShareAction(activity).withText(shareEntity.getDesc())
                .withMedia(imagelocal)
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }

    /**
     * @param share_media
     * @param shareEntity
     */
    private void shareFile(SHARE_MEDIA share_media, ModuleShareEntity shareEntity, IShareCallBack callback) {
        File file = new File(shareEntity.getFilePath());

        new ShareAction(activity)
                .withFile(file)
                .withText(shareEntity.getDesc())
                .withSubject(shareEntity.getTitle())
                .setPlatform(share_media)
                .setCallback(new CustomShareListener(activity, callback)).share();
    }


    /**
     * 分享监听接口
     */
    private class CustomShareListener implements UMShareListener {

        private WeakReference<Context> mActivity;

        private IShareCallBack callback;

        private CustomShareListener(Context activity, IShareCallBack callBack) {
            mActivity = new WeakReference(activity);
            this.callback = callBack;
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

            if (callback != null) {
                DVLogUtils.d();
                callback.onStart(platform.toString());
            } else {
                DVLogUtils.d("call back is null");
            }
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (callback != null) {
                DVLogUtils.d();
                callback.onResult(platform.toString());
            } else {
                DVLogUtils.d("call back is null");
            }
            if (platform.name().equals("WEIXIN_FAVORITE")) {
                //收藏成功啦
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            if (callback != null) {
                DVLogUtils.d();
                callback.onError(platform.toString(), t);
            } else {
                DVLogUtils.d("call back is null");
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            if (callback != null) {
                DVLogUtils.d();
                callback.onCancel(platform.toString());
            } else {
                DVLogUtils.d("call back is null");
            }
        }
    }


    /**
     * 分享回调
     */
    public interface IShareCallBack {

        void onStart(String shareType);

        void onResult(String shareType);

        void onError(String shareType, Throwable throwable);

        void onCancel(String shareType);

        void shareOther(ModuleShareEntity shareEntity);

    }

    public int getShareResourceId() {
        return shareResourceId;
    }

    public void setShareResourceId(int shareResourceId) {
        this.shareResourceId = shareResourceId;
    }

    public String getShareResourdePath() {
        return shareResourdePath;
    }

    public void setShareResourdePath(String shareResourdePath) {
        this.shareResourdePath = shareResourdePath;
    }

    public String getDefaultSavepath() {
        return DEFAULT_SAVEPATH;
    }

    public void setDefaultSavepath(String defaultSavepath) {
        DEFAULT_SAVEPATH = defaultSavepath;
    }

    /**
     * 获取二维码分享图片文件
     * @param url
     * @return
     */
    public File getQrCodeShareFile(Context context, String url, String mInviteCode) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseShareView.QRCODE_URL, url);
        bundle.putString(BaseShareView.INVITE_CODE, mInviteCode);
        BaseShareView qrCodeShareView = new QRCodeShareView(context, bundle, shareResourceId, shareResourdePath);
        File fileDir = new File(SD_PATH + DEFAULT_SAVEPATH);
        if(!fileDir.exists()){
            fileDir.mkdirs();
        }

        File file = qrCodeShareView.createShareFile(SD_PATH + DEFAULT_SAVEPATH);
        return file;
    }

}
