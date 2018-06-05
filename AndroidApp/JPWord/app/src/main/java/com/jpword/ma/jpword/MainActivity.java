package com.jpword.ma.jpword;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

import com.jpword.ma.baseui.IMyDialog;
import com.jpword.ma.baseui.DialogSingleItemSelect;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import DataEngine.AppLogging;
import DataEngine.DB;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager_;
    private MenuItem menuItem_;
    private BottomNavigationView bottomNavigationView_;

    private FragmentFind findFragment_ = null;
    private FragmentSync syncFragment_ = null;
    private FragmentRemember rememberFragment_ = null;
    private FragmentMy myFragment_ = null;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (DB.getInstance().getDatabase() == null) {
                final List<String> dblist = JPWord.Data.Database.getInstance().getDictList();
                if (dblist.isEmpty()) {
                    dblist.add("Create New");
                }
                final DialogSingleItemSelect dlg = new DialogSingleItemSelect("Choose database", dblist, "");
                dlg.show(MainActivity.this, new IMyDialog.CallBack() {
                    @Override
                    public void confirmed() {
                        String ret = dlg.getResult();
                        if (ret.equals("Create New")) {
                            DB.getInstance().changeDatabase("Dictionary", true);
                        } else {
                            DB.getInstance().changeDatabase(ret, false);
                        }
                        setupMainPage();
                    }
                });
            } else {
                setupMainPage();
            }
        }
    };

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

    private static final int MY_PERMISSIONS_REQUEST_CODE = 1;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    initialize();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG);
                    this.finish();
                }
                return;
            }
        }
    }

    protected void initialize() {
        Intent startIntent = new Intent(this, DatabaseService.class);
        startService(startIntent);
        bindService(startIntent, mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_CODE);
        }
        else {
            initialize();
        }




        //System.out.println("Loading");
//        try {
//            DB.getInstance().initialize();
//        } catch (Exception e) {
//            System.err.println("+++++++++++++ Error: " + e.getMessage());
//        }
//        try {
//            File path = Environment.getExternalStorageDirectory();
//            File jpPath = new File(path.getCanonicalFile() + "/JP/");
//            File file = new File(jpPath.getCanonicalPath() + "/Dictionary.dat");
//            if (file.exists()) {
//                System.err.println("+++++++++++++ Exist");
//            }
//            else {
//                System.err.println("+++++++++++++ None exist");
//            }
//        }
//        catch (Exception e) {
//            System.err.println("+++++++++++++ Test Error: " + e.getMessage());
//        }


        //System.out.println("Done");


    }

    private void setupMainPage() {
        findFragment_ = new FragmentFind();
        syncFragment_ = new FragmentSync();
        rememberFragment_ = new FragmentRemember();
        myFragment_ = new FragmentMy();
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
            unbindService(mConnection);
            DB.getInstance().persist(this);
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
