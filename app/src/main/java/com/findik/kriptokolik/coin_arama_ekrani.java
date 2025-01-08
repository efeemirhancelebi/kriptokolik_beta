package com.findik.kriptokolik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class coin_arama_ekrani extends AppCompatActivity implements SearchView.OnQueryTextListener {

    // Örnek sabit aranan değerler
    String[] ArananDegerler = {"Bitcoin", "Ethereum", "Avalanche", "Ocean Protocol", "Biconomy", "Solana", "Sui", "Dogecoin"};

    private SearchView naramaKutusu;
    private ListView naramaListesi;
    private ArrayList<String> coinList;  // Aranan coin’lerin tutulduğu liste

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_arama_ekrani);

        // Kenar boşlukları (status bar vs.)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coin_arama_ekrani), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        naramaKutusu = findViewById(R.id.aramaKutusu);
        naramaListesi = findViewById(R.id.aramaListesi);

        // Intent ile gelen coinNames (örn. "long_name" listesi) var mı?
        coinList = getIntent().getStringArrayListExtra("veri");
        if (coinList == null || coinList.isEmpty()) {
            // Boş geldiyse, en azından sabit "ArananDegerler" koy
            Log.e("coin_arama_ekrani", "coinList boş veya null, ArananDegerler ile dolduruyoruz");
            coinList = new ArrayList<>(Arrays.asList(ArananDegerler));
        }

        // ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                coinList
        ) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(getResources().getColor(R.color.white));
                textView.setTextSize(18);

                return view;
            }
        };
        naramaListesi.setAdapter(adapter);
        naramaListesi.setTextFilterEnabled(true);

        // Arama setup
        setupArama();

        // Tıklanınca "coin_ekleme_ekrani"na gidiyoruz, seçilen coin ismini gönderiyoruz
        naramaListesi.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String secilenItem = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent(coin_arama_ekrani.this, coin_ekleme_ekrani.class);
            intent.putExtra("secilenItem", secilenItem);
            startActivity(intent);
        });
    }

    private void setupArama() {
        naramaKutusu.setIconifiedByDefault(false);
        naramaKutusu.setOnQueryTextListener(this);
        naramaKutusu.setSubmitButtonEnabled(true);
        naramaKutusu.setQueryHint("Coin Ara...");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // İsterseniz ek işlem yapabilirsiniz
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            naramaListesi.clearTextFilter();
        } else {
            naramaListesi.setFilterText(newText);
        }
        return true;
    }
}
