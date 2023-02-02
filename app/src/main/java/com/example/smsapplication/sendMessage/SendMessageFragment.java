package com.example.smsapplication.sendMessage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smsapplication.GroupModel;
import com.example.smsapplication.MessageModel;
import com.example.smsapplication.R;
import com.example.smsapplication.addMember.GroupAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class SendMessageFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    RecyclerView rv_groupSendMessage, rv_messageSendMessage;
    TextView sendMessage_selectedGroup, sendMessage_selectedMessage;
    Button bt_sendMessage;

    ArrayList<GroupModel> groupModelList;
    ArrayList<MessageModel>messageModelList;

    GroupModel selectedGroup;
    MessageModel selectedMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_message, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        rv_groupSendMessage = view.findViewById(R.id.rv_groupSendMessage);
        rv_messageSendMessage = view.findViewById(R.id.rv_messageSendMessage);
        sendMessage_selectedGroup = view.findViewById(R.id.sendMessage_selectedGroup);
        sendMessage_selectedMessage = view.findViewById(R.id.sendMessage_selectedMessage);
        bt_sendMessage = view.findViewById(R.id.bt_sendMessage);

        groupModelList = new ArrayList<>();
        messageModelList = new ArrayList<>();

        ActivityResultLauncher launcher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGrant -> {
            if (isGrant) {
                sendSMS();
            } else {
                Toast.makeText(getContext(), "Sms gönderme izni gerekli", Toast.LENGTH_SHORT).show();
            }
        });

        bt_sendMessage.setOnClickListener(v->{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                launcher.launch(Manifest.permission.SEND_SMS);
            }
            else {
                sendSMS();
            }
        });


        fetchGroups();
        fetchMessages();
        return view;
    }

    private void fetchGroups(){
        String uid = mAuth.getCurrentUser().getUid();

        mStore.collection("users").document(uid).collection("groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("groupName"), documentSnapshot.getString("groupDescp"),
                        documentSnapshot.getString("groupImage"),(List<String>)documentSnapshot.get("numbers"), documentSnapshot.getId());
                groupModelList.add(groupModel);
            }
            rv_groupSendMessage.setAdapter(new GroupAdapter(groupModelList, position -> {
                selectedGroup = groupModelList.get(position);
                sendMessage_selectedGroup.setText("Seçili Grup"+selectedGroup.getGroupName());
            }));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
            rv_groupSendMessage.setLayoutManager(linearLayoutManager);

        });
    }
    private void fetchMessages() {
        String uid = mAuth.getCurrentUser().getUid();

        mStore.collection("users").document(uid).collection("messages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            messageModelList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                MessageModel messageModel = new MessageModel(documentSnapshot.getString("messageName"), documentSnapshot.getString("messageDescp"), documentSnapshot.getId());
                messageModelList.add(messageModel);
            }
            rv_messageSendMessage.setAdapter(new MessageAdapter(messageModelList, position -> {
                selectedMessage = messageModelList.get(position);
                sendMessage_selectedMessage.setText("Seçili Mesaj" + selectedMessage.getMessageName());
            }));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rv_messageSendMessage.setLayoutManager(linearLayoutManager);
        });
    }
    private void sendSMS() {
        if (selectedGroup == null && selectedMessage == null) {
            Toast.makeText(getContext(), "Lütfen grup ve mesaj seçiniz!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedGroup.getNumbers() == null && selectedGroup.getNumbers().size() == 0) {
            SmsManager smsManager = SmsManager.getDefault();
            for (String number : selectedGroup.getNumbers()) {
                smsManager.sendTextMessage(number, null, selectedMessage.getMessageDescp(), null, null);
            }
            Toast.makeText(getContext(), "Mesajlar gönderildi!", Toast.LENGTH_SHORT).show();

        }


    }
}