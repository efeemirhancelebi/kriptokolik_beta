//package com.findik.kriptokolik;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//
//public class giris extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.giris_ekrani);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_Giris), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        TextView kayitText = findViewById(R.id.kayit);
//        kayitText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(giris.this, kayit.class);
//                startActivity(intent);
//            }
//        });
//
//        EditText tbox_mail = findViewById(R.id.tbox_mail);
//        EditText tbox_password = findViewById(R.id.tbox_password);
//        Button giris_button = findViewById(R.id.button_Giris);
//        tbox_mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    tbox_mail.setText("");
//                }
//            }
//        });
//
//        tbox_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    tbox_password.setText("");
//                }
//            }
//        });
//
//        giris_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = tbox_mail.getText().toString().trim();
//                String password = tbox_password.getText().toString().trim();
//                if (!email.isEmpty() && !password.isEmpty()) {
//                    signInUser(email, password);
//                } else {
//                    Toast.makeText(giris.this, "E-posta veya şifre kısmı boş bırakılamaz", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    FirebaseAuth mAuth = FirebaseAuth.getInstance();
//
//    private void signInUser(String email, String password) {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Giriş başarılı
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            // Giriş yapan kullanıcının bilgilerini al
//                            getUserFromDatabase(user);
//
//                            Button girisButton = findViewById(R.id.button_Giris);
//                            girisButton.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(giris.this, MainActivity.class);
//                                    startActivity(intent);
//                                }
//                            });
//
//                        } else {
//                            // Giriş başarısız oldu
//                            Toast.makeText(giris.this, "Giriş başarısız oldu. Lütfen bilgilerini kontrol et ve tekrar giriş yap.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void getUserFromDatabase(FirebaseUser user) {
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        database.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                User userObj = dataSnapshot.getValue(User.class);
//                // Kullanıcı bilgilerini kullan
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Hata durumunu yönet
//            }
//        });
//    }
//
//
//    public class User {
//        public String uid;
//        public String email;
//
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//        public User() {
//        }
//
//        public User(String uid, String email) {
//            this.uid = uid;
//            this.email = email;
//        }
//
//        // Getter ve setter yöntemleri
//        public String getUid() {
//            return uid;
//        }
//
//        public void setUid(String uid) {
//            this.uid = uid;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        private void getUserFromDatabase(FirebaseUser user) {
//            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//            database.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    User userObj = dataSnapshot.getValue(User.class);
//                    // Kullanıcı bilgilerini kullan
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Hata durumunu yönet
//                }
//            });
//        }
//    }
//}