package net.dearcode.candy;

import android.content.Intent;

import com.forlong401.log.transaction.log.manager.LogManager;
import com.meikoz.core.MainApplication;
import com.meikoz.core.api.RestApi;
import com.meikoz.core.manage.crash.AndroidCrash;
import com.meikoz.core.manage.interfacee.HttpReportCallback;

import net.dearcode.candy.controller.component.DB;
import net.dearcode.candy.controller.component.ServiceBinder;
import net.dearcode.candy.controller.service.MessageService;
import net.dearcode.candy.localdb.localpreferences.LocalPreferences;
import net.dearcode.candy.model.User;

import java.io.File;

/**
 * Created by jsson on 16/12/31.
 */
public class MainApp extends MainApplication {
    private static final MainApp INSTANCE = new MainApp();
    private LocalPreferences localPreferences = null;
    private static ServiceBinder binder;
    public static DB db;

    private User mMyself = new User();

    private boolean isLogin = false;

    public boolean isLogin() {
        return isLogin;
    }

    public LocalPreferences getLocalPreferences() {
        return localPreferences;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }


    public static MainApp getInstance() {
        return INSTANCE;
    }
    @Override
    public void onCreate() {
        RestApi.getInstance().bug(Constants.Config.DEVELOPER_MODE);
        super.onCreate();

        LogManager.getManager(this).registerCrashHandler();

        db = new DB(this);

        localPreferences = new LocalPreferences(this);

        String login = localPreferences.get("login");
        if(login == null || "".equals(login)) {
            isLogin = false;
        } else {
            String[] split = login.split("\\#\\@\\#");
            int i = split.length;
            mMyself.setName(split[0]);
            mMyself.setPassword(split[1]);
            mMyself.setID(Long.parseLong(split[2]));
            isLogin = true;
        }
        Intent i = new Intent(this, MessageService.class);
        i.putExtra("type", "1");
        i.putExtra("account", mMyself.getName());
        i.putExtra("passwd", mMyself.getPassword());
        this.startService(i);

        configure();

    }

    public User getMyself() {
        return mMyself;
    }

    public static CandyMessage getService() {
        return binder.getCandy();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        binder.Disconnect();
        LogManager.getManager(this).unregisterCrashHandler();
    }

    private void configure() {
        HttpReportCallback callback = new HttpReportCallback() {
            @Override
            public void uploadException2remote(File file) {
                // param file is exception file
            }
        };
        AndroidCrash.getInstance().setCrashReporter(callback).init(this);
    }

}
