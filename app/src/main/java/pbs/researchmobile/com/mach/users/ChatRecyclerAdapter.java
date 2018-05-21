package pbs.researchmobile.com.mach.users;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pbs.researchmobile.com.mach.ChatFragment;
import pbs.researchmobile.com.mach.R;


public class ChatRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ME = 1;
    private static final int VIEW_TYPE_OTHER = 2;

    private List<Chat> mChats;
    private ChatFragment c;

    public ChatRecyclerAdapter(List<Chat> chats, ChatFragment cont) {
        mChats = chats;
        c = cont;
    }

    public void add(Chat chat) {
        mChats.add(chat);
        notifyItemInserted(mChats.size() - 1);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_TYPE_ME:
                View viewChatMine = layoutInflater.inflate(R.layout.item_chat_mine, parent, false);
                viewHolder = new MyChatViewHolder(viewChatMine);
                break;
            case VIEW_TYPE_OTHER:
                View viewChatOther = layoutInflater.inflate(R.layout.item_chat_other, parent, false);
                viewHolder = new OtherChatViewHolder(viewChatOther);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            configureMyChatViewHolder((MyChatViewHolder) holder, position);
        } else {
            configureOtherChatViewHolder((OtherChatViewHolder) holder, position);
        }
    }

    private void configureMyChatViewHolder(MyChatViewHolder myChatViewHolder, int position) {
        Chat chat = mChats.get(position);
        String alphabet = chat.sender.substring(0, 1);
        myChatViewHolder.txtChatMessage.setText(chat.message);
        myChatViewHolder.txtUserAlphabet.setText(alphabet);

        if(mChats.get(position).type_mensaje.equals("2")){
            myChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            myChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
            myChatViewHolder.imgMensajeFoto.setVisibility(View.VISIBLE);

            Glide.with(c).load(mChats.get(position).urlFoto).into(myChatViewHolder.imgMensajeFoto);

        }else if(mChats.get(position).type_mensaje.equals("1")){
            myChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            myChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
            myChatViewHolder.imgMensajeFoto.setVisibility(View.GONE);
        }
    }

    private void configureOtherChatViewHolder(OtherChatViewHolder otherChatViewHolder, int position) {
        Chat chat = mChats.get(position);
        String alphabet = chat.sender.substring(0, 1);
        otherChatViewHolder.txtChatMessage.setText(chat.message);
        otherChatViewHolder.txtUserAlphabet.setText(alphabet);

        if(mChats.get(position).type_mensaje.equals("2")){
            otherChatViewHolder.txtChatMessage.setVisibility(View.GONE);
            otherChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
            otherChatViewHolder.imgMensajeFoto2.setVisibility(View.VISIBLE);

            Glide.with(c).load(mChats.get(position).urlFoto).into(otherChatViewHolder.imgMensajeFoto2);

        }else if(mChats.get(position).type_mensaje.equals("1")){
            otherChatViewHolder.txtChatMessage.setVisibility(View.VISIBLE);
            otherChatViewHolder.txtUserAlphabet.setVisibility(View.VISIBLE);
            otherChatViewHolder.imgMensajeFoto2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mChats != null) {
            return mChats.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (TextUtils.equals(mChats.get(position).senderUid,
                FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return VIEW_TYPE_ME;
        } else {
            return VIEW_TYPE_OTHER;
        }
    }

    private static class MyChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet;
        private ImageView imgMensajeFoto;

        public MyChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            imgMensajeFoto = (ImageView) itemView.findViewById(R.id.mensajeFoto1);
        }
    }

    private static class OtherChatViewHolder extends RecyclerView.ViewHolder {
        private TextView txtChatMessage, txtUserAlphabet;
        private ImageView imgMensajeFoto2;

        public OtherChatViewHolder(View itemView) {
            super(itemView);
            txtChatMessage = (TextView) itemView.findViewById(R.id.text_view_chat_message);
            txtUserAlphabet = (TextView) itemView.findViewById(R.id.text_view_user_alphabet);
            imgMensajeFoto2 = (ImageView) itemView.findViewById(R.id.mensajeFoto2);
        }
    }
}
