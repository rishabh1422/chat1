package com.example.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {
    EditText username,/* fullname,*/ email, password;
    Button button;
    TextView tv;
    FirebaseAuth auth;
    ProgressDialog pd;
     DatabaseReference reference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        username = findViewById(R.id.username);
       // fullname = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button = findViewById(R.id.register);
        tv = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please Wait....");
                pd.show();
                String str = username.getText().toString();
               // String str1 = fullname.getText().toString();
                String st2 = email.getText().toString();
                String st3 = password.getText().toString();
                if (TextUtils.isEmpty(str) || /*TextUtils.isEmpty(str1) ||*/ TextUtils.isEmpty(st2) || TextUtils.isEmpty(st3)) {
                    Toast.makeText(RegisterActivity.this, "All feilds are requred", Toast.LENGTH_SHORT).show();
                } else if (st3.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Password Must have 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    Register(str,/*str1,*/st2,st3);

                }
            }
        });


}



    private void     Register(final String username , /*final String fullname, */String email, String password  )
       {
           auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                   if(task.isSuccessful())
                   {
                       FirebaseUser firebaseUser=auth.getCurrentUser();
                       String userid=firebaseUser.getUid();
                       reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
                       HashMap<String , Object> hashMap=new HashMap<>();
                       hashMap.put("ID ",userid);
                       hashMap.put("Username ",username);
                       //hashMap.put("FullName", fullname);
                      // hashMap.put("Bio ", "");
                       hashMap.put("ImageUrl ","default" );
                       reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if(task.isSuccessful())
                               {
                                   pd.dismiss();
                                   Intent intent=new Intent(RegisterActivity.this,Main2Activity.class);
                                   intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(intent);
                               }

                           }
                       });


                   }
                   else
                   {
                       pd.dismiss();
                       Toast.makeText(RegisterActivity.this, "You can't Register ", Toast.LENGTH_SHORT).show();
                   }

               }
           });

    }
}
