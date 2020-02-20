package com.example.myapplicationfragmentslegacy_20;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.myapplicationfragmentslegacy_20.Model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseImageFragment extends Fragment {

    Button buttonsetPic;
    ImageView imageView;
    NavController navController;
    MainActivity mainActivity = new MainActivity();

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage mstore;
    public ChooseImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        buttonsetPic = view.findViewById(R.id.button_choose_photo);
        imageView = view.findViewById(R.id.imageViewPreview);

        buttonsetPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarFoto();
            }
        });


    }

    public void cargarFoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(Intent.createChooser(intent,"Seleccione la aplicacion"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            final Uri path = data.getData();

            Glide.with(requireView()).load(path).into(imageView);

            FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser()+"AccountImage.jpg")
                    .putFile(path)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            return task.getResult().getStorage().getDownloadUrl();
                        }
                    })
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            db.collection("users")
                                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(),uri.toString())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mainActivity.setStoreState(true);
                                    navController.navigate(R.id.homeFragment);
                                }
                            });
                        }
                    });
        }
    }
//    private void guardarEnFirestore(Uri pathImage) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        User currentUser = new User(user.getEmail(), pathImage.toString());
//
//        FirebaseFirestore.getInstance().collection("users")
//                .add(currentUser)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        navController.popBackStack();
//                    }
//                });
//    }
}
