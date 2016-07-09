package com.ulgebra.luxscarr;
import com.ulgebra.luxscarr.FragmentBookingDate;
import com.ulgebra.luxscarr.FragmentBookingHistory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import static com.ulgebra.luxscarr.R.id.info;

public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                return new FragmentBookingDate();
            case 1:
                // Games fragment activity
                return new FragmentBookingHistory();

        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}