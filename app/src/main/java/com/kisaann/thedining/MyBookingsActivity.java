package com.kisaann.thedining;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kisaann.thedining.Common.Common;
import com.kisaann.thedining.Models.BookTableModel;
import com.kisaann.thedining.ViewHolders.BookTableViewHolder;

import io.paperdb.Paper;

public class MyBookingsActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    String restId;
    String phoneNo;
    FirebaseDatabase database;
    DatabaseReference bookRequestRestaurant;
    String userNo;
    FirebaseRecyclerAdapter<BookTableModel, BookTableViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookings);
        // init paper
        Paper.init(this);
        if (getIntent() != null){
            phoneNo = getIntent().getStringExtra("phoneNo");
            restId = getIntent().getStringExtra("restId");
        }
        // init paper
        Paper.init(this);

      //  restId = Paper.book().read(Common.RESTAURANT_ID);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Bookings");
        getSupportActionBar().setHomeButtonEnabled(true);

        // init firebase
        database = FirebaseDatabase.getInstance();
       // bookRequestRestaurant = database.getReference(restId+"_"+"BookRequests");
        bookRequestRestaurant = database.getReference("BookRequests");

        userNo = Paper.book().read(Common.USER_KEY);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_bookings);
        LinearLayoutManager layoutManage = new LinearLayoutManager(this);
        layoutManage.setReverseLayout(true);
        layoutManage.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManage);
        recyclerView.setHasFixedSize(true);
        /*layoutManager = new LinearLayoutManager(this);*/
        recyclerView.setLayoutManager(layoutManage);

        loadRequestedBookings();
    }
    private void loadRequestedBookings() {
        Query searchByUser = bookRequestRestaurant.orderByChild("phone").equalTo(userNo);
        FirebaseRecyclerOptions<BookTableModel> options = new FirebaseRecyclerOptions.Builder<BookTableModel>()
                .setQuery(searchByUser,BookTableModel.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<BookTableModel, BookTableViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookTableViewHolder viewHolder, int position, @NonNull BookTableModel model) {
                viewHolder.txtOrderId.setText(model.getBookId());
                viewHolder.txtOrderPnone.setText(model.getTableNo());
                viewHolder.txt_restName.setText(model.getRestaurantName());
                if (Common.convertCodeToBookStatus(model.getStatus()).equalsIgnoreCase("Pending")){
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.blue));
                }
                if (Common.convertCodeToBookStatus(model.getStatus()).equalsIgnoreCase("Confirm")){
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.green));
                }
                if (Common.convertCodeToBookStatus(model.getStatus()).equalsIgnoreCase("Cancel")){
                    viewHolder.txtOrderStatus.setTextColor(getResources().getColor(R.color.light_orange));
                }
                viewHolder.txtOrderStatus.setText(Common.convertCodeToBookStatus(model.getStatus()));
                viewHolder.order_date.setText(model.getBookDate()+" & "+model.getBookTime());

                viewHolder.btn_delete.setVisibility(View.GONE);
                viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getItem(position).getStatus().equals("0"))
                            cancelBooking(adapter.getRef(position).getKey());
                        else
                            Toast.makeText(MyBookingsActivity.this,"You cannot cancel this order!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public BookTableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.table_book_layout,parent,false);

                return new BookTableViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //fix click back button from food and don't see category
        if (adapter != null){
            adapter.startListening();
        }
    }
    private void cancelBooking(final String key) {
        bookRequestRestaurant.child(key)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MyBookingsActivity.this,new StringBuilder("Order ")
                                .append(key)
                                .append("has been deleted!").toString(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyBookingsActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
