package com.rebeau.permission;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;

import com.rebeau.base.utils.RBLogUtil;
import com.rebeau.base.utils.rom.HuaweiUtil;
import com.rebeau.base.utils.rom.MeizuUtil;
import com.rebeau.base.utils.rom.MiuiUtil;
import com.rebeau.base.utils.rom.OtherRomUtil;
import com.rebeau.base.utils.rom.QikuUtil;
import com.rebeau.base.utils.rom.RomUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Description:
 */
public class FloatPerMissionUtil {

    private static volatile FloatPerMissionUtil instance;

    private Dialog dialog;

    public static FloatPerMissionUtil getInstance() {
        if (instance == null) {
            synchronized (FloatPerMissionUtil.class) {
                if (instance == null) {
                    instance = new FloatPerMissionUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 判断是否有悬浮窗权限
     * true 表示有权限
     * false 表示需要申请权限
     *
     * @param context
     * @return
     */
    public boolean checkFloatWindowPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //return Settings.canDrawOverlays(context);
            return commonROMPermissionCheck(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //AppOpsManager添加于API 19
            boolean hasPermission = checkOps(context);
            RBLogUtil.dt(hasPermission);
            return hasPermission;
        } else {
            //4.4以下一般都可以直接添加悬浮窗
            return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean checkOps(Context context) {
        switch (RomUtil.getRomName()) {
            case RomUtil.ROM_MIUI:
                return MiuiUtil.checkFloatWindowPermission(context);
            case RomUtil.ROM_EMUI:
                return HuaweiUtil.checkFloatWindowPermission(context);
            case RomUtil.ROM_FLYME:
                return MeizuUtil.checkFloatWindowPermission(context);
            case RomUtil.ROM_QIKU:
                return QikuUtil.checkFloatWindowPermission(context);
            case RomUtil.ROM_VIVO:
                //break;
            case RomUtil.ROM_OPPO:
                //break;
            case RomUtil.ROM_SMARTISAN:
                //break;
            case RomUtil.ROM_COOLPAD:
                //break;
            case RomUtil.ROM_ZTE:
                //break;
            case RomUtil.ROM_LENOVO:
                //break;
            case RomUtil.ROM_LETV:
                //break;
            default:
                return RomUtil.checkOtherOps(context);
        }
    }

    /**
     * 6.0 判断权限
     */
    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtil.checkIsMeizuRom()) {
            return MeizuUtil.checkFloatWindowPermission(context);
        } else if (RomUtil.checkIsHuaweiRom() && "FRD-DL00".equals(Build.MODEL)) {
            return true;
        } else if (RomUtil.isOppoRom()) { //oppo 6.0 也是奇葩， 没有权限在应用内也是能弹出悬浮窗 (R9s)
            return true;
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= 23) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    RBLogUtil.et(e);
                }
            }
            RBLogUtil.dt(result);
            return result;
        }
    }

    /**
     * 权限申请
     *
     * @param context
     */
    public void applyPermission(Context context) {
        // < 23
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            switch (RomUtil.getRomName()) {
                case RomUtil.ROM_MIUI:
                    miuiROMPermissionApply(context);
                    break;
                case RomUtil.ROM_EMUI:
                    huaweiROMPermissionApply(context);
                    break;
                case RomUtil.ROM_FLYME:
                    meizuROMPermissionApply(context);
                    break;
                case RomUtil.ROM_QIKU:
                    ROM360PermissionApply(context);
                    break;
                case RomUtil.ROM_VIVO:
                    ROMVivoPermissionApply(context);
                    break;
                case RomUtil.ROM_OPPO:
                    ROMOPPOPermissionApply(context);
                    break;
                case RomUtil.ROM_COOLPAD:
                    ROMCoolpadPermissionApply(context);
                    break;
                case RomUtil.ROM_ZTE:
                    ROMZTEPermissionApply(context);
                    break;
                case RomUtil.ROM_LENOVO:
                    ROMLenovoPermissionApply(context);
                    break;
                case RomUtil.ROM_LETV:
                    ROMLetvPermissionApply(context);
                    break;
                case RomUtil.ROM_SMARTISAN:
                    ROMSmartPermissionApply(context);
                    break;
                default:
                    RomUtil.showAlertToast(context);
                    break;
            }
        } else {
            commonROMPermissionApply(context);
        }
    }

    /**
     * coolpad权限申请页面
     */
    private void ROMCoolpadPermissionApply(final Context context) {

        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OtherRomUtil.applyCoolpadPermission(context);
                } else {
                    RBLogUtil.et("ROM:coolpad, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * smart权限申请页面
     */
    private void ROMSmartPermissionApply(final Context context) {

        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OtherRomUtil.applySmartisanPermission(context);
                } else {
                    RBLogUtil.et("ROM:smart, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * lenovo权限申请页面
     */
    private void ROMLenovoPermissionApply(final Context context) {

        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OtherRomUtil.applyLenovoPermission(context);
                } else {
                    RBLogUtil.et("ROM:lenovo, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * zte权限申请页面
     */
    private void ROMZTEPermissionApply(final Context context) {

        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OtherRomUtil.applyZTEPermission(context);
                } else {
                    RBLogUtil.et("ROM:ZTE, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * letv权限申请页面
     */
    private void ROMLetvPermissionApply(final Context context) {

        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OtherRomUtil.applyLetvPermission(context);
                } else {
                    RBLogUtil.et("ROM:letv, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * vivo权限申请页面
     */
    private void ROMVivoPermissionApply(final Context context) {

        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OtherRomUtil.applyVivoPermission(context);
                } else {
                    RBLogUtil.et("ROM:vivo, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * oppo权限申请页面
     */
    private void ROMOPPOPermissionApply(final Context context) {

        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    OtherRomUtil.applyOppoPermission(context);
                } else {
                    RBLogUtil.et("ROM:oppo, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void ROM360PermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QikuUtil.applyPermission(context);
                } else {
                    RBLogUtil.et("ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void huaweiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaweiUtil.applyPermission(context);
                } else {
                    RBLogUtil.et("ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void meizuROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeizuUtil.applyPermission(context);
                } else {
                    RBLogUtil.et("ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void miuiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MiuiUtil.applyMiuiPermission(context);
                } else {
                    RBLogUtil.et("ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 通用 rom 权限申请
     * 6.0以上才用的到
     */
    private void commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtil.checkIsMeizuRom()) {
            meizuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            try {
                                Class clazz = Settings.class;
                                Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

                                Intent intent = new Intent(field.get(null).toString());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                context.startActivity(intent);
                            } catch (Exception e) {
                                RBLogUtil.et(e);
                            }
                        } else {
                            RBLogUtil.dt("user manually refuse OVERLAY_PERMISSION");
                            //需要做统计效果
                        }
                    }
                });
            }
        }
    }

    private void showConfirmDialog(Context context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Context context, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new AlertDialog.Builder(context).setCancelable(true).setTitle("")
                .setMessage(message)
                .setPositiveButton("现在去开启",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(true);
                                dialog.dismiss();
                            }
                        }).setNegativeButton("暂不开启",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                result.confirmResult(false);
                                dialog.dismiss();
                            }
                        }).create();
        dialog.show();
    }

    private interface OnConfirmResult {
        void confirmResult(boolean confirm);
    }

}
