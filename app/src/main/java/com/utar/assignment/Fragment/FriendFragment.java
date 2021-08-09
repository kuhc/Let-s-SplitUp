package com.utar.assignment.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.mbms.MbmsErrors;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.utar.assignment.Activity.AddFriend;
import com.utar.assignment.Activity.MainActivity;
import com.utar.assignment.Model.User;
import com.utar.assignment.R;
import com.utar.assignment.Util.FirebaseCallback;
import com.utar.assignment.Util.FirestoreHelper;
import com.utar.assignment.Util.GeneralHelper;

import java.util.ArrayList;
import java.util.List;


public class FriendFragment extends Fragment {

    private static FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private FirebaseAuth Auth;
    FirebaseFirestore db;
    RecyclerView recyclerView;
    private User userInfo;
    TextView username;
    TextView friendNo;
    List<String> userList = new ArrayList<>();
    View view;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);

        username = view.findViewById(R.id.friend_frag_uname);
        friendNo = view.findViewById(R.id.friend_no);

        FirebaseUser user;
        user = Auth.getInstance().getCurrentUser();
        String uid = user.getUid();

        fStore.collection("Users").document(uid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        User user1 = task.getResult().toObject(User.class);
                        String um = user1.getUsername();


                        username.setText(um);
                    }
                });

        Button button = view.findViewById(R.id.add_friend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFriend.class);
                startActivity(intent);
            }
        });

       /* FirestoreHelper.getUser(uid, new FirebaseCallback() {
            @Override
            public void onResponse(Object object) {


                recyclerView = view.findViewById(R.id.freindListRecycle);
                userInfo = (User)object;
                userList.addAll(userInfo.getFriendList());
                int fNo = userList.size();
                String fNo1 = Integer.toString(fNo);
                friendNo.setText(fNo1);
            }
        });*/

        db = FirebaseFirestore.getInstance();
        DocumentReference documentReference =db.collection("Users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                userInfo = documentSnapshot.toObject(User.class);
                if(userInfo.getFriendList()!= null) {
                    userList.addAll(userInfo.getFriendList());
                    int fNo = userList.size();
                    String fNo1 = Integer.toString(fNo);
                    friendNo.setText(fNo1);

                    initializeListView(userInfo, userList);
                }
                else
                    GeneralHelper.showMessage(getContext(),"There is no existing friend!");
                //initializeRecycleView(userList);
            }
        });

        return view;
    }

    public void initializeListView(User userInfo, List<String> userList) {

        FirebaseUser user;
        user = Auth.getInstance().getCurrentUser();
        String uid = user.getUid();

        listView = view.findViewById(R.id.friendList);
        ArrayAdapter arrayAdapter;
        ArrayList<String> email = new ArrayList<>(userList);
        arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, email);
        listView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

         listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            PopupMenu popupMenu = new PopupMenu(getContext(),view);
            popupMenu.getMenuInflater().inflate(R.menu.friend_menu,popupMenu.getMenu());

            GeneralHelper.showMessage(getContext(),"The unsettled amount with " + email.get(position) + " is " );

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {
                        case R.id.settle_bill:

                            GeneralHelper.showMessage(getContext(), "You have settled bill with " + email.get(position));

                            break;

                        case R.id.delete_friend:

                            List<String> newUserList = userList;
                            newUserList.remove(position);
                            GeneralHelper.showMessage(getContext(), "You have remove friend with email : " + email.get(position));

                            FirestoreHelper.addFriend(uid, newUserList, new FirebaseCallback() {
                                @Override
                                public void onResponse() {
                                    GeneralHelper.showMessage(getContext(), "Successfully Deleted!");

                                    Intent intent = new Intent(getContext(),MainActivity.class);
                                    startActivity(intent);

                                    arrayAdapter.notifyDataSetChanged();
                                }
                            });
                            break;


                    }
                    return true;
                }
            });
            popupMenu.show();

        }
    });
}

    /*private void initializeRecycleView(List<String> userList) {

        LinearLayoutManager layoutManager =
                //new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL,false);
                new LinearLayoutManager(getContext());

        recyclerView = (RecyclerView) view.findViewById(R.id.freindList);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new friendListAdapter(getContext(),userList);
        recyclerView.setAdapter(adapter);
        GeneralHelper.showMessage(getContext(),"The no of friend: " + userList);

    }*/

}