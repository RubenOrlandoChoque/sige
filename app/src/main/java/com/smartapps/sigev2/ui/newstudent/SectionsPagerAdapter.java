package com.smartapps.sigev2.ui.newstudent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public String studentId;

    public SectionsPagerAdapter(FragmentManager supportFragmentManager, String studentId) {
        super(supportFragmentManager);
        this.studentId = studentId;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a StudentFragment (defined as a static inner class below).
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new StudentFragment();
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                break;
            case 1:
                fragment = new TutorsListFragment();
                bundle = new Bundle();
                fragment.setArguments(bundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
        }
        return null;
    }
}