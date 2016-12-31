package net.dearcode.candy.ui.activity;

import android.content.Intent;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import net.dearcode.candy.MainApp;
import net.dearcode.candy.R;
import net.dearcode.candy.localdb.localpreferences.LocalPreferences;
import net.dearcode.candy.ui.fragment.FragmentConversation;
import net.dearcode.candy.ui.fragment.FragmentExplore;
import net.dearcode.candy.ui.fragment.FragmentFriends;
import net.dearcode.candy.ui.fragment.FragmentOther;
import net.dearcode.candy.ui.base.BaseFragmentActivity;

/**
 * Created by 水寒 on 2016/9/17.
 * 主Activity
 */
public class MainActivity extends BaseFragmentActivity {

    private FragmentTabHost mTabHost;

    private Class<?> fragmentArray[] = {FragmentConversation.class, FragmentFriends.class,
            FragmentExplore.class, FragmentOther.class};

    private int mImageViewArray[] = {R.drawable.tab_conversation, R.drawable.tab_people,
            R.drawable.tab_find, R.drawable.tab_set};

    private String mTextviewArray[] = {"对话","好友","发现","更多"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!MainApp.getInstance().isLogin()) {
            exit();
        }

        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        setTabInfo();
    }

    private void setTabInfo() {
        int count = fragmentArray.length;

        mTabHost.setCurrentTab(0);
        mTabHost.clearAllTabs();
        for(int i=0;i<count; i++) {
            TabHost.TabSpec tabSpec= mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
        }
    }

    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setContentDescription(mTextviewArray[index]);
        imageView.setImageResource(mImageViewArray[index]);

        return view;
    }

    public void exit() {
        // 清空登录
        LocalPreferences localPreferences = MainApp.getInstance().getLocalPreferences();
        if(localPreferences != null) {
            localPreferences.save("login", "");
        }
        MainApp.getInstance().setLogin(true);
        // 跳转
        Intent intent=new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        return;
    }
}
