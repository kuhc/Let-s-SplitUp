package com.utar.assignment.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.utar.assignment.R;

import java.util.ArrayList;


public class FriendFragment extends Fragment {

    private FirebaseAuth Auth;
    TextView username;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        FirebaseUser user;
        user = Auth.getInstance().getCurrentUser();
        String name = user.getDisplayName();

        username = view.findViewById(R.id.friend_frag_uname);
        username.setText(name);

       return view;
    }
}