package com.findik.kriptokolik;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class coin_ekleme_ekrani extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.coin_ekleme_ekrani);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coin_ekleme_ekrani), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView coinAdi = findViewById(R.id.coinName);
        String selectedItem = getIntent().getStringExtra("secilenItem");
        coinAdi.setText(selectedItem);

        Button editButton = findViewById(R.id.duzenlemeButonu);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editButton.getText().toString().equals("Ekleme Modu Aktif")) {
                    editButton.setText("Çıkartma Modu Aktif");
                }
                else { editButton.setText("Ekleme Modu Aktif"); }
            }
        });

        EditText miktar = findViewById(R.id.miktar);
        miktar.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    miktar.setText("");
                }
            }
        });

        EditText notEkle = findViewById(R.id.notEkle);
        notEkle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    notEkle.setText("");
                }
            }
        });
    }
}