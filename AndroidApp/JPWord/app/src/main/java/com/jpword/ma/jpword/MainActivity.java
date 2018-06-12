package com.jpword.ma.jpword;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


import DataEngine.AppLogging;
import DataEngine.DBEntity;
import DataEngine.DatabaseServiceConnection;
import DataEngine.IDatabaseChangeListener;

public class MainActivity extends AppCompatActivity implements IDatabaseChangeListener {

    private ViewPager viewPager_;
    private MenuItem menuItem_;
    private BottomNavigationView bottomNavigationView_;

    private FragmentFind findFragment_ = null;
    private FragmentSync syncFragment_ = null;
    private FragmentRemember rememberFragment_ = null;
    private FragmentMy myFragment_ = null;

    private DBEntity dbEntity_ = null;

    private DatabaseServiceConnection connection_ = new DatabaseServiceConnection() {
        @Override
        public void onServiceConnected() {
            AppLogging.showDebug(MainActivity.class, "onServiceConnected");
            dbEntity_ = getDatabaseOperator().getDBEntity();
            setupMainPage(dbEntity_);
        }

        @Override
        public void onServiceDisconnected() {

        }
    };

    @Override
    public void onDatabaseChange(DBEntity dbEntity) {
        dbEntity_ = dbEntity;
        findFragment_.setDatabaseEntity(dbEntity_);
        myFragment_.setDatabaseEntity(dbEntity_);
        rememberFragment_.setDatabaseEntity(dbEntity_);
        syncFragment_.setDatabaseEntity(dbEntity_);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<Integer> mIDList = new ArrayList<>();
        private ViewPager viewPager_ = null;
        private int current_ = 0;


        public ViewPagerAdapter(ViewPager v, FragmentManager manager) {
            super(manager);
            viewPager_ = v;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(int id, Fragment fragment) {
            mFragmentList.add(fragment);
            mIDList.add(id);
        }

        public boolean switchView(int id) {
            int index = 0;
            for (; index < mIDList.size(); index++) {
                if (mIDList.get(index) == id) {
                    viewPager_.setCurrentItem(index);
                    getItem(index).onResume();
                    getItem(current_).onPause();
                    current_ = index;
                    return true;
                }
            }
            return false;
        }
    }

    protected void initialize() {
        DatabaseService.bind(this, connection_);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void setupMainPage(DBEntity dbEntity) {
        findFragment_ = new FragmentFind();
        syncFragment_ = new FragmentSync();
        rememberFragment_ = new FragmentRemember();
        myFragment_ = new FragmentMy();

        onDatabaseChange(dbEntity);

        viewPager_ = (ViewPager) findViewById(R.id.mainViewPager);
        viewPager_.setOffscreenPageLimit(4);
        bottomNavigationView_ = (BottomNavigationView) findViewById(R.id.navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        bottomNavigationView_.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                ViewPagerAdapter adp = (ViewPagerAdapter) viewPager_.getAdapter();
                if (adp.switchView(item.getItemId())) {
                    return true;
                }
                return false;
            }
        });
        viewPager_.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem_ != null) {
                    menuItem_.setChecked(false);
                } else {
                    bottomNavigationView_.getMenu().getItem(0).setChecked(false);
                }
                menuItem_ = bottomNavigationView_.getMenu().getItem(position);
                menuItem_.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setupViewPager(viewPager_);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            dbEntity_.dict_.saveToDB();
            unbindService(connection_);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(viewPager_, getSupportFragmentManager());

        adapter.addFragment(R.id.navigation_remember, rememberFragment_);
        adapter.addFragment(R.id.navigation_find, findFragment_);
        adapter.addFragment(R.id.navigation_my, myFragment_);
        adapter.addFragment(R.id.navigation_sync, syncFragment_);
        viewPager.setAdapter(adapter);
    }
}
