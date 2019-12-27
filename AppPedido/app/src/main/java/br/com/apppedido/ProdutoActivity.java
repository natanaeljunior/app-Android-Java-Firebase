package br.com.apppedido;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import br.com.apppedido.Adapter.MyAdapterProduto;
import br.com.apppedido.Model.Cliente;
import br.com.apppedido.Model.Produto;

public class ProdutoActivity extends AppCompatActivity {

    ArrayList<Produto> list_produto = new ArrayList<>();
    private ArrayList<Produto> pesquisa = new ArrayList<>();
    private ListView lViewPro;
    private EditText eTextPro;
    private String m_Text;
    ImageView btnScan;
    DatabaseReference databaseProduto;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_produtos);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProduto);
        toolbar.setNavigationIcon(R.drawable.left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final SwipeMenuListView menuListView = (SwipeMenuListView) findViewById(R.id.listViewProduto);
        eTextPro = (EditText) findViewById(R.id.etextproduto);

        btnScan = findViewById(R.id.imgScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ScannerActivity.class);
                intent.putExtra("scan",true);
                startActivityForResult(intent, 3);
            }
        });


        databaseProduto = FirebaseDatabase.getInstance().getReference("produtos");
        //Creating Adapter object for setting to listview
        final MyAdapterProduto adapter = new MyAdapterProduto(this, list_produto);
        menuListView.setAdapter(adapter);

        databaseProduto.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Produto newPro = dataSnapshot.getValue(Produto.class);
                list_produto.add(newPro);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(), "s:" + s, Toast.LENGTH_LONG).show();
                for (int i = 0; i < list_produto.size(); i++) {
                    if (list_produto.get(i).getId().equals(dataSnapshot.getValue(Produto.class).getId())) {
                        list_produto.remove(i);
                    }
                }

                Produto newPro = dataSnapshot.getValue(Produto.class);
                list_produto.add(newPro);
                Toast.makeText(getApplicationContext(), "s:" + newPro.getNome(), Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "REGISTRO EXCLUIDO COM SUCESSO: " + dataSnapshot.getValue(Cliente.class).getId(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < list_produto.size(); i++) {
                    if (list_produto.get(i).getId().equals(dataSnapshot.getValue(Produto.class).getId())) {
                        list_produto.remove(i);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        // Handle Listview click
        menuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = getIntent();
                if (intent.hasExtra("produto")) {
                    //     list_produto.get(position).setQtd(Integer.parseInt(qtd));
                    intent.putExtra("produto", list_produto.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        FloatingActionButton flaotButtonAdd = (FloatingActionButton) findViewById(R.id.floatButtonProduto);

        flaotButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadProdutoActivity.class);
                startActivity(intent);
            }
        });


        // Função responsável pela pesquisa
        Pesquisar();

        eTextPro.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }

            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                Pesquisar();
                MyAdapterProduto adapter = new MyAdapterProduto(ProdutoActivity.this, pesquisa);
                menuListView.setAdapter(adapter);
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                        0xff)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
// set creator


        menuListView.setMenuCreator(creator);

        menuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        Toast.makeText((getApplicationContext()), "OPEN: " + list_produto.get(position).getId(), Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        Toast.makeText((getApplicationContext()), "DELETE", Toast.LENGTH_SHORT).show();
                        DatabaseReference uR = FirebaseDatabase.getInstance().getReference("produtos").child(list_produto.get(position).getId());
                        uR.removeValue();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 3) {

            String cod =  data.getStringExtra("scan");

            eTextPro.setText(cod);

        }
    }


    public void Pesquisar() {
        int textlength = eTextPro.getText().length();
        pesquisa.clear();

        for (int i = 0; i < list_produto.size(); i++) {

            if ((list_produto.get(i).getNome().toLowerCase().contains(eTextPro.getText().toString().toLowerCase())) || list_produto.get(i).getCodBarra().toLowerCase().equals(eTextPro.getText().toString().toLowerCase())) {
                pesquisa.add(list_produto.get(i));
            }

        }
    }


}

