package br.com.apppedido;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.apppedido.Model.Pagamentos;
import br.com.apppedido.Model.Pedido;
import br.com.apppedido.Model.Vendedor;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView QtdPedido;
    TextView QtdVenda;
    TextView homeValorVenda;
    TextView vencidas;
    TextView vencendo;
    TextView pagas;
    ArrayList<Pedido> list_pedido = new ArrayList<Pedido>();
    ArrayList<Pagamentos> list_pag = new ArrayList<Pagamentos>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarHome);

        setSupportActionBar(toolbar);

        final DatabaseReference databasePedido = FirebaseDatabase.getInstance().getReference("pedidos");


        databasePedido.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_pedido.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Pedido pedido = data.getValue(Pedido.class);
                    list_pedido.add(pedido);
                }

                int cont = 0;
                double valor = 0.0;
                for (int i = 0; i < list_pedido.size(); i++) {
                    if (list_pedido.get(i).getStatus().equals("Solicitado")) {
                        cont++;
                    }

                }

                QtdPedido = findViewById(R.id.homeQtdPedido);
                QtdPedido.setText(Integer.toString(cont));

                int cont2 = 0;
                double valor2 = 0.0;
                for (int i = 0; i < list_pedido.size(); i++) {
                    if (list_pedido.get(i).getStatus().equals("Efetuado")) {
                        valor2 = valor2 + list_pedido.get(i).getValorPedido();
                        cont2++;
                    }

                }

                QtdVenda = findViewById(R.id.HomeQtdVenda);
                QtdVenda.setText(Integer.toString(cont2));

                homeValorVenda = findViewById(R.id.textHomeValorVenda);
                homeValorVenda.setText("R$ " + Double.toString(valor2));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference databasePagamento = FirebaseDatabase.getInstance().getReference("pagamentos");

        databasePagamento.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_pag.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Pagamentos pag = data.getValue(Pagamentos.class);
                    list_pag.add(pag);
                }

                int cont = 0;
                double valor = 0.0;
                for (int i = 0; i < list_pag.size(); i++) {
                    if (list_pag.get(i).getStatus().equals("Vencida")) {
                        cont++;
                    }

                }

                vencidas = findViewById(R.id.qtdVencidas);
                vencidas.setText(Integer.toString(cont));


                int cont2 = 0;
                for (int i = 0; i < list_pag.size(); i++) {
                    if (list_pag.get(i).getStatus().equals("Pago")) {
                        cont2++;
                    }
                }

                pagas = findViewById(R.id.qtdPagas);
                pagas.setText(Integer.toString(cont2));


                int cont3 = 0;
                for (int i = 0; i < list_pag.size(); i++) {
                    if (list_pag.get(i).getStatus().equals("Aberto")) {
                        cont3++;
                    }
                }

                vencendo = findViewById(R.id.qtdVencendo);
                vencendo.setText(Integer.toString(cont3));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        FloatingActionButton flaotButtonAdd = (FloatingActionButton) findViewById(R.id.floatingButtonHome);

        flaotButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadPedidoActivity.class);
                startActivity(intent);
            }
        });


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        final DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference("usuario").child(FirebaseAuth.getInstance().getUid());

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Vendedor userV = dataSnapshot.getValue(Vendedor.class);
                ImageView imgUser = findViewById(R.id.imgUsuario);
                TextView id =  findViewById(R.id.textIndentificador);

                Picasso.get().load(userV.getImagem()).into(imgUser);
                id.setText(userV.getIdentificador());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void clickAction() {
        Intent intent = new Intent(getApplicationContext(), PedidoActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Bundle bundle = new Bundle();
        if (id == R.id.op_clientes) {
            Toast.makeText(this, "Cliente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ClientesActivity.class);
            startActivity(intent);
        } else if (id == R.id.op_prod_esto) {
            Toast.makeText(this, "Produtos/Estoque", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ProdutoActivity.class);
            startActivity(intent);
        } else if (id == R.id.op_pedidos) {
            Toast.makeText(this, "Pedidos", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), PedidoActivity.class);
            startActivity(intent);
        } else if (id == R.id.op_relatorio) {
            Toast.makeText(this, "Relatorio", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ActivityRelatorio.class);
            startActivity(intent);
        } else if (id == R.id.op_pagamento) {
            Toast.makeText(this, "Pagamentos", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ActivityPagamento.class);
            startActivity(intent);
        } else if (id == R.id.op_sair) {
            FirebaseAuth autenticacao = FirebaseAuth.getInstance();
            autenticacao.signOut();
            Toast.makeText(this, "Usuario deslogado", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getTitleCondensed().toString()) {
            case "listPedidos":
                Intent intent = new Intent(getApplicationContext(), PedidoActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


}
