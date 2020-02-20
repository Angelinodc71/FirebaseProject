package com.example.myapplicationfragmentslegacy_20;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private EditText emailRegisterEditText, passwordRegisterEditText;

    private Button registerButton;
    private FirebaseAuth mAuth;
    private NavController navController;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // ...
        mAuth = FirebaseAuth.getInstance();
        navController = Navigation.findNavController(view);


        emailRegisterEditText = view.findViewById(R.id.emailRegisterEditText);
        passwordRegisterEditText = view.findViewById(R.id.passwordRegisterEditText);

        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crearCuenta();
            }
        });
    }
    private void crearCuenta() {
        if (!validarFormulario()) {
            return;
        }

        registerButton.setEnabled(false);

        mAuth.createUserWithEmailAndPassword(emailRegisterEditText.getText().toString(), passwordRegisterEditText.getText().toString())
                .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            actualizarUI(mAuth.getCurrentUser());
                        } else {
                            Snackbar.make(requireView(), "Error: " + task.getException(), Snackbar.LENGTH_LONG).show();

                        }
                        registerButton.setEnabled(true);
                    }
                });

    }

    private void actualizarUI(FirebaseUser currentUser) {
        if(currentUser != null){
            navController.navigate(R.id.chooseImageFragment);
        }
    }

    private boolean validarFormulario() {
        boolean valid = true;

        if (TextUtils.isEmpty(emailRegisterEditText.getText().toString())) {
            emailRegisterEditText.setError("Required.");
            valid = false;
        } else {
            emailRegisterEditText.setError(null);
        }

        if (TextUtils.isEmpty(passwordRegisterEditText.getText().toString())) {
            passwordRegisterEditText.setError("Required.");
            valid = false;
        } else {
            passwordRegisterEditText.setError(null);
        }

        return valid;
    }
}
