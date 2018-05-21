package pbs.researchmobile.com.mach;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Tab1Fragment extends Fragment {

    private static final String TAG = "Tab1Fragment";
    private ViewPager mViewPagerUserListing;
    private TabLayout mTabLayoutUserListing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab1_fragment,container,false);


        mViewPagerUserListing = (ViewPager) view.findViewById(R.id.view_pager_user_listing);
        mTabLayoutUserListing = (TabLayout) view.findViewById(R.id.tab_layout_user_listing);

        UserListingPagerAdapter userListingPagerAdapter = new UserListingPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPagerUserListing.setAdapter(userListingPagerAdapter);

        // attach tab layout with view pager
        mTabLayoutUserListing.setupWithViewPager(mViewPagerUserListing);

        return view;
    }
}
