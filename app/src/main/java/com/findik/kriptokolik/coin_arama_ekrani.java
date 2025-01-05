package com.findik.kriptokolik;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class coin_arama_ekrani extends AppCompatActivity implements SearchView.OnQueryTextListener {

    String[] ArananDegerler = {"deger1", "deger2", "deger3", "deger4", "deger5", "deger6", "deger7"};
    private SearchView naramaKutusu;
    private ListView naramaListesi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coin_arama_ekrani);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.coin_arama_ekrani), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        naramaKutusu = findViewById(R.id.aramaKutusu);
        naramaListesi =findViewById(R.id.aramaListesi);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, ArananDegerler);

        naramaListesi.setAdapter(adapter);
        naramaListesi.setTextFilterEnabled(true);
        setupArama();

        naramaListesi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String secilenItem = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(coin_arama_ekrani.this, coin_ekleme_ekrani.class);
                intent.putExtra("secilenItem", secilenItem);
                startActivity(intent);
            }
        });
    }

    private void setupArama() {
        naramaKutusu.setIconifiedByDefault(false);
        naramaKutusu.setOnQueryTextListener(this);
        naramaKutusu.setSubmitButtonEnabled(true);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty (newText)){
            naramaListesi.clearTextFilter();
        } else {
            naramaListesi.setFilterText(newText);
        }
        return true;
    }
}

