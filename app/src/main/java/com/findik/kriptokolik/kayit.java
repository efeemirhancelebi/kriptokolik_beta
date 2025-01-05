//package com.findik.kriptokolik;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
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
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class kayit extends AppCompatActivity {
//
//    private EditText name;
//    private EditText surname;
//    private EditText username;
//    private EditText eposta;
//    private EditText sifre;
//    private Button kayit_button;
//    private FirebaseAuth mAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.kayit_ekrani);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_Kayit), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // EditText ve Button'ları burada tanımlayın
//        name = findViewById(R.id.tbox_ad);
//        surname = findViewById(R.id.tbox_soyad);
//        username = findViewById(R.id.tbox_kullanici_adi);
//        eposta = findViewById(R.id.tbox_mail);
//        sifre = findViewById(R.id.tbox_sifre);
//        kayit_button = findViewById(R.id.button_Kayit);
//
//        // FirebaseAuth örneğini başlatma
//        mAuth = FirebaseAuth.getInstance();
//
//        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    name.setText("");
//                }
//            }
//        });
//
//        surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    surname.setText("");
//                }
//            }
//        });
//
//        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    username.setText("");
//                }
//            }
//        });
//
//        eposta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    eposta.setText("");
//                }
//            }
//        });
//
//        sifre.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    sifre.setText("");
//                }
//            }
//        });
//
//        kayit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String adi = name.getText().toString().trim();
//                String soyadi = surname.getText().toString().trim();
//                String kullaniciAdi = username.getText().toString().trim();
//                String email = eposta.getText().toString().trim();
//                String password = sifre.getText().toString().trim();
//
//                if (!adi.isEmpty() && !soyadi.isEmpty() && !kullaniciAdi.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
//                    createUser(adi, soyadi, kullaniciAdi, email, password);
//                } else {
//                    Toast.makeText(kayit.this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//
//    public class User {
//        public String uid;
//        public String ad;
//        public String soyad;
//        public String kullaniciAdi;
//        public String email;
//
//        // Default constructor required for calls to DataSnapshot.getValue(User.class)
//        public User() {
//        }
//
//        public User(String uid, String ad, String soyad, String kullaniciAdi, String email) {
//            this.uid = uid;
//            this.ad = ad;
//            this.soyad = soyad;
//            this.kullaniciAdi = kullaniciAdi;
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
//        public String getAd() {
//            return ad;
//        }
//
//        public void setAd(String ad) {
//            this.ad = ad;
//        }
//
//        public String getSoyad() {
//            return soyad;
//        }
//
//        public void setSoyad(String soyad) {
//            this.soyad = soyad;
//        }
//
//        public String getKullaniciAdi() {
//            return kullaniciAdi;
//        }
//
//        public void setKullaniciAdi(String kullaniciAdi) {
//            this.kullaniciAdi = kullaniciAdi;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//    }
//
//    private void createUser(String ad, String soyad, String kullaniciAdi, String email, String sifre) {
//        mAuth.createUserWithEmailAndPassword(email, sifre)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) { // Kullanıcı başarıyla oluşturuldu
//                            FirebaseUser user = mAuth.getCurrentUser(); // Kullanıcı bilgilerini veritabanına kaydet
//                            saveUserToDatabase(user, ad, soyad, kullaniciAdi);
//                            Toast.makeText(kayit.this, "Kayıdınız başarıyla tamamlandı. Lütfen giriş yapın.", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(kayit.this, giris.class);
//                            startActivity(intent);
//
//                        } else { // Kayıt işlemi başarısız oldu
//                            Toast.makeText(kayit.this, "Kayıt işlemi başarısız.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//
//    private void saveUserToDatabase(FirebaseUser user, String ad, String soyad, String kullaniciAdi) {
//        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
//        User userObj = new User(user.getUid(), ad, soyad, kullaniciAdi, user.getEmail());
//        database.child("users").child(user.getUid()).setValue(userObj)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) { // Kullanıcı bilgileri başarıyla kaydedildi
//                            Toast.makeText(kayit.this, "Kullanıcı başarıyla kaydedildi.", Toast.LENGTH_SHORT).show();
//                        }
//                        else { // Kullanıcı bilgileri kaydedilemedi
//                            Toast.makeText(kayit.this, "Kullanıcı bilgileri kaydedilemedi.", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }
//}
