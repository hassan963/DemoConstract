package com.constructefile.democonstract.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.constructefile.democonstract.fragment.HowToApproachingTasks;
import com.constructefile.democonstract.fragment.HowToDosAndDonts;
import com.constructefile.democonstract.fragment.HowToOHNSLaws;
import com.constructefile.democonstract.fragment.HowToOperateSafely;
import com.constructefile.democonstract.fragment.HowToOperationSafety;
import com.constructefile.democonstract.fragment.ProfileFragment;
import com.constructefile.democonstract.R;
import com.constructefile.democonstract.helper.SQLiteHandler;
import com.constructefile.democonstract.helper.SessionManager;

public class MainMenu extends AppCompatActivity {


    private SQLiteHandler db;
    private SessionManager session;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    //load images here when have such function

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "profile";
    private static final String TAG_OPERATE_SAFELY = "operate_safely";
    private static final String TAG_APPROACHE_TASK = "approache_task";
    private static final String TAG_OPERATOR_SAFETY = "operator_safety";
    private static final String TAG_DOS_DONTS = "dos_donts";
    private static final String TAG_OHNS_LAWS = "ohns_laws";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//Getting session
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }


        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText("Operator");
        txtWebsite.setText("CONSTRUCTEFILE APP");
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            case 1:
                // How to
                HowToOperateSafely howToOperateSafely = new HowToOperateSafely();
                return howToOperateSafely;
            case 2:
                // Hoe to fragment
                HowToApproachingTasks howToApproachingTasks = new HowToApproachingTasks();
                return howToApproachingTasks;
            case 3:
                // How to fragment
                HowToOperationSafety howToOperationSafety = new HowToOperationSafety();
                return howToOperationSafety;

            case 4:
                // How to fragment
                HowToDosAndDonts howToDosAndDonts = new HowToDosAndDonts();
                return howToDosAndDonts;

            case 5:
                //  fragment
                HowToOHNSLaws howToOHNSLaws = new HowToOHNSLaws();
                return howToOHNSLaws;

            default:
                return new ProfileFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_o_safely:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_OPERATE_SAFELY;
                        break;
                    case R.id.nav_a_t:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_APPROACHE_TASK;
                        break;
                    case R.id.nav_o_safety:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_OPERATOR_SAFETY;
                        break;
                    case R.id.nav_dos:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_DOS_DONTS;
                        break;
                    case R.id.nav_ohns_laws:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_OHNS_LAWS;
                        break;

                    case R.id.nav_timesheet:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainMenu.this, TimeSheet.class));
                        drawer.closeDrawers();
                        return true;
                    /*case R.id.nav_tailgate_meeting:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainMenu.this, TailgateMeeting.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_daily_task:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainMenu.this, DailyTask.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_hazard_assesment:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainMenu.this, HazardAssesment.class));
                        drawer.closeDrawers();
                        return true;*/
                    case R.id.nav_category:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainMenu.this, CategoryActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_injury_report:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainMenu.this, Injury_Report_Activity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_near_miss:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainMenu.this, NearMiss_Activity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        logoutUser();
                        drawer.closeDrawers();
                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }*/

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }



    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        db.deleteLabels();
        db.deleteEquipments();

        // Launching the login activity
        Intent intent = new Intent(MainMenu.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
