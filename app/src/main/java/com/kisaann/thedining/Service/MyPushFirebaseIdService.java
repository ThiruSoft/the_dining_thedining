package com.kisaann.thedining.Service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.Token;

public class MyPushFirebaseIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String tokenRefreshed = FirebaseInstanceId.getInstance().getToken();
        if (Common.currentUser != null)
            updateTokenToFirebase(tokenRefreshed);
    }
    private void updateTokenToFirebase(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token,"false",Common.currentUser.getPhoneNo()); // false bcz token" // false bcz token send from client app
        tokens.child(Common.currentUser.getPhoneNo()).setValue(token);

    }
}
