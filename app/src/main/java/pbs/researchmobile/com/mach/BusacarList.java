package pbs.researchmobile.com.mach;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import pbs.researchmobile.com.mach.users.GetUsersContract;
import pbs.researchmobile.com.mach.users.GetUsersPresenter;
import pbs.researchmobile.com.mach.users.ItemClickSupport;
import pbs.researchmobile.com.mach.users.User;
import pbs.researchmobile.com.mach.users.UserListingRecyclerAdapter;

public class BusacarList extends AppCompatActivity implements ItemClickSupport.OnItemClickListener, GetUsersContract.View, SearchView.OnQueryTextListener {

    public static final String ARG_TYPE = "type";
    public static final String TYPE_CHATS = "type_chats";
    public static final String TYPE_ALL = "type_all";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerViewAllUserListing;

    private UserListingRecyclerAdapter mUserListingRecyclerAdapter;

    private GetUsersPresenter mGetUsersPresenter;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busacar_list);

        mRecyclerViewAllUserListing = (RecyclerView)findViewById(R.id.recycler_view_all_user_listing);
        searchView = (SearchView)findViewById(R.id.idSearch);
        searchView.setOnQueryTextListener(this);
        mGetUsersPresenter = new GetUsersPresenter(this);
        getUsers();

        ItemClickSupport.addTo(mRecyclerViewAllUserListing).setOnItemClickListener(this);

    }

    private void getUsers() {
        //if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_CHATS)) {

        //} else if (TextUtils.equals(getArguments().getString(ARG_TYPE), TYPE_ALL)) {
        mGetUsersPresenter.getAllUsers();
        //}
    }

    @Override
    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
        ChatActivity.startActivity(this,
                mUserListingRecyclerAdapter.getUser(position).email,
                mUserListingRecyclerAdapter.getUser(position).uid,
                mUserListingRecyclerAdapter.getUser(position).firebaseToken,
                mUserListingRecyclerAdapter.getUser(position).username);
    }

    @Override
    public void onGetAllUsersSuccess(List<User> users) {
        mUserListingRecyclerAdapter = new UserListingRecyclerAdapter(users);
        mRecyclerViewAllUserListing.setAdapter(mUserListingRecyclerAdapter);
        mUserListingRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetAllUsersFailure(String message) {
        Toast.makeText(this, "Error: " + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetChatUsersSuccess(List<User> users) {

    }

    @Override
    public void onGetChatUsersFailure(String message) {

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BusacarList.this,Paint.class));
        finish();
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
       // String text = newText;
       // mUserListingRecyclerAdapter.getFilter().filter(newText);

        return false;
    }
}
