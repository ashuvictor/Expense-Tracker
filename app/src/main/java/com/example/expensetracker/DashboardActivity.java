package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.expensetracker.databinding.ActivityDashboardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity {
ActivityDashboardBinding binding;
FirebaseFirestore firebaseFirestore;
FirebaseAuth firebaseAuth;
ArrayList<TransactionModel>transactionModelArrayList;
TransactionAdapter transactionAdapter;
int sumExpense=0;
int sumIncome=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        transactionModelArrayList=new ArrayList<>();

        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(DashboardActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
        binding.logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignOutDialog();
            }

            private void createSignOutDialog() {

                AlertDialog.Builder builder=new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Delete").setMessage("Are you sure you want to sign out?").setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                firebaseAuth.signOut();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
            builder.create().show();
            }
        });
        binding.addFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    startActivity(new Intent(DashboardActivity.this,AddTransactionActivity.class));
                }
                catch (Exception e){

                }
            }
        });
        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
startActivity(new Intent(DashboardActivity.this,DashboardActivity.class));
finish();
                }
                catch (Exception e)
                {

                }
            }
        });
        loadData();
    }

    private void loadData() {


        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot ds: task.getResult())
                {
                    TransactionModel model=new TransactionModel(
                            ds.getString("id"),ds.getString("note")
                            ,ds.getString("amount"),ds.getString("type"),ds.getString("date")
                    );
                    int amount =Integer.parseInt(ds.getString("amount"));
                    if(ds.getString("type").equals("Expense"))
                    {
                        sumExpense+=amount;
                    }
                    else
                    {
                        sumIncome+=amount;
                    }
                    transactionModelArrayList.add(model);
                }
                binding.totalIncome.setText(String.valueOf(sumIncome));
                binding.totalExpense.setText(String.valueOf(sumExpense));
                binding.balance.setText(String.valueOf(sumIncome-sumExpense));
                transactionAdapter=new TransactionAdapter(DashboardActivity.this,transactionModelArrayList);
                binding.historyRecyclerView.setAdapter(transactionAdapter);
            }
        });
    }
}