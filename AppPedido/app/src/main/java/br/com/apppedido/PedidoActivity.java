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

import br.com.apppedido.Adapter.MyAdapterPedido;
import br.com.apppedido.Model.Cliente;
import br.com.apppedido.Model.Pedido;

public class PedidoActivity extends AppCompatActivity {

    ArrayList<Pedido> list_pedido = new ArrayList<>();
    private ArrayList<Pedido> pesquisa = new ArrayList<>();
    private ListView lViewPed;
    private EditText eTextPed;

    DatabaseReference databasePedido;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pedido);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPed);
        toolbar.setNavigationIcon(R.drawable.left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final SwipeMenuListView menuListView = (SwipeMenuListView) findViewById(R.id.listViewPed);
        eTextPed = (EditText) findViewById(R.id.etextped);



        databasePedido = FirebaseDatabase.getInstance().getReference("pedidos");
        //Creating Adapter object for setting to listview
        final MyAdapterPedido adapter = new MyAdapterPedido(this, list_pedido);
        menuListView.setAdapter(adapter);

        databasePedido.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pedido newPed= dataSnapshot.getValue(Pedido.class);
                list_pedido.add(newPed);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Toast.makeText(getApplicationContext(), "s:"+s,Toast.LENGTH_LONG).show();
                for (int i = 0; i < list_pedido.size(); i++) {
                    if (list_pedido.get(i).getId().equals(dataSnapshot.getValue(Pedido.class).getId())) {
                        list_pedido.remove(i);
                    }
                }

                Pedido newPed = dataSnapshot.getValue(Pedido.class);
                list_pedido.add(newPed);
                Toast.makeText(getApplicationContext(), "s:"+newPed.getNome(),Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "REGISTRO EXCLUIDO COM SUCESSO: "+dataSnapshot.getValue(Cliente.class).getId(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < list_pedido.size(); i++) {
                    if (list_pedido.get(i).getId().equals(dataSnapshot.getValue(Pedido.class).getId())) {
                        list_pedido.remove(i);
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
                //Perform click events

                Pedido ped = (Pedido) list_pedido.get(position);

                String title =ped.getId();
                Toast.makeText(getApplicationContext(), "Clique: "+title, Toast.LENGTH_LONG).show();
            }
        });


        FloatingActionButton flaotButtonAdd = (FloatingActionButton) findViewById(R.id.floatButtonPedido);

        flaotButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadPedidoActivity.class);
                startActivity(intent);
            }
        });



        // Função responsável pela pesquisa
        Pesquisar();

        eTextPed.addTextChangedListener(new TextWatcher() {

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
                MyAdapterPedido adapter = new MyAdapterPedido(PedidoActivity.this, pesquisa);
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
                        Toast.makeText((getApplicationContext()), "OPEN: "+list_pedido.get(position).getId(), Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        Toast.makeText((getApplicationContext()), "DELETE", Toast.LENGTH_SHORT).show();
                        DatabaseReference uR = FirebaseDatabase.getInstance().getReference("pedidos").child(list_pedido.get(position).getId());
                        uR.removeValue();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }


    public void Pesquisar() {
        int textlength = eTextPed.getText().length();
        pesquisa.clear();

        for (int i = 0; i < list_pedido.size(); i++) {

            if (list_pedido.get(i).getNome().toLowerCase().contains(eTextPed.getText().toString().toLowerCase())) {
                pesquisa.add( list_pedido.get(i));
            }


        }
    }



}
