package pbs.researchmobile.com.mach.users;

import java.util.List;
import pbs.researchmobile.com.mach.users.User;


public interface GetUsersContract {
    interface View {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);

        void onGetChatUsersSuccess(List<User> users);

        void onGetChatUsersFailure(String message);
    }

    interface Presenter {
        void getAllUsers();

        void getChatUsers();
    }

    interface Interactor {
        void getAllUsersFromFirebase();

        void getChatUsersFromFirebase();
    }

    interface OnGetAllUsersListener {
        void onGetAllUsersSuccess(List<User> users);

        void onGetAllUsersFailure(String message);
    }

    interface OnGetChatUsersListener {
        void onGetChatUsersSuccess(List<User> users);

        void onGetChatUsersFailure(String message);
    }
}
