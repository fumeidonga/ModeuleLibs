package com.rebeau.share;

import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 * 自定义分享list
 */

public class InviteDataEntity {

    /**
     * 分享的途径，
     */
    private List<ModuleShareEntity> share_list;

    /**
     * 分享的二维码
     */
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

    public void setShare_list(List<ModuleShareEntity> share_list) {
        this.share_list = share_list;
    }

    public List<ModuleShareEntity> getShare_list() {
        return share_list;
    }

    public void setShareList(List<ModuleShareEntity> share_list) {
        this.share_list = share_list;
    }

    public String getInvite_code() {
        return invite_code;
    }

    public void setInvite_code(String invite_code) {
        this.invite_code = invite_code;
    }

    @Override
    public String toString() {
        return "InviteDataEntity{" +
                "share_list=" + share_list +
                ", invite_code='" + invite_code + '\'' +
                '}';
    }
}