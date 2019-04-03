package net.sourceforge.simcpux;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbiz.SubscribeMiniProgramMsg;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yline.base.BaseActivity;


/**
 * Created by willenwu on 2018/5/10
 */

public class SubscribeMiniProgramMsgActivity extends BaseActivity {

    private IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID,false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_mini_program_msg);
        Button checkSubscribeMsgBtn = (Button)findViewById(R.id.check_subscribe_message_btn);
        checkSubscribeMsgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean supported = api.getWXAppSupportAPI() >= Build.SUBSCRIBE_MINI_PROGRAM_MSG_SUPPORTED_SDK_INT;
                Toast.makeText(SubscribeMiniProgramMsgActivity.this, supported ? "support" : "not support", Toast.LENGTH_SHORT).show();
            }
        });

        final EditText miniProgramAppIdEt = (EditText)findViewById(R.id.mini_program_appid_et);

        Button subscribeMsgBtn = (Button)findViewById(R.id.subscribe_message_btn);
        subscribeMsgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SubscribeMiniProgramMsg.Req req = new SubscribeMiniProgramMsg.Req();
                req.miniProgramAppId = miniProgramAppIdEt.getText().toString().trim();

                boolean ret = api.sendReq(req);
                String message = String.format("sendReq ret : %s", ret);
                Toast.makeText(SubscribeMiniProgramMsgActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
