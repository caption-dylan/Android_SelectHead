package com.zym.selecthead;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.zym.selecthead.config.Configs;
import com.zym.selecthead.tools.FileTools;
import com.zym.selecthead.tools.SelectHeadTools;

import java.io.File;
import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnPopUp;
    private ImageView iv_show;
    private Uri photoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnPopUp = (Button) this.findViewById(R.id.btn_popup);
        btnPopUp.setOnClickListener(this);
        iv_show = (ImageView) this.findViewById(R.id.iv_show);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_popup:
                if(!FileTools.hasSdcard()){
                    Toast.makeText(this,"没有找到SD卡，请检查SD卡是否存在",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    photoUri = FileTools.getUriByFileDirAndFileName(Configs.SystemPicture.SAVE_DIRECTORY, Configs.SystemPicture.SAVE_PIC_NAME);
                } catch (IOException e) {
                    Toast.makeText(this, "创建文件失败。", Toast.LENGTH_SHORT).show();
                    return;
                }
                SelectHeadTools.openDialog(this,photoUri);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Configs.SystemPicture.PHOTO_REQUEST_TAKEPHOTO: // 拍照
                SelectHeadTools.startPhotoZoom(this,photoUri, 600);
                break;
            case Configs.SystemPicture.PHOTO_REQUEST_GALLERY://相册获取
                if (data==null)
                    return;
                SelectHeadTools.startPhotoZoom(this, data.getData(), 600);
                break;
            case Configs.SystemPicture.PHOTO_REQUEST_CUT:  //接收处理返回的图片结果
                if (data==null)
                    return;
                Bitmap bit = data.getExtras().getParcelable("data");
                iv_show.setImageBitmap(bit);
                File file = FileTools.getFileByUri(this,photoUri);
                Log.d("File",file.toString());
                break;
        }
    }
}
