package net.sourceforge.simcpux;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbiz.SubscribeMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yline.base.BaseActivity;

public class SubscribeMessageActivity extends BaseActivity {

    private IWXAPI api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_APP_ID,false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subscribe_message);

        Button checkSubscribeMsgBtn = (Button) findViewById(R.id.check_subscribe_message_btn);
        checkSubscribeMsgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean supported = api.getWXAppSupportAPI() >= Build.SUBSCRIBE_MESSAGE_SUPPORTED_SDK_INT;
                Toast.makeText(SubscribeMessageActivity.this, supported ? "supported" : "unsupported", Toast.LENGTH_SHORT).show();
            }
        });

        Button subscribeMsgBtn = (Button) findViewById(R.id.subscribe_message_btn);
        subscribeMsgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText sceneEt = (EditText)findViewById(R.id.scene_et);
                EditText templateIdEt = (EditText)findViewById(R.id.templateid_et);
                EditText reservedEt = (EditText)findViewById(R.id.reserved_et);

                SubscribeMessage.Req req = new SubscribeMessage.Req();
                req.scene = Util.parseInt(sceneEt.getText().toString().trim(), 0);
                req.templateID = templateIdEt.getText().toString().trim();
                req.reserved = reservedEt.getText().toString().trim();

                boolean ret = api.sendReq(req);
                Toast.makeText(SubscribeMessageActivity.this, "sendReq result = " + ret, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
