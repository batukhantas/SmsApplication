package com.example.smsapplication.createMessage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smsapplication.MessageModel;
import com.example.smsapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;


public class CreateMessageFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    EditText messageName, messageDescp;
    Button createMessageBT;
    RecyclerView messageCreateRV;

    ArrayList<MessageModel> messageModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_message, container, false);

        messageName = view.findViewById(R.id.message_Name);
        messageDescp = view.findViewById(R.id.message_Descp);
        createMessageBT = view.findViewById(R.id.bt_messageCreate);
        messageCreateRV = view.findViewById(R.id.rv_messageCreate);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        messageModelList = new ArrayList<>();

        createMessageBT.setOnClickListener(v -> {
            String messageNameText = messageName.getText().toString();
            String messageText = messageDescp.getText().toString();

            if(messageNameText.isEmpty()){
                Toast.makeText(getContext(),"Mesaj adı boş bırakılmamalıdır.", Toast.LENGTH_SHORT).show();
                return;
            }
            if(messageText.isEmpty()){
                Toast.makeText(getContext(),"Mesaj alanı boş bırakılmamalıdır.", Toast.LENGTH_SHORT).show();
                return;
            }
            createMessage(messageNameText, messageText);

        });
        fetchMessages();

        return view;
    }

    private void createMessage(String messageNameText, String messageText) {
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/users/" + userId + "/messages").add(new HashMap<String, String>() {{
                    put("messageName", messageNameText);
                    put("messageText", messageText);
        }}).addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Mesaj başarıyla oluşturuldu.", Toast.LENGTH_SHORT).show();

                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        MessageModel messageModel = new MessageModel(messageNameText, messageText, documentSnapshot.getId());
                        messageModelList.add(messageModel);
                        messageCreateRV.getAdapter().notifyItemInserted(messageModelList.size() - 1);

                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Mesaj oluşturulamadı.", Toast.LENGTH_SHORT).show();
                });
    }

    private void fetchMessages(){
        String userId = mAuth.getCurrentUser().getUid();
        mStore.collection("/users/" + userId + "/messages").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                     messageModelList.clear();
                     for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                          MessageModel messageModel = new MessageModel(documentSnapshot.getString("messageName"), documentSnapshot.getString("messageText"), documentSnapshot.getId());
                          messageModelList.add(messageModel);
            }
                     messageCreateRV.setAdapter(new MessageAdapter(messageModelList));
                     LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                     messageCreateRV.setLayoutManager(linearLayoutManager);
        });
    }

}