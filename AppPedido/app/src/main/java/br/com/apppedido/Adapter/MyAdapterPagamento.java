package br.com.apppedido.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import br.com.apppedido.Model.Pagamentos;
import br.com.apppedido.R;

public class MyAdapterPagamento extends BaseAdapter {
    Context context;
    ArrayList<Pagamentos> listforview;
    LayoutInflater inflator = null;
    View v;
    ViewHolder vholder;


    //Constructor
    public MyAdapterPagamento(Context con, ArrayList<Pagamentos> list) {
        super();
        context = con;
        listforview = list;
        if (con==null){
            return;
        }
        inflator = LayoutInflater.from(con);
    }

    // return position here
    @Override
    public long getItemId(int position) {
        return position;
    }

    // return size of list
    @Override
    public int getCount() {
        return listforview.size();
    }

    //get Object from each position
    @Override
    public Object getItem(int position) {
        return listforview.get(position);
    }

    //Viewholder class to contain inflated xml views
    private class ViewHolder {
        TextView nPedido;
        TextView vencimento;
        TextView valor;
        TextView cliente;
        ImageView logoPag;

    }

    // Called for each view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v = convertView;
        if (convertView == null) {
            //inflate the view for each row of listview
            v = inflator.inflate(R.layout.row_pag, null);
            //ViewHolder object to contain myadapter.xml elements
            vholder = new ViewHolder();
            vholder.nPedido = v.findViewById(R.id.textNPedido);
            vholder.cliente = v.findViewById(R.id.textClientePag);
            vholder.valor = v.findViewById(R.id.textValorPag);
            vholder.vencimento = v.findViewById(R.id.textVencimento);
            vholder.logoPag =  v.findViewById(R.id.imglogopag);
            //set holder to the view
            v.setTag(vholder);
        } else
            vholder = (ViewHolder) v.getTag();
        //getting MyItem Object for each position
        Pagamentos item = listforview.get(position);
        //setting the values from object to holder views for each row
        vholder.cliente.setText(item.getCliente().getNomeRazao());
        vholder.nPedido.setText(item.getPedido().getId());
        vholder.valor.setText(item.getValor());
        String dia = Integer.toString(item.getVencimento().getDay());
        String mes = Integer.toString(item.getVencimento().getMonth());
        String ano = Integer.toString(item.getVencimento().getYear());

        vholder.vencimento.setText(dia+"-"+mes+"-"+ano);
        Picasso.get().load(item.getCliente().getCaminhoFoto()).into(vholder.logoPag);
        return v;
    }
}