package com.rebeau.share.bdialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.moduleshare.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by david on 2018/6/27.
 */

public class ModuleShareCustomDialog extends Dialog {
    private LinearLayout background;
    private LinearLayout container;
    private TextView titleView;

    private DialogAdapter adapter;
    private Context mContext;
    public View mViewShade;
    public View view;

    private int padding;
    private int topPadding;
    private int leftPadding;
    private int topIcon;
    private int leftIcon;

    private int orientation;
    private int layout;
    public int GRID_NUMBER = 3;

    public ModuleShareCustomDialog(Context context, int layoutNum) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mContext = context;
        if (layoutNum > 0) {
            GRID_NUMBER = layoutNum;
        }
        init();
    }

    private void init() {
        padding = getContext().getResources().getDimensionPixelSize(R.dimen.module_share_app_normal_margin);
        topPadding = getContext().getResources().getDimensionPixelSize(R.dimen.module_share_app_tiny_margin);
        leftPadding = getContext().getResources().getDimensionPixelSize(R.dimen.module_share_app_normal_margin);
        topIcon = getContext().getResources().getDimensionPixelSize(R.dimen.module_share_bottom_dialog_top_icon);
        leftIcon = getContext().getResources().getDimensionPixelSize(R.dimen.module_share_bottom_dialog_left_icon);

        Window window = getWindow();

        view = LayoutInflater.from(getContext()).inflate(R.layout.module_share_dialog_layout, null);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
//            window.setGravity(Gravity.BOTTOM);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x0000000));
            /*WindowManager.LayoutParams params = window.getAttributes();
            params.y = 0;
            window.setAttributes(params);*/
        mViewShade = view.findViewById(R.id.view_dialog_km_red_gift);

        background = (LinearLayout) findViewById(R.id.background);
        titleView = (TextView) findViewById(R.id.title);
        container = (LinearLayout) findViewById(R.id.container);
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void show() {
        super.show();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(400);
        mViewShade.startAnimation(alphaAnimation);
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.module_share_dialog_show);
        background.startAnimation(animation);
        //Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.km_util_permissinos_dialog_show_anim);
        //view.startAnimation(animation);
    }

    void addItems(List<ModuleShareItem> moduleShareItems, ModuleShareItemClickListener moduleShareItemClickListener) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RecyclerView.LayoutManager manager;

        adapter = new DialogAdapter(moduleShareItems, layout, orientation);
        adapter.setItemClick(moduleShareItemClickListener);

        if (layout == ModuleShareDialogBuilder.LINEAR)
            manager = new LinearLayoutManager(getContext(), orientation, false);
        else if (layout == ModuleShareDialogBuilder.GRID)
            manager = new GridLayoutManager(getContext(), GRID_NUMBER, orientation, false);
        else manager = new LinearLayoutManager(getContext(), orientation, false);

        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(params);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        container.addView(recyclerView);
    }

    public void title(int title) {
        title(getContext().getString(title));
    }

    public void title(String title) {
        titleView.setText(title);
        titleView.setVisibility(View.VISIBLE);
    }

    public void layout(int layout) {
        this.layout = layout;
        if (adapter != null) adapter.setLayout(layout);
    }

    public void orientation(int orientation) {
        this.orientation = orientation;
        if (adapter != null) adapter.setOrientation(orientation);
    }

    public void background(int res) {
        background.setBackgroundResource(res);
    }

    @SuppressLint("RestrictedApi")
    void inflateMenu(int menu, ModuleShareItemClickListener moduleShareItemClickListener) {
        MenuInflater menuInflater = new SupportMenuInflater(getContext());
        MenuBuilder menuBuilder = new MenuBuilder(getContext());
        menuInflater.inflate(menu, menuBuilder);
        List<ModuleShareItem> moduleShareItems = new ArrayList<>();
        for (int i = 0; i < menuBuilder.size(); i++) {
            MenuItem menuItem = menuBuilder.getItem(i);
            moduleShareItems.add(new ModuleShareItem(menuItem.getItemId(), menuItem.getTitle().toString(), menuItem.getIcon()));
        }
        addItems(moduleShareItems, moduleShareItemClickListener);
    }

    @SuppressLint("RestrictedApi")
    void inflateMenu(int menu, String showId, ModuleShareItemClickListener moduleShareItemClickListener) {
        MenuInflater menuInflater = new SupportMenuInflater(getContext());
        MenuBuilder menuBuilder = new MenuBuilder(getContext());
        menuInflater.inflate(menu, menuBuilder);
        List<ModuleShareItem> moduleShareItems = new ArrayList<>();

        List<String> list = getShowTitle(showId);

        for (int i = 0; i < menuBuilder.size(); i++) {
            MenuItem menuItem = menuBuilder.getItem(i);
            if(list.contains(menuItem.getTitle().toString())) {
                moduleShareItems.add(new ModuleShareItem(menuItem.getItemId(), menuItem.getTitle().toString(), menuItem.getIcon()));
            }
        }
        addItems(moduleShareItems, moduleShareItemClickListener);
    }

    private List<String> getShowTitle(String showid) {
        List<String> list = new ArrayList<>();
        String[] platformArray = showid.split(",");
        for (int i = 0; i < platformArray.length; i++) {
            String shareMedia = idToShareMedia(platformArray[i]);
            if (shareMedia != null) {
                list.add(shareMedia);
            }
        }
        return list;
    }

    /**
     * case 0: // 微信好友
     * case 1: // 微信朋友圈
     * case 2: // 二维码
     * case 3: // QQ好友
     * case 4: // QQ空间
     */

    private String idToShareMedia(String platformId) {
        String share_media = null;
        switch (platformId) {
            case "0":
                share_media = "微信好友";
                break;
            case "1":
                share_media = "微信朋友圈";
                break;
            case "2":
                share_media = "面对面邀请";
                break;
            case "3":
                share_media = "QQ";
                break;
            case "4":
                share_media = "QQ空间";
                break;
            default:
                break;
        }

        return share_media;
    }

    void setItemClick(ModuleShareItemClickListener moduleShareItemClickListener) {
        adapter.setItemClick(moduleShareItemClickListener);
    }

    /**
     * recycler view adapter, provide HORIZONTAL and VERTICAL item style
     */
    private class DialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<ModuleShareItem> mModuleShareItems = Collections.emptyList();
        private ModuleShareItemClickListener itemClickListener;

        private int orientation;
        private int layout;

        DialogAdapter(List<ModuleShareItem> mModuleShareItems, int layout, int orientation) {
            setList(mModuleShareItems);
            this.layout = layout;
            this.orientation = orientation;
        }

        private void setList(List<ModuleShareItem> moduleShareItems) {
            mModuleShareItems = moduleShareItems == null ? new ArrayList<ModuleShareItem>() : moduleShareItems;
        }

        void setItemClick(ModuleShareItemClickListener moduleShareItemClickListener) {
            this.itemClickListener = moduleShareItemClickListener;
        }

        public void setOrientation(int orientation) {
            this.orientation = orientation;
            notifyDataSetChanged();
        }

        public void setLayout(int layout) {
            this.layout = layout;
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (layout == ModuleShareDialogBuilder.GRID) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.module_share_layout_invite_way_item, null);
                return new InviteWayHolder(itemView);
            } else if (orientation == ModuleShareDialogBuilder.HORIZONTAL) {
                return new TopHolder(new LinearLayout(parent.getContext()));
            } else {
                return new LeftHolder(new LinearLayout(parent.getContext()));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ModuleShareItem moduleShareItem = mModuleShareItems.get(position);

            TopHolder topHolder;
            LeftHolder leftHolder;
            InviteWayHolder gridHolder;

            if (layout == ModuleShareDialogBuilder.GRID) {
                gridHolder = (InviteWayHolder) holder;

                gridHolder.textView.setText(moduleShareItem.getTitle());
                gridHolder.imageView.setImageDrawable(moduleShareItem.getIcon());
                gridHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        Log.d("hrl", "position = " + position);
                        if (itemClickListener != null) itemClickListener.click(moduleShareItem, position);
                    }
                });
            } else if (orientation == ModuleShareDialogBuilder.HORIZONTAL) {
                topHolder = (TopHolder) holder;

                topHolder.item.setText(moduleShareItem.getTitle());
                topHolder.item.setCompoundDrawablesWithIntrinsicBounds(null, topHolder.icon(moduleShareItem.getIcon()), null, null);
                topHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        Log.d("hrl", "position = " + position);
                        if (itemClickListener != null) itemClickListener.click(moduleShareItem, position);
                    }
                });
            } else {
                leftHolder = (LeftHolder) holder;

                leftHolder.item.setText(moduleShareItem.getTitle());
                leftHolder.item.setCompoundDrawablesWithIntrinsicBounds(leftHolder.icon(moduleShareItem.getIcon()), null, null, null);
                leftHolder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        Log.d("hrl", "position = " + position);
                        if (itemClickListener != null) itemClickListener.click(moduleShareItem, position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mModuleShareItems.size();
        }

        /**
         * horizontal item adapter
         */
        class TopHolder extends RecyclerView.ViewHolder {
            private TextView item;

            TopHolder(View view) {
                super(view);

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.width = getScreenWidth(getContext()) / GRID_NUMBER;

                item = new TextView(view.getContext());
                item.setLayoutParams(params);
                item.setMaxLines(1);
                item.setEllipsize(TextUtils.TruncateAt.END);
                item.setGravity(Gravity.CENTER);
                item.setTextColor(ContextCompat.getColor(view.getContext(), R.color.module_share_gray_font_dark));
                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.module_share_font_small));
                item.setCompoundDrawablePadding(topPadding);
                item.setPadding(0, padding, 0, padding);

                TypedValue typedValue = new TypedValue();
                view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
                item.setBackgroundResource(typedValue.resourceId);

                ((LinearLayout) view).addView(item);
            }

            private Drawable icon(Drawable drawable) {
                if (drawable != null) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    @SuppressWarnings("SuspiciousNameCombination")
                    Drawable resizeIcon = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmap, topIcon, topIcon, true));
                    Drawable.ConstantState state = resizeIcon.getConstantState();
                    resizeIcon = DrawableCompat.wrap(state == null ? resizeIcon : state.newDrawable().mutate());
                    return resizeIcon;
                }
                return null;
            }
        }

        class LeftHolder extends RecyclerView.ViewHolder {
            private TextView item;

            LeftHolder(View view) {
                super(view);

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                view.setLayoutParams(params);
                item = new TextView(view.getContext());
                item.setLayoutParams(params);
                item.setMaxLines(1);
                item.setEllipsize(TextUtils.TruncateAt.END);
                item.setGravity(Gravity.CENTER_VERTICAL);
                item.setTextColor(ContextCompat.getColor(view.getContext(), R.color.module_share_black));
                item.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen.module_share_font_normal));
                item.setCompoundDrawablePadding(leftPadding);
                item.setPadding(padding, padding, padding, padding);

                TypedValue typedValue = new TypedValue();
                view.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
                item.setBackgroundResource(typedValue.resourceId);

                ((LinearLayout) view).addView(item);
            }

            private Drawable icon(Drawable drawable) {
                if (drawable != null) {
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    @SuppressWarnings("SuspiciousNameCombination") Drawable resizeIcon = new BitmapDrawable(getContext().getResources(), Bitmap.createScaledBitmap(bitmap, leftIcon, leftIcon, true));
                    Drawable.ConstantState state = resizeIcon.getConstantState();
                    resizeIcon = DrawableCompat.wrap(state == null ? resizeIcon : state.newDrawable().mutate());
                    return resizeIcon;
                }
                return null;
            }
        }
    }

    class InviteWayHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        ImageView imageView;
        TextView textView;

        public InviteWayHolder(View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.ll_invite_way_item);
            imageView = itemView.findViewById(R.id.iv_invite_way_item);
            textView = itemView.findViewById(R.id.tv_invite_way_item);
        }

    }

    public int getScreenWidth(Context c) {
        return c.getResources().getDisplayMetrics().widthPixels;
    }
}
