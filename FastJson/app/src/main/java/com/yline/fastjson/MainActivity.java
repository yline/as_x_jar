package com.yline.fastjson;

import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.yline.fastjson.model.Group;
import com.yline.log.LogFileUtil;
import com.yline.test.BaseTestActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseTestActivity {

    @Override
    public void testStart(View view, Bundle savedInstanceState) {
        addButton("String==>Json", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Group group = new Group();
                group.setId(0L);
                group.setName("admin");

                Group.User guestUser = new Group.User();
                guestUser.setId(2L);
                guestUser.setName("guest");

                Group.User rootUser = new Group.User();
                rootUser.setId(3L);
                rootUser.setName("root");

                List<Group.User> userList = new ArrayList<>();
                userList.add(guestUser);
                userList.add(rootUser);
                group.setUsers(userList);


                String jsonString = JSON.toJSONString(group);
                LogFileUtil.v("jsonString = " + jsonString);
            }
        });

        addButton("Json==>String", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonString = "{\"id\":0,\"name\":\"admin\",\"users\":[{\"id\":2,\"name\":\"guest\"},{\"id\":3,\"name\":\"root\"}]}";
                Group group = JSON.parseObject(jsonString, Group.class);

                LogFileUtil.v("group = " + group);
            }
        });
    }
}
