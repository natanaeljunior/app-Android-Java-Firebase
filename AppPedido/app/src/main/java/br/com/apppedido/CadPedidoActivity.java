package br.com.apppedido;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.apppedido.Adapter.MyAdapterCadPedido;
import br.com.apppedido.Model.Cliente;
import br.com.apppedido.Model.Pagamentos;
import br.com.apppedido.Model.Pedido;
import br.com.apppedido.Model.Produto;

public class CadPedidoActivity extends AppCompatActivity {

    Cliente cliente;
    ArrayList<Produto> lista_produtos;
    TextView valorTotal;
    TextView clienteText;
    Button btnSalvarPedido;

    ListView listView_add_prod;
    String textQtd;

    EditText editCont;


    private static final int SELECT_PICTURE = 200;
    private static final String TAG = "SelectImageActivity";

    private StorageReference storageRef;
    DatabaseReference databasePedido;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_cad_pedido);

        storageRef = FirebaseStorage.getInstance().getReference();
        databasePedido = FirebaseDatabase.getInstance().getReference("pedidos");

        final DatabaseReference databasePag = FirebaseDatabase.getInstance().getReference("pagamentos");

        inicializarComponentes();

        editCont = findViewById(R.id.editContador);
        ImageView addCliImg = (ImageView) findViewById(R.id.addClienteImg);
        addCliImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ClientesActivity.class);
                int i = 0;
                intent.putExtra("cliente", i);
                startActivityForResult(intent, 2);
                Toast.makeText(getApplicationContext(), "Selecione Cliente!", Toast.LENGTH_LONG).show();
            }
        });

        ImageView addProImg = (ImageView) findViewById(R.id.addProdutoImg);
        addProImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), ProdutoActivity.class);
                int i = 0;
                intent.putExtra("produto", i);
                startActivityForResult(intent, 1);


            }
        });



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCadPedido);
        toolbar.setNavigationIcon(R.drawable.ic_cancel); // your drawable
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


        //Cadastrar Pedido

        btnSalvarPedido.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (clienteText == null) {
                    Toast.makeText(getApplicationContext(), "Selecione um Cliente!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (lista_produtos.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Lista de produtos não pode ser Vazia!", Toast.LENGTH_LONG).show();
                    return;
                }

                final Pedido newPed = setPedido();
                databasePedido.child(newPed.getId()).setValue(newPed).addOnCompleteListener(CadPedidoActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(getApplicationContext(), "Pedido salvo com sucesso!", Toast.LENGTH_LONG).show();

                        Pagamentos pag = new Pagamentos();
                        pag.setId(databasePag.push().getKey());
                        pag.setCliente(newPed.getCliente());
                        pag.setPedido(newPed);
                        pag.setValor(Double.toString(newPed.getValorPedido()));
                        pag.setVencimento(Calendar.getInstance().getTime());
                        pag.setStatus("Aberto");

                        databasePag.child(pag.getId()).setValue(pag);
                    }
                });
                finish();
            }
        });


    }

    private double valorPedido() {
        double vPedido = 0.0;
        for(int i =  0; i<lista_produtos.size(); i++){
            double preco = Double.parseDouble(lista_produtos.get(i).getPreco());
            double qtd =(lista_produtos.get(i).getQtd());
            vPedido = vPedido+ (preco*qtd);
        }
        valorTotal.setText(Double.toString(vPedido));
     return   vPedido;
    }

    Pedido setPedido() {

        Pedido ped = new Pedido();

        ped.setId(databasePedido.push().getKey());
        ped.setNome(cliente.getNomeRazao());

        ped.setData(Calendar.getInstance().getTime());
        ped.setValorPedido(valorPedido());
        ped.setCliente(cliente);
        ped.setList_produto(lista_produtos);
        ped.setImgPedido(cliente.getCaminhoFoto());
        ped.setStatus("Solicitado");
        return ped;
    }

    public void inicializarComponentes() {

        cliente = new Cliente();
        lista_produtos = new ArrayList<Produto>();
        valorTotal = findViewById(R.id.textCalcTotal);
        btnSalvarPedido = findViewById(R.id.btnSalvarCadPed);
        listView_add_prod = findViewById(R.id.listViewAddProduto);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {

            Produto prod = (Produto) data.getSerializableExtra("produto");
            prod.setQtd(2);
            lista_produtos.add(prod);
            valorPedido();

            final MyAdapterCadPedido adapter = new MyAdapterCadPedido(this, lista_produtos);
            listView_add_prod.setAdapter(adapter);
            Toast.makeText(getApplicationContext(), "INTENT:" + prod.getNome(), Toast.LENGTH_LONG).show();


        }
        if (resultCode == RESULT_OK && requestCode == 2) {

            Cliente cli = (Cliente) data.getSerializableExtra("cliente");
            clienteText = (TextView) findViewById(R.id.textSelectCli);
            clienteText.setText(cli.getNomeRazao());
            cliente =  cli;
        }


    }

    public void caixaDialogo() {

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(getLayoutInflater().inflate(R.layout.popup, null));

        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                EditText editText = alertDialog.findViewById(R.id.editTextDialogUserInput);
                textQtd = editText.getText().toString();

            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                textQtd = null;
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }


}