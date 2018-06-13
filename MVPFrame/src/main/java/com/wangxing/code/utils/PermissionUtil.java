package com.wangxing.code.utils;

import android.content.Context;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;

/**
 * Created by WangXing on 2018/6/13.
 */
public class PermissionUtil {

    public static void ApplyWithOutCallBack(final Context context, String... permissions) {
        ApplyPermission(context, null, null, null, permissions);
    }

    public static void ApplyWithRationale(final Context context, Rationale rationale, String... permissions) {
        ApplyPermission(context, rationale, null, null, permissions);
    }

    public static void ApplyPermission(final Context context, Rationale rationale, Action yesAc, Action noAc, String... permissions) {
        AndPermission.with(context)
                .permission(permissions)
                .rationale(rationale)
                .onGranted(yesAc).onDenied(noAc).start();
    }


//     AndPermission.with(this)
//             .permission(Manifest.permission.READ_EXTERNAL_STORAGE
//            , Manifest.permission.CAMERA
//            , Manifest.permission.ACCESS_COARSE_LOCATION
//            , Manifest.permission.ACCESS_FINE_LOCATION
//            , Manifest.permission.READ_PHONE_STATE
//            , Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .rationale(mRationale)
//                .onGranted(new Action() {
//        @Override
//        public void onAction(List<String> permissions) {
//            // TODO 同意
//
//            AmapLocationManager.getInstance(UserMainActivity.this).getLocation(new AMapLocationListener() {
//                @Override
//                public void onLocationChanged(AMapLocation aMapLocation) {
//
//                    UserManager.getInstance().saveLongitude(UserMainActivity.this, GsonUtil.format0(aMapLocation.getLongitude()));
//                    UserManager.getInstance().saveLatitude(UserMainActivity.this, GsonUtil.format0(aMapLocation.getLatitude()));
//
//                }
//            });
//
//        }
//    }).onDenied(new Action() {
//        @Override
//        public void onAction(List<String> permissions) {
//            // TODO 拒绝
//            if (AndPermission.hasAlwaysDeniedPermission(UserMainActivity.this, permissions)) {
    // 这些权限被用户总是拒绝。
    // 这里使用一个Dialog展示没有这些权限应用程序无法继续运行，询问用户是否去设置中授权。

//                    SettingService settingService = AndPermission.permissionSetting(mContext);

    // 如果用户同意去设置：
//                    settingService.execute();

    // 如果用户不同意去设置：
//                    settingService.cancel();
//            }
//        }
//    }).start();

//    private Rationale mRationale = new Rationale() {
//        @Override
//        public void showRationale(Context context, List<String> permissions,
//                                  RequestExecutor executor) {
//            // 这里使用一个Dialog询问用户是否继续授权。
//
//            // 如果用户继续：
//            executor.execute();
//
//            // 如果用户中断：
//            executor.cancel();
//        }
//    };

}
