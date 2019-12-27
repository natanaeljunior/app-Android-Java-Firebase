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

import br.com.apppedido.Adapter.MyAdapterCliente;
import br.com.apppedido.Model.Cliente;

public class ClientesActivity extends AppCompatActivity {

    private static final String TAG = "ClientesActivity";
    ArrayList<Cliente> list_cli = new ArrayList<>();
    private EditText eTextCli;
    private ArrayList<Cliente> pesquisa = new ArrayList<>();


    DatabaseReference databaseCliente;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCli);
        toolbar.setNavigationIcon(R.drawable.left); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });



        final SwipeMenuListView menuListView = (SwipeMenuListView) findViewById(R.id.listViewCli);
        eTextCli = (EditText) findViewById(R.id.etextcli);


        databaseCliente = FirebaseDatabase.getInstance().getReference("clientes");
        //Creating Adapter object for setting to listview
        final MyAdapterCliente adapter = new MyAdapterCliente(this, list_cli);
        menuListView.setAdapter(adapter);

       databaseCliente.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot ds, @Nullable String s) {
               Cliente newCli = ds.getValue(Cliente.class);
               list_cli.add(newCli);
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot ds, @Nullable String s) {
               Toast.makeText(getApplicationContext(), "s:"+s,Toast.LENGTH_LONG).show();
               for (int i = 0; i < list_cli.size(); i++) {
                   if (list_cli.get(i).getId().equals(ds.getValue(Cliente.class).getId())) {
                       list_cli.remove(i);
                   }
               }

               Cliente newCli = ds.getValue(Cliente.class);
               list_cli.add(newCli);
               Toast.makeText(getApplicationContext(), "s:"+newCli.getNomeRazao(),Toast.LENGTH_LONG).show();
               adapter.notifyDataSetChanged();


           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot ds) {
               Toast.makeText(getApplicationContext(), "REGISTRO EXCLUIDO COM SUCESSO: "+ds.getValue(Cliente.class).getId(), Toast.LENGTH_LONG).show();
               for (int i = 0; i < list_cli.size(); i++) {
                   if (list_cli.get(i).getId().equals(ds.getValue(Cliente.class).getId())) {
                       list_cli.remove(i);
                   }
               }

               adapter.notifyDataSetChanged();
           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot ds, @Nullable String s) {

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
                Intent intent = getIntent();
                if(intent.hasExtra("cliente")){
                    intent.putExtra("cliente", list_cli.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });


        FloatingActionButton flaotButtonAdd = (FloatingActionButton) findViewById(R.id.floatButtonCliente);

        flaotButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadClienteActivity.class);
                startActivity(intent);
            }
        });



        // Função responsável pela pesquisa
        Pesquisar();

        eTextCli.addTextChangedListener(new TextWatcher() {

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
                MyAdapterCliente adapter = new MyAdapterCliente(ClientesActivity.this, pesquisa);
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
                openItem.setTitle("Editar");
                // set item title fontsize
                openItem.setTitleSize(18);
                openItem.setIcon(R.drawable.ic_edit);
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

                        Intent intent = new Intent(getApplicationContext(), CadClienteActivity.class);
                        intent.putExtra("editCliente", list_cli.get(position));
                        startActivity(intent);

                        Toast.makeText((getApplicationContext()), "EDIT: "+list_cli.get(position).getId(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText((getApplicationContext()), "DELETE", Toast.LENGTH_SHORT).show();
                        DatabaseReference uR = FirebaseDatabase.getInstance().getReference("clientes").child(list_cli.get(position).getId());
                        uR.removeValue();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }


    public void Pesquisar() {
        int textlength = eTextCli.getText().length();
        pesquisa.clear();

        for (int i = 0; i < list_cli.size(); i++) {
            if (textlength <= list_cli.get(i).getNomeRazao().length()) {
                if (eTextCli.getText().toString().equalsIgnoreCase((String)list_cli.get(i).getNomeRazao().subSequence(0, textlength))) {
                    pesquisa.add( list_cli.get(i));
                }
            }
        }
    }


}
