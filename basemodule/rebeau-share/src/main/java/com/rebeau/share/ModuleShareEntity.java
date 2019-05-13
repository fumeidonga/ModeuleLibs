package com.rebeau.share;

/**
 * 分享的实体类
 */
public class ModuleShareEntity<T> {

    /**
     ModuleShareEntity entity = ModuleShareEntity.with()
            .type("")
            .title("")
            .share_type(0)
            .img_url("")
            .thumbimage("")
            .desc("")
            .link("")
            .callback("")
            .show_id("0,1,2,3,4")
            .qrcode_url("")
            .showTitle("")
            .build();
     */

    /**
     * 分享途径，比如qq，weixin
     * case 0: // 微信好友
     * case 1: // 微信朋友圈
     * case 2: // 二维码
     * case 3: // QQ好友
     * case 4: // QQ空间
     */
    private int share_type;

    /**
     * 分享途径需要显示的分享按钮，0,1,2,3,4
     * case 0: // 微信好友
     * case 1: // 微信朋友圈
     * case 2: // 二维码
     * case 3: // QQ好友
     * case 4: // QQ空间
     */
    private String show_id;
    /**
     * media type
     * 分享类型
     * 文本，图片，链接等
     * @see ModuleShareStyle
     */
    private String type;
    /**
     * 分享标题
     */
    private String title;
    /**
     * 分享面板的标题
     */
    private String share_title;
    /**
     * 分享回调
     */
    private String callback;
    /**
     * 分享本地资源文件 R.drawable.icon
     */
    private int localimage;
    /**
     * img_url ,图片路径
     * 本地图 or http
     */
    private String img_url;
    /**
     * 缩略图
     */
    private String thumbimage;
    /**
     * 分享内容
     */
    private String desc;
    /**
     *
     */
    private String musicUrl;
    /**
     *
     */
    private String videoUrl;
    /**
     *
     */
    private String musicTargetUrl;
    /**
     * 分享链接
     */
    private String link;
    /**
     * file
     */
    private String filePath;
    /**
     * 备用字段
     */
    private String ext;
    private String ext1;
    private T data;
    /**
     * 生成二维码的图片地址
     */
    private String qrcode_url;

    private String invite_code;

    /**
     * 任务id 1 每日分享 2邀请好友分享
     * 从哪个地方调用分享的，用于回调
     */
    private int share_from;

    public int getShare_from() {
        return share_from;
    }

    public void setShare_from(int share_from) {
        this.share_from = share_from;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getLocalimage() {
        return localimage;
    }

    public String getCallback() {
        return callback;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getThumbimage() {
        return thumbimage;
    }

    public String getDesc() {
        return desc;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getMusicTargetUrl() {
        return musicTargetUrl;
    }

    public String getLink() {
        return link;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getExt() {
        return ext;
    }

    public String getExt1() {
        return ext1;
    }

    public String getQrcode_url() {
        return qrcode_url;
    }

    public int getShare_type() {
        return share_type;
    }

    public String getShow_id() {
        return show_id;
    }

    public T getData() {
        return data;
    }

    public static  Builder with(){
        return new Builder();
    }

    public void setShow_id(String show_id) {
        this.show_id = show_id;
    }

    public String getShare_title() {
        return share_title;
    }

    public void setShare_type(int share_type) {
        this.share_type = share_type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setShare_title(String share_title) {
        this.share_title = share_title;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public void setLocalimage(int localimage) {
        this.localimage = localimage;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public void setThumbimage(String thumbimage) {
        this.thumbimage = thumbimage;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setMusicTargetUrl(String musicTargetUrl) {
        this.musicTargetUrl = musicTargetUrl;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setQrcode_url(String qrcode_url) {
        this.qrcode_url = qrcode_url;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    @Override
    public String toString() {
        return "ModuleShareEntity{" +
                "share_type=" + share_type +
                ", show_id='" + show_id + '\'' +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", share_title='" + share_title + '\'' +
                ", callback='" + callback + '\'' +
                ", localimage=" + localimage +
                ", img_url='" + img_url + '\'' +
                ", thumbimage='" + thumbimage + '\'' +
                ", desc='" + desc + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", musicTargetUrl='" + musicTargetUrl + '\'' +
                ", link='" + link + '\'' +
                ", filePath='" + filePath + '\'' +
                ", ext='" + ext + '\'' +
                ", ext1='" + ext1 + '\'' +
                ", data=" + data +
                ", qrcode_url='" + qrcode_url + '\'' +
                '}';
    }

    public static class Builder<T> {

        private String type;
        private String title;
        private int localimage;
        private String imageurl;
        private String thumbimage;
        private String description;
        private String musicUrl;
        private String videoUrl;
        private String musicTargetUrl;
        private String shareurl;
        private String filePath;
        private String ext;
        private String ext1;
        private String qrcodeUrl;
        private String showTitle;
        private String showid;
        private int share_type;
        private String callback;
        private String invite_code;
        private T data;
        private int share_from;

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder localimage(int localimage) {
            this.localimage = localimage;
            return this;
        }

        public Builder imageurl(String imageurl) {
            this.imageurl = imageurl;
            return this;
        }

        public Builder thumbimage(String thumbimage) {
            this.thumbimage = thumbimage;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder musicUrl(String musicUrl) {
            this.musicUrl = musicUrl;
            return this;
        }

        public Builder videoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
            return this;
        }

        public Builder musicTargetUrl(String musicTargetUrl) {
            this.musicTargetUrl = musicTargetUrl;
            return this;
        }

        public Builder callback(String callback) {
            this.callback = callback;
            return this;
        }
        public Builder shareurl(String shareurl) {
            this.shareurl = shareurl;
            return this;
        }

        public Builder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public Builder ext(String ext) {
            this.ext = ext;
            return this;
        }

        public Builder ext1(String ext1) {
            this.ext1 = ext1;
            return this;
        }

        public Builder qrcodeUrl(String qrcodeUrl) {
            this.qrcodeUrl = qrcodeUrl;
            return this;
        }

        public Builder showTitle(String showTitle) {
            this.showTitle = showTitle;
            return this;
        }

        public Builder shareType(int share_type) {
            this.share_type = share_type;
            return this;
        }

        public Builder showid(String showid) {
            this.showid = showid;
            return this;
        }
        public Builder share_from(int share_from) {
            this.share_from = share_from;
            return this;
        }
        public Builder invite_code(String invite_code) {
            this.invite_code = invite_code;
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }
        public ModuleShareEntity build() {
            ModuleShareEntity shareEntity = new ModuleShareEntity();
            shareEntity.type = type;
            shareEntity.title = title;
            shareEntity.localimage = localimage;
            shareEntity.img_url = imageurl;
            shareEntity.thumbimage = thumbimage;
            shareEntity.desc = description;
            shareEntity.musicUrl = musicUrl;
            shareEntity.videoUrl = videoUrl;
            shareEntity.musicTargetUrl = musicTargetUrl;
            shareEntity.link = shareurl;
            shareEntity.filePath = filePath;
            shareEntity.ext = ext;
            shareEntity.ext1 = ext1;
            shareEntity.share_title = showTitle;
            shareEntity.qrcode_url = qrcodeUrl;
            shareEntity.share_type = share_type;
            shareEntity.show_id = showid;
            shareEntity.data = data;
            shareEntity.share_from = share_from;
            shareEntity.invite_code = invite_code;
            shareEntity.callback = callback;
            return shareEntity;
        }
    }
}
