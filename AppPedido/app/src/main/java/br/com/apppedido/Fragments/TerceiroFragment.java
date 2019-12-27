package br.com.apppedido.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import br.com.apppedido.Adapter.MyAdapterPagamento;
import br.com.apppedido.Model.Pagamentos;
import br.com.apppedido.R;

public class TerceiroFragment extends Fragment {

    ArrayList<Pagamentos> list_vencendo = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.terceiro_fragment, container, false);


        final ListView listView_vencendo = view.findViewById(R.id.listViewVencendo);

        DatabaseReference databasePag = FirebaseDatabase.getInstance().getReference("pagamentos");

        databasePag.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_vencendo.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                        Pagamentos pagamentos = data.getValue(Pagamentos.class);
                    if(pagamentos.getStatus().equals("Aberto")){
                        list_vencendo.add(pagamentos);
                   }
                }

                final MyAdapterPagamento adapter = new MyAdapterPagamento(getContext(), list_vencendo);
                listView_vencendo.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

}
