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

import br.com.apppedido.Model.Pedido;
import br.com.apppedido.R;

public class MyAdapterPedido extends BaseAdapter {
    Context context;
    ArrayList<Pedido> listforview;
    LayoutInflater inflator=null;
    View v;
    ViewHolder vholder;
    //Constructor
    public MyAdapterPedido(Context con, ArrayList<Pedido> list)
    {
        super();
        context=con;
        listforview=list;
        inflator=LayoutInflater.from(con);
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
    private  class ViewHolder
    {
        TextView nome;
        ImageView image;
        TextView valorT;
        TextView statusPedido;
        ImageView icon;

    }
    // Called for each view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v=convertView;
        if(convertView==null)
        {
            //inflate the view for each row of listview
            v=inflator.inflate(R.layout.row_pedido,null);
            //ViewHolder object to contain myadapter.xml elements
            vholder=new ViewHolder();
            vholder.nome =(TextView)v.findViewById(R.id.title_pedido);
            vholder.image=(ImageView)v.findViewById(R.id.image_pedido);
            vholder.valorT = (TextView)v.findViewById(R.id.textValorPed);
            vholder.statusPedido =  (TextView)v.findViewById(R.id.textStatus);
            vholder.icon =  v.findViewById(R.id.imgStatus);

            //set holder to the view
            v.setTag(vholder);
        }
        else
            vholder=(ViewHolder)v.getTag();
        //getting MyItem Object for each position
        Pedido item=(Pedido)listforview.get(position);
        //setting the values from object to holder views for each row
        vholder.nome.setText(item.getNome());
        vholder.valorT.setText(Double.toString(item.getValorPedido()));
        Picasso.get().load(item.getImgPedido()).into(vholder.image);
        vholder.statusPedido.setText(item.getStatus());
        if(item.getStatus().equals("Solicitado")){
            vholder.icon.setImageResource(R.drawable.ic_status);
        }
        if(item.getStatus().equals("Efetuado")){
            vholder.icon.setImageResource(R.drawable.ic_status2);
        }
        return v;
    }
}