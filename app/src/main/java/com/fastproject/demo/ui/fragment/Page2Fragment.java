package com.fastproject.demo.ui.fragment;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.fastproject.demo.R;
import com.fastproject.demo.aop.SingleClick;
import com.fastproject.demo.app.AppConfig;
import com.fastproject.demo.app.TitleBarFragment;
import com.fastproject.demo.ui.activity.HomeActivity;
import com.fastproject.demo.utils.FileUtils;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.io.File;
import java.util.List;

public class Page2Fragment extends TitleBarFragment<HomeActivity> {


    public static Page2Fragment newInstance() {
        return new Page2Fragment();
    }

    private AppCompatImageView iv_camera_return;

    private Uri cameraImgUri;
    private String cropPath;
    private static final int REQUEST_CODE_CAPTURE_RAW = 1001;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_page2;
    }

    @Override
    protected void initView() {
        iv_camera_return = findViewById(R.id.iv_camera_return);
        setOnClickListener(R.id.btnTakeCamera, R.id.iv_camera_return);
    }

    @Override
    protected void initData() {
    }


    @SingleClick
    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.btnTakeCamera) {
            goToCamera();
        }
        if (viewId == R.id.iv_camera_return) {
            toast("照片控件被点击");
        }
    }

    private void goToCamera() {
        XXPermissions.with(getActivity())
                .permission(new String[]{Permission.MANAGE_EXTERNAL_STORAGE, Permission.CAMERA})
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = FileUtils.getImgFile(false);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                // 通过 FileProvider 创建一个 Content 类型的 Uri 文件
                                cameraImgUri = FileProvider.getUriForFile(getActivity(), AppConfig.getPackageName() + ".provider", file);
                            } else {
                                cameraImgUri = Uri.fromFile(file);
                            }
                            cropPath = file.getPath();
                            // 对目标应用临时授权该 Uri 所代表的文件
                            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            // 将拍取的照片保存到指定 Uri
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImgUri);
                            startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_RAW);
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        toast("无法启动相机");
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CAPTURE_RAW) {
            if (cameraImgUri != null) {
                if (!TextUtils.isEmpty(cropPath)) {
                    // 通知系统多媒体扫描该文件，否则会导致拍摄出来的图片或者视频没有及时显示到相册中，而需要通过重启手机才能看到
                    MediaScannerConnection.scanFile(getActivity().getApplicationContext(), new String[]{cropPath}, null, null);
                    Glide.with(getActivity()).load(cropPath).into(iv_camera_return);
                }
            }
        }
    }
}
