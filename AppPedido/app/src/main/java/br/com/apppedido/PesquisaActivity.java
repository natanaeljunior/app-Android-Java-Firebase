package br.com.apppedido;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class PesquisaActivity  extends Activity {


    private String[] lstFrutas;
    private ListView lView;
    private EditText eText;
    private ArrayList<String> pesquisa = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pesquisa);

        // Declarando os objetos do main.xml
        lView = (ListView) findViewById(R.id.lView);
        eText = (EditText) findViewById(R.id.eText);

        lstFrutas = new String[] { "Abacate", "Abacaxi", "Banana", "Carambola",
                "Goiaba", "Jabuticaba", "Laranja", "Maçã", "Melancia", "Morango","Abacate", "Abacaxi", "Banana", "Carambola",
                "Goiaba", "Jabuticaba", "Laranja", "Maçã", "Melancia", "Morango" ,"Abacate", "Abacaxi", "Banana", "Carambola",
                "Goiaba", "Jabuticaba", "Laranja", "Maçã", "Melancia", "Morango" ,"Abacate", "Abacaxi", "Banana", "Carambola",
                "Goiaba", "Jabuticaba", "Laranja", "Maçã", "Melancia", "Morango" };

        lView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lstFrutas));

        // Função responsável pela pesquisa
        Pesquisar();

        eText.addTextChangedListener(new TextWatcher() {

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
                lView.setAdapter(new ArrayAdapter<String>(PesquisaActivity.this, android.R.layout.simple_list_item_1, pesquisa));
            }
        });
    }

    public void Pesquisar() {
        int textlength = eText.getText().length();
        pesquisa.clear();

        for (int i = 0; i < lstFrutas.length; i++ ) {
            if (textlength <= lstFrutas[i].length()) {
                if (eText.getText().toString().equalsIgnoreCase((String)lstFrutas[i].subSequence(0, textlength))) {
                    pesquisa.add(lstFrutas[i]);
                }
            }
        }
    }
}

