package com.kisaann.thedining;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.kisaann.thedining.Adapters.MessageAdapter;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Database.OderDatabase;
import com.kisaann.thedining.Models.ChatModel;
import com.kisaann.thedining.Models.Data;
import com.kisaann.thedining.Models.DataMessage;
import com.kisaann.thedining.Models.MyNewResponce;
import com.kisaann.thedining.Models.MyResponse;
import com.kisaann.thedining.Models.Order;
import com.kisaann.thedining.Models.Sender;
import com.kisaann.thedining.Models.Token;
import com.kisaann.thedining.Models.User;
import com.kisaann.thedining.Remote.APIService;
import com.kisaann.thedining.Service.Client;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageChatActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    /*FirebaseUser firebaseUser;*/
    DatabaseReference reference;

    Intent intent;
    ImageButton btn_send;
    EditText edt_send;

    MessageAdapter messageAdapter;
    List<ChatModel> mChar;
    RecyclerView recyclerView;
    String userId;

    ValueEventListener seenListener;
    String ownerNo;
    APIService mService ;
    APIService apiService ;
    boolean notify = false;
    String chatFrom ;
    List<Order> cartConfirmOrder = new ArrayList<>();
    Double total = 0.0;
    NumberFormat fmt;
    String orderItemMsg = "";
    String orderId;
    String userPhone;
    String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        if (getIntent() != null){
            chatFrom = getIntent().getStringExtra("from");
        }
        if (chatFrom.equalsIgnoreCase("Owner")){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(" Chat with Restaurant");
            getSupportActionBar().setHomeButtonEnabled(true);
        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(" Chat with Customer Support");
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // init paper
        Paper.init(this);
        // init service
        mService = Common.getFCMService();

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        recyclerView = findViewById(R.id.recycler_vicw);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
        /*userId = intent.getStringExtra("userId");*/
        orderId = Paper.book().read(Common.ORDER_ID);
        if (chatFrom.equalsIgnoreCase("Owner")){
            userId = Paper.book().read(Common.OWNER_NO);
            Log.e("OWNER_NO",""+userId);
        }else {
            userId = "8886666848";
        }
        userPhone = Paper.book().read(Common.USER_KEY);
        userName = Paper.book().read(Common.USER_NAME);
        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        edt_send = findViewById(R.id.edt_send);
        btn_send = findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String msg = edt_send.getText().toString().trim();
                if (!msg.equals("")){
                    sendMessage(userPhone,userId,msg);
                }else {
                    Toast.makeText(MessageChatActivity.this, "You con't send empty message", Toast.LENGTH_SHORT).show();
                }
                edt_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getFirstName());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

                readMessages(userPhone, userId, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userId);

        cartConfirmOrder = new OderDatabase(this).getOrderCart(userPhone);
        if (cartConfirmOrder.size() > 0){
            for (Order order : cartConfirmOrder) {

                Double original_price = Double.parseDouble(order.getPrice());
                Double discount = Double.parseDouble(order.getDiscount()+".0");
                Double discount_price = (original_price / 100.0f) * discount;
                Double servicePrice = Double.parseDouble(order.getPrice()) - discount_price;
                String str_price = "" + new DecimalFormat("##.##").format(servicePrice);

                total += (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
                Locale locale = new Locale("en", "IN");
                fmt = NumberFormat.getCurrencyInstance(locale);
                //  txt_totalPrice.setText(fmt.format(total));
                Double price = (Double.parseDouble(str_price)) * (Integer.parseInt(order.getQuantity()));
                orderItemMsg = orderItemMsg+order.getProductName()+" Qty:"+order.getQuantity()+" Price:"+price+"\n";
            }
            notify = true;
            orderItemMsg = "Order Id: "+orderId+"\n"+orderItemMsg+"\n"+" Total : "+fmt.format(total);
            sendMessage(userPhone,userId,orderItemMsg);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void seenMessage(String userId){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(userPhone) && chatModel.getSender().equals(userId)){
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sendMessage(String sender, String receiver, String message) {

        reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isSeen",false);

        reference.child("Chats").push().setValue(hashMap);

        // add user to chat fragment
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(userPhone)
                .child(userId);
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // add user to chat fragment
        DatabaseReference chatRefA = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(userId)
                .child(userPhone);
        chatRefA.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatRefA.child("id").setValue(userPhone);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userPhone);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    if (chatFrom.equalsIgnoreCase("Owner")){
                        sendChatNotificationToOwner(sender,receiver, user.getFirstName(), msg);
                    }else {
                        sendChatNotificationToAdmin(sender,receiver, user.getFirstName(), msg);
                    }

                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void sendChatNotificationToOwner(String sender, String receiver, String name, String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        /* DatabaseReference tokens = database.getReference("Tokens");*/
        Query data = tokens.orderByChild("phone").equalTo(receiver);//get all node with
        // isSercertoken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    // create raw payload to send
                    /*Notification notification = new Notification("You have a new order" +
                            order_number, "Foody Mail");
                    Sender content = new Sender(serverToken.getToken(), notification);*/
                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title","New Message");
                    dataSend.put("message","You have a new message from : "+userName+" : " + message+"<?>"+sender+"<?>"+"Chat"+"<?>"+".");
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);

                    String text = new Gson().toJson(dataMessage);
                    Log.d("Content",text);

                    mService.sendNotification(dataMessage)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    // only run when get result
                                    // issue notification video 22
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            /*Toast.makeText(MessageChatActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();*/
                                        } else {
                                            Toast.makeText(MessageChatActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void sendChatNotificationToAdmin(String sender, String receiver, String name, String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        /* DatabaseReference tokens = database.getReference("Tokens");*/
        Query data = tokens.orderByChild("phone").equalTo(receiver);//get all node with
        // isSercertoken is true
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
                    // create raw payload to send
                    /*Notification notification = new Notification("You have a new order" +
                            order_number, "Foody Mail");
                    Sender content = new Sender(serverToken.getToken(), notification);*/
                    Map<String,String> dataSend = new HashMap<>();
                    dataSend.put("title","New Message");
                    dataSend.put("message","You have a new message from : "+userName+" : " + message+"<?>"+sender+"<?>"+"ChatUser"+"<?>"+".");
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(),dataSend);

                    String text = new Gson().toJson(dataMessage);
                    Log.d("Content",text);

                    mService.sendNotification(dataMessage)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    // only run when get result
                                    // issue notification video 22
                                    if (response.code() == 200) {
                                        if (response.body().success == 1) {
                                            /*Toast.makeText(MessageChatActivity.this, "Thank you, Order Please ", Toast.LENGTH_SHORT).show();*/
                                        } else {
                                            Toast.makeText(MessageChatActivity.this, "Failed !!!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                    Log.e("ERROR", t.getMessage());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void sendNotification(String receiver, String name, String messge){
        ownerNo = Paper.book().read(Common.OWNER_NO);
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        /* Query query = tokens.orderByKey().equalTo(receiver);*/
        Query query = tokens.orderByChild("phone").equalTo(receiver);//get all node with
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(userPhone, R.mipmap.ic_launcher, name+": "+messge,"New Message",userId,"Chat");

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNewNotification(sender)
                            .enqueue(new Callback<MyNewResponce>() {
                                @Override
                                public void onResponse(Call<MyNewResponce> call, Response<MyNewResponce> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(MessageChatActivity.this, "Failed ! ", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyNewResponce> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void readMessages(String myId, String userId, String imageUrl){
        mChar = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChar.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(myId) && chatModel.getSender().equals(userId)
                            || chatModel.getReceiver().equals(userId) && chatModel.getSender().equals(myId)){
                        mChar.add(chatModel);
                    }
                    messageAdapter = new MessageAdapter(MessageChatActivity.this, mChar, imageUrl, userPhone);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userPhone);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    private void currentUser(String userId){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentUser", userId);
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }
}
