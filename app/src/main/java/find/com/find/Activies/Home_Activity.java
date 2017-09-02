package find.com.find.Activies;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import find.com.find.Adapter.TabAdapter;
import find.com.find.R;


public class Home_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TabLayout home_tblayout = (TabLayout) findViewById(R.id.home_tblayout);
        home_tblayout.addTab(home_tblayout.newTab().setIcon(R.mipmap.ic_menu_ranking));
        home_tblayout.addTab(home_tblayout.newTab().setIcon(R.mipmap.ic_menu_maps));
        home_tblayout.addTab(home_tblayout.newTab().setIcon(R.mipmap.ic_menu_account));
        home_tblayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.home_viewpager);
        final TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(),home_tblayout.getTabCount());
        viewPager.setAdapter(tabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(home_tblayout));
        home_tblayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
