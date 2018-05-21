package pbs.researchmobile.com.mach;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import pbs.researchmobile.com.mach.users.Chat;
import pbs.researchmobile.com.mach.users.ChatContract;
import pbs.researchmobile.com.mach.users.ChatPresenter;
import pbs.researchmobile.com.mach.users.ChatRecyclerAdapter;
import pbs.researchmobile.com.mach.users.Constants;
import pbs.researchmobile.com.mach.users.PushNotificationEvent;

import static android.app.Activity.RESULT_OK;


public class ChatFragment extends Fragment implements ChatContract.View, TextView.OnEditorActionListener {

    private RecyclerView mRecyclerViewChat;
    private EditText mETxtMessage;
    private ProgressDialog mProgressDialog;
    private ChatRecyclerAdapter mChatRecyclerAdapter;
    private ChatPresenter mChatPresenter;
    private ImageButton mBtnEnviarFoto;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private static final int PHOTO_SEND = 1;

    public static ChatFragment newInstance(String receiver,
                                           String receiverUid,
                                           String firebaseToken) {
        Bundle args = new Bundle();
        args.putString(Constants.ARG_RECEIVER, receiver);
        args.putString(Constants.ARG_RECEIVER_UID, receiverUid);
        args.putString(Constants.ARG_FIREBASE_TOKEN, firebaseToken);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_chat, container, false);
        TextView send = (TextView)fragmentView.findViewById(R.id.text_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
        storage = FirebaseStorage.getInstance();
        bindViews(fragmentView);
        return fragmentView;
    }

    private void bindViews(View view) {
        mRecyclerViewChat = (RecyclerView) view.findViewById(R.id.recycler_view_chat);
        mETxtMessage = (EditText) view.findViewById(R.id.edit_text_message);
        mBtnEnviarFoto = (ImageButton) view.findViewById(R.id.btnEnviarFoto);
        mBtnEnviarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarImagen();
            }
        });
    }

    private void enviarImagen() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        i.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(i,"Selecciona una foto"),PHOTO_SEND);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PHOTO_SEND && resultCode == RESULT_OK){
            Uri u = data.getData();
            storageReference = storage.getReference("imagenes_chat");//imagenes_chat
            final StorageReference fotoReferencia = storageReference.child(u.getLastPathSegment());

            final String receiver = getArguments().getString(Constants.ARG_RECEIVER);
            final String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
            final String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            final String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);

            fotoReferencia.putFile(u).addOnSuccessListener(getActivity(), new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri u = taskSnapshot.getDownloadUrl();
                    Chat chat = new Chat(sender, receiver, senderUid, receiverUid, "Te ha enviado una foto",u.toString(), System.currentTimeMillis(),"2");
                    mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat, receiverFirebaseToken);
                }
            });
        }
    }

    private void init() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setTitle(getString(R.string.loading));
        mProgressDialog.setMessage(getString(R.string.please_wait));
        mProgressDialog.setIndeterminate(true);

        mETxtMessage.setOnEditorActionListener(this);

        mChatPresenter = new ChatPresenter(this);
        mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getArguments().getString(Constants.ARG_RECEIVER_UID));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEND) {
            sendMessage();
            return true;
        }
        return false;
    }

    private void sendMessage() {
        String message = mETxtMessage.getText().toString();
        if (message.isEmpty()){

        }else{
            String receiver = getArguments().getString(Constants.ARG_RECEIVER);
            String receiverUid = getArguments().getString(Constants.ARG_RECEIVER_UID);
            String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String senderUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String receiverFirebaseToken = getArguments().getString(Constants.ARG_FIREBASE_TOKEN);
            Chat chat = new Chat(sender, receiver, senderUid, receiverUid, message, System.currentTimeMillis(),"1");
            mChatPresenter.sendMessage(getActivity().getApplicationContext(), chat, receiverFirebaseToken);
        }


    }

    @Override
    public void onSendMessageSuccess() {
        mETxtMessage.setText("");
        //Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendMessageFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetMessagesSuccess(Chat chat) {
        if (mChatRecyclerAdapter == null) {
            mChatRecyclerAdapter = new ChatRecyclerAdapter(new ArrayList<Chat>(), this);
            mRecyclerViewChat.setAdapter(mChatRecyclerAdapter);
        }
        mChatRecyclerAdapter.add(chat);
        mRecyclerViewChat.smoothScrollToPosition(mChatRecyclerAdapter.getItemCount() - 1);
    }

    @Override
    public void onGetMessagesFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onPushNotificationEvent(PushNotificationEvent pushNotificationEvent) {
        if (mChatRecyclerAdapter == null || mChatRecyclerAdapter.getItemCount() == 0) {
            mChatPresenter.getMessage(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    pushNotificationEvent.getUid());
        }
    }
}
