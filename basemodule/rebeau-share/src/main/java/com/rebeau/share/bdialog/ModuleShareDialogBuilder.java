package com.rebeau.share.bdialog;

import android.content.Context;
import android.support.v7.widget.OrientationHelper;

import java.util.List;

public class ModuleShareDialogBuilder {
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    public static final int VERTICAL = OrientationHelper.VERTICAL;

    public static final int LINEAR = 0;
    public static final int GRID = 1;

    private ModuleShareCustomDialog customDialog;

    /**
     *
     * @param context
     * @param layoutNumber 每一行的个数 ， 默认是5个
     */
    public ModuleShareDialogBuilder(Context context, int layoutNumber) {
        customDialog = new ModuleShareCustomDialog(context, layoutNumber);
    }

    public ModuleShareDialogBuilder title(String title) {
        customDialog.title(title);
        return this;
    }

    public ModuleShareDialogBuilder title(int title) {
        customDialog.title(title);
        return this;
    }

    public ModuleShareDialogBuilder background(int res) {
        customDialog.background(res);
        return this;
    }

    public ModuleShareDialogBuilder inflateMenu(int menu, ModuleShareItemClickListener moduleShareItemClickListener) {
        customDialog.inflateMenu(menu, moduleShareItemClickListener);
        return this;
    }

    public ModuleShareDialogBuilder inflateMenu(int menu, String showId, ModuleShareItemClickListener moduleShareItemClickListener) {
        if(showId == null || "".equals(showId)) {
            customDialog.inflateMenu(menu, moduleShareItemClickListener);
        } else {
            customDialog.inflateMenu(menu, showId, moduleShareItemClickListener);
        }
        return this;
    }

    public ModuleShareDialogBuilder layout(int layout) {
        customDialog.layout(layout);
        return this;
    }

    public ModuleShareDialogBuilder orientation(int orientation) {
        customDialog.orientation(orientation);
        return this;
    }

    public ModuleShareDialogBuilder addItems(List<ModuleShareItem> moduleShareItems, ModuleShareItemClickListener moduleShareItemClickListener) {
        customDialog.addItems(moduleShareItems, moduleShareItemClickListener);
        return this;
    }

    /**
     * @deprecated
     */
    public ModuleShareDialogBuilder itemClick(ModuleShareItemClickListener listener) {
        customDialog.setItemClick(listener);
        return this;
    }

    public void show() {
        customDialog.show();
    }

}
