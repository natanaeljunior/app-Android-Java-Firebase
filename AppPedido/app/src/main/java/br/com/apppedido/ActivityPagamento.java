package br.com.apppedido;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import br.com.apppedido.Adapter.AbasAdapter;
import br.com.apppedido.Fragments.PrimeiroFragment;
import br.com.apppedido.Fragments.SegundoFragment;
import br.com.apppedido.Fragments.TerceiroFragment;

public class ActivityPagamento extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pagamento);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPagamento);
        toolbar.setNavigationIcon(R.drawable.left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AbasAdapter adapter = new AbasAdapter( getSupportFragmentManager() );
        adapter.adicionar( new PrimeiroFragment() , "Vencidas");
        adapter.adicionar( new SegundoFragment(), "Pagas");
        adapter.adicionar( new TerceiroFragment(), "Vencendo");

        ViewPager viewPager = (ViewPager) findViewById(R.id.abas_view_pager);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.abas);
        tabLayout.setupWithViewPager(viewPager);
    }
}
