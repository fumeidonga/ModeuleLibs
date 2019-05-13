package com.rebeau.share.bdialog;

import android.graphics.drawable.Drawable;

public class ModuleShareItem {
    private int id;
    private int position;
    private String title;
    private Drawable icon;

    public ModuleShareItem() {
    }

    public ModuleShareItem(int id, int position, String title, Drawable icon) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.icon = icon;
    }

    public ModuleShareItem(int id, String title, Drawable icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
