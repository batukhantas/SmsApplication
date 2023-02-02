package com.example.smsapplication.addMember;

import android.os.Bundle;
import android.os.Build;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smsapplication.ContactModel;
import com.example.smsapplication.GroupModel;
import com.example.smsapplication.R;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AddMemberFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;

    RecyclerView rv_groupAddMember, rv_contactAddMember;
    TextView addMember_groupName;

    GroupModel selectedGroup;
    ArrayList<GroupModel> groupModelList;
    ArrayList<ContactModel> contactModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        rv_groupAddMember = view.findViewById(R.id.rv_groupAddMember);
        rv_contactAddMember = view.findViewById(R.id.rv_contactAddMember);
        addMember_groupName = view.findViewById(R.id.addMember_groupName);

        groupModelList = new ArrayList<>();
        contactModelList = new ArrayList<>();

        ActivityResultLauncher launcher=registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGrant -> {
            if(isGrant) {
                fetchContact();
            }else{
                Toast.makeText(getContext(), "Rehber izni gereklidir!", Toast.LENGTH_SHORT).show();
            }
        });

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED){
                launcher.launch(Manifest.permission.READ_CONTACTS);
            }
        else {
            fetchContact();
        }


        fetchGroups();

        return view;
    }

    private void fetchGroups(){
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/users/" + userId + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("groupName"), documentSnapshot.getString("groupDescp"),
                        documentSnapshot.getString("groupImage"), (List<String>)documentSnapshot.get("numbers"), documentSnapshot.getId());
                groupModelList.add(groupModel);
            }
            rv_groupAddMember.setAdapter(new GroupAdapter(groupModelList, position -> {
                selectedGroup = groupModelList.get(position);
                addMember_groupName.setText(selectedGroup.getGroupName());

            }));

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            rv_groupAddMember.setLayoutManager(linearLayoutManager);
        });
    }
    private void fetchContact(){
        Cursor cursor = getContext().getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        contactModelList.clear();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER));
            @SuppressLint("Range") String photo = cursor.getString(cursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            ContactModel contactModel = new ContactModel(name, phoneNumber, photo);
            contactModelList.add(contactModel);


        }
        rv_contactAddMember.setAdapter(new ContactAdapter(contactModelList, position -> {
            ContactModel contactModel = contactModelList.get(position);
            if (selectedGroup != null){
                new AlertDialog.Builder(getContext())
                        .setTitle("Kişiyi Gruba Ekle")
                        .setMessage(contactModel.getName() + " adlı kişiyi " + selectedGroup.getGroupName() + " grubuna eklemek istediğinize emin misiniz?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                mStore.collection("/users/" + mAuth.getCurrentUser().getUid() + "/groups").document(selectedGroup.getGroupId()).update(new HashMap<String, Object>(){{
                        put("numbers", FieldValue.arrayUnion(contactModel.getNumber()));

                }}).addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Kişi başarıyla gruba eklendi!", Toast.LENGTH_SHORT).show();
                });

               })
                        .setNegativeButton("Hayır", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
                Toast.makeText(getContext(), contactModel.getName() + " " +contactModel.getNumber(),Toast.LENGTH_SHORT).show();

    }
        }));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_contactAddMember.setLayoutManager(linearLayoutManager);
    }
}