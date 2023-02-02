package com.example.smsapplication.createGroup;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smsapplication.GroupModel;
import com.example.smsapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CreateGroupFragment extends Fragment {

    EditText group_Name, group_Descp;
    RecyclerView rv_groupCreate;
    Button btn_groupCreate;
    ImageView group_Image;

    FirebaseAuth mAuth;
    FirebaseFirestore mStore;
    FirebaseStorage mStorage;

    Uri filePath;

    ArrayList<GroupModel> groupModelArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_group, container, false);

        group_Name = view.findViewById(R.id.group_Name);
        group_Descp = view.findViewById(R.id.group_Descp);
        rv_groupCreate = view.findViewById(R.id.rv_groupCreate);
        btn_groupCreate = view.findViewById(R.id.btn_groupCreate);
        group_Image = view.findViewById(R.id.group_Image);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();


        groupModelArrayList = new ArrayList<>();
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                filePath = result.getData().getData();
                group_Image.setImageURI(filePath);
            }
        });
        group_Image.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(intent);
        });

        btn_groupCreate.setOnClickListener(v -> {
            String name = group_Name.getText().toString();
            String description = group_Descp.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(getContext(), "Grup adı boş bırakılmamalı", Toast.LENGTH_SHORT).show();
                return;

            }
            if (description.isEmpty()) {
                Toast.makeText(getContext(), "Grup açıklaması boş bırakılmamalı", Toast.LENGTH_SHORT).show();
                return;

            }

            if (filePath != null) {
                StorageReference storageReference = mStorage.getReference().child("images" + UUID.randomUUID().toString());
                storageReference.putFile(filePath).addOnSuccessListener(taskSnapshot -> {
                    storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Toast.makeText(getContext(), "Resim başarıyla yüklendi", Toast.LENGTH_SHORT).show();
                        createGroup(name, description, imageUrl);
                    });
                });
            } else {
                createGroup(name, description, null);
            }
        });
        FetchGroup();

        return view;

    }

    private void createGroup(String name, String description, String imageUrl) {
        String userId = mAuth.getCurrentUser().getUid();

        mStore.collection("/users" + userId + "/groups").add(new HashMap<String, Object>() {
            {
                put("grupAdi", name);
                put("grupAciklamasi", description);
                put("grupResmi", imageUrl);
                put("grupNumaralar", new ArrayList<String>());
            }
        }).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Grup Başarıyla Oluşturuldu", Toast.LENGTH_SHORT).show();
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                GroupModel groupModel = new GroupModel(name, description, imageUrl, (List<String>)documentSnapshot.get("grupNumaralar"), documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
                rv_groupCreate.getAdapter().notifyItemInserted(groupModelArrayList.size() - 1);

            });

        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Grup Oluşturulamadı", Toast.LENGTH_SHORT).show();
        });
    }

    private void FetchGroup() {
        String userId = mAuth.getCurrentUser().getUid();
        mStore.collection("/users" + userId + "/groups").get().addOnSuccessListener(queryDocumentSnapshots -> {
            groupModelArrayList.clear();
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                GroupModel groupModel = new GroupModel(documentSnapshot.getString("grupAdi"), documentSnapshot.getString("grupAciklamasi"),
                        documentSnapshot.getString("grupResmi"), (List<String>)documentSnapshot.get("grupNumaralar"), documentSnapshot.getId());
                groupModelArrayList.add(groupModel);
            }

            rv_groupCreate.setAdapter(new GroupAdapter(groupModelArrayList));
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            rv_groupCreate.setLayoutManager(linearLayoutManager);

        });
    }
}

