package pbs.researchmobile.com.mach.users.add;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;


public interface AddUserContract {
    interface View {
        void onAddUserSuccess(String message);

        void onAddUserFailure(String message);
    }

    interface Presenter {
        void addUser(Context context, FirebaseUser firebaseUser, String unombre);
    }

    interface Interactor {
        void addUserToDatabase(Context context, FirebaseUser firebaseUser, String unombre);
    }

    interface OnUserDatabaseListener {
        void onSuccess(String message);

        void onFailure(String message);
    }
}
