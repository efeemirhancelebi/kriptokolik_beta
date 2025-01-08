package com.findik.kriptokolik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class coin_ekleme_ekrani extends AppCompatActivity {

    private DBHelper dbHelper;
    private EditText miktar;
    private TextView coinAdi;
    private Button satButonu, alButonu, iptalButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.coin_ekleme_ekrani);

        // Sistem kenarlıkları vs.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coin_ekleme_ekrani), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // DBHelper
        dbHelper = new DBHelper(this);

        // Ekrandaki bileşenler
        miktar = findViewById(R.id.miktar);
        coinAdi = findViewById(R.id.coinName);
        satButonu = findViewById(R.id.satBtn);
        alButonu = findViewById(R.id.alBtn);
        iptalButton = findViewById(R.id.iptal);

        String selectedItem = getIntent().getStringExtra("secilenItem");
        if (selectedItem != null) {
            coinAdi.setText(selectedItem);
        }

        miktar.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                miktar.setText("");
            }
        });

        satButonu.setOnClickListener(view -> {
            String girilenMiktarStr = miktar.getText().toString().trim();
            String coinName = coinAdi.getText().toString().trim();

            if (girilenMiktarStr.isEmpty()) {
                Toast.makeText(this, "Lütfen miktar giriniz!", Toast.LENGTH_SHORT).show();
                return;
            }

            double girilenMiktar = Double.parseDouble(girilenMiktarStr);
            coinName = coinName.toUpperCase();

            boolean success = dbHelper.insertOrUpdateCoin(coinName, girilenMiktar, true);
            if (success) {
                Toast.makeText(this, "Satış kaydedildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kayıt hatası!", Toast.LENGTH_SHORT).show();
            }

            // Ana sayfaya dön
            startActivity(new Intent(coin_ekleme_ekrani.this, MainActivity.class));
            finish();
        });

        // AL Butonu
        alButonu.setOnClickListener(view -> {
            // EditText'ten metin -> double
            String girilenMiktarStr = miktar.getText().toString().trim();
            String coinName = coinAdi.getText().toString().trim();

            if (girilenMiktarStr.isEmpty()) {
                Toast.makeText(this, "Lütfen miktar giriniz!", Toast.LENGTH_SHORT).show();
                return;
            }

            double girilenMiktar = Double.parseDouble(girilenMiktarStr);
            // Coin adını büyük harf
            coinName = coinName.toUpperCase();

            // Al (isSell=false)
            boolean success = dbHelper.insertOrUpdateCoin(coinName, girilenMiktar, false);
            if (success) {
                Toast.makeText(this, "Alış kaydedildi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Kayıt hatası!", Toast.LENGTH_SHORT).show();
            }

            // Ana sayfaya dön
            startActivity(new Intent(coin_ekleme_ekrani.this, MainActivity.class));
            finish();
        });

        // İPTAL Butonu
        iptalButton.setOnClickListener(v -> {
            startActivity(new Intent(coin_ekleme_ekrani.this, MainActivity.class));
        });
    }
}
