package com.example.yudikarma.aplikasidokterfkh.Adapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.yudikarma.aplikasidokterfkh.Fragment.ChatFragment;
import com.example.yudikarma.aplikasidokterfkh.Fragment.FriendsFragment;
import com.example.yudikarma.aplikasidokterfkh.Fragment.RequestsFragment;

public class SectionPageAdapter extends FragmentPagerAdapter {
    public SectionPageAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {


        switch(position) {
            case 0:
                ChatFragment chatsFragment = new ChatFragment();
                return  chatsFragment;

            case 1:

            FriendsFragment friendsFragment = new FriendsFragment();
            return friendsFragment;

            case 2:

            RequestsFragment requestsFragment = new RequestsFragment();
            return requestsFragment;

            default:
                return  null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "REQUEST";
            case 1:
                return "CHATS";
            case 2:
                return "Friends";
            default:
                return null;
        }

    }
}
