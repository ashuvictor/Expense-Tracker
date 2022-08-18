package com.example.expensetracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.expensetracker.databinding.ActivityAddTransactionBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {
ActivityAddTransactionBinding binding;
String type="";
FirebaseFirestore fireStore;
FirebaseAuth firebaseAuth;
FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fireStore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        binding.expenseCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Expense";
                binding.expenseCheck.setChecked(true);
                binding.incomeCheck.setChecked(false);
            }
        });
        binding.incomeCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type="Income";
                binding.incomeCheck.setChecked(true);
                binding.expenseCheck.setChecked(false);
            }
        });
        binding.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
String amount=binding.userAmount.getText().toString().trim();
String note=binding.userNoteAdd.getText().toString().trim();
if(amount.length()<=0)
{
    return;
}
if(type.length()<=0)
{
    Toast.makeText(AddTransactionActivity.this,"Select the the type of transaction",Toast.LENGTH_SHORT).show();
}
                SimpleDateFormat sdf=new SimpleDateFormat("dd mm yyyy", Locale.getDefault());
String currentDateTime=sdf.format(new Date());
                String id= UUID.randomUUID().toString();
                Map<String,Object>transaction=new HashMap<>();
transaction.put("id",id);
transaction.put("amount",amount);
transaction.put("note",note);
transaction.put("type",type);
transaction.put("date",currentDateTime);
fireStore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
    @Override
    public void onSuccess(Void unused) {
        Toast.makeText(AddTransactionActivity.this,"Added",Toast.LENGTH_SHORT).show();
        binding.userNoteAdd.setText("");
        binding.userAmount.setText("");
    }
}).addOnFailureListener(new OnFailureListener() {
    @Override
    public void onFailure(@NonNull Exception e) {
        Toast.makeText(AddTransactionActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
    }
});

            }
        });
    }
}