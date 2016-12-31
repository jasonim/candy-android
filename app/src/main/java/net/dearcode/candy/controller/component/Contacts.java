package net.dearcode.candy.controller.component;

import android.util.Log;

import net.dearcode.candy.MainApp;
import net.dearcode.candy.model.FriendList;
import net.dearcode.candy.model.ServiceResponse;
import net.dearcode.candy.model.User;

import java.util.ArrayList;

/**
 *  * Created by c-wind on 2016/9/26 16:50
 *  * mail：root@codecn.org
 *  
 */
public class Contacts {
    public static ArrayList<User> getContacts() {
        ArrayList<User> users = null;

        boolean hasUser = MainApp.getInstance().getLocalPreferences().getBool("friends");
        if(hasUser) {
            users = MainApp.db.getFriends();
        } else {
            users = new ArrayList<>();
            ServiceResponse sr = new RPC() {
                public ServiceResponse getResponse() throws Exception {
                    return MainApp.getService().loadFriendList();
                }
            }.Call();
            if (sr.hasError()) {
                Log.e("", "rpc load friend list error:" + sr.getError());
                return users;
            }
            FriendList list = sr.getFriendList();
            if (list == null || list.Users == null || list.Users.length == 0) {
                return users;
            }
            for (final long id : list.Users) {
                sr = new RPC() {
                    public ServiceResponse getResponse() throws Exception {
                        return MainApp.getService().loadUserInfo(id);
                    }
                }.Call();
                if (sr.hasError()) {
                    Log.e("", "rpc load userInfo error:" + sr.getError());
                    return users;
                }
                User user = sr.getUser();
                users.add(user);
                // store friends
                MainApp.db.saveFriend(id);
                // store details
                MainApp.db.saveUser(user.getID(), user.getName(), user.getNickName(), null);
            }
            MainApp.getInstance().getLocalPreferences().saveBool("friends", true);
        }

        return users;
    }

}
