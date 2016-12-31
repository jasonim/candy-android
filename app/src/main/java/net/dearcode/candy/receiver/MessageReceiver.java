package net.dearcode.candy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import net.dearcode.candy.MainApp;
import net.dearcode.candy.controller.command.BroadcastMsg;
import net.dearcode.candy.model.Event;
import net.dearcode.candy.model.Message;
import net.dearcode.candy.util.Common;

import java.util.Date;

/**
 * Created by lujinfei on 2016/10/20.
 */

public class MessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle b = intent.getExtras();
        if(b == null) {
            Log.e(Common.LOG_TAG, "extans is null");
            return;
        }

        BroadcastMsg bm = b.getParcelable("broadcast");
        if(bm != null) {
            Intent i = new Intent("net.dearcode.candy.chat");
            i.putExtras(b);
            context.sendBroadcast(i);
            return;
        }

        Message m = b.getParcelable("message");
        if(m == null) {
            Log.e(Common.LOG_TAG, "Message is null");
            return;
        }

        if(m.getEvent() == Event.None) {
            if(m.isGroupMessage()) {

            }else {
                // 入库, 个人聊天
                MainApp.db.saveUserMessage(new Date().getTime(),
                        m.getFrom(),
                        m.getFrom(),
                        m.getMsg());
                // 入库，聊天列表
                //CustomeApplication.db.saveChatList(message);
            }
            Intent i = new Intent("net.dearcode.candy.chat");
            i.putExtras(b);
            context.sendBroadcast(i);
            return;

        }

    }
}
