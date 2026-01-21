package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private Fragment contactsFragment;
    private Fragment dialerFragment;
    private Fragment profileFragment;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_container); // 使用新的容器布局

        initFragments();
        setupBottomNavigation();

        // 默认显示通讯录
        switchFragment(contactsFragment, "contacts");
    }

    private void initFragments() {
        // 创建三个Fragment实例
        contactsFragment = new ContactsFragment();
        dialerFragment = new DialerFragment();
        profileFragment = new ProfileFragment();
    }

    private void setupBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_contacts) {
                    switchFragment(contactsFragment, "contacts");
                    return true;
                } else if (itemId == R.id.nav_dialer) {
                    switchFragment(dialerFragment, "dialer");
                    return true;
                } else if (itemId == R.id.nav_profile) {
                    switchFragment(profileFragment, "profile");
                    return true;
                }
                return false;
            }
        });
    }

    private void switchFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 隐藏当前显示的Fragment
        if (activeFragment != null) {
            transaction.hide(activeFragment);
        }

        // 如果Fragment已经添加，则显示它
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment != null) {
            transaction.show(existingFragment);
            activeFragment = existingFragment;
        } else {
            // 否则添加新的Fragment
            transaction.add(R.id.fragment_container, fragment, tag);
            activeFragment = fragment;
        }

        transaction.commit();
    }

    private long lastBackPressTime = 0;

    @Override
    public void onBackPressed() {
        // 双击返回退出
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackPressTime < 2000) {
            super.onBackPressed();
        } else {
            lastBackPressTime = currentTime;
            Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
        }
    }
}