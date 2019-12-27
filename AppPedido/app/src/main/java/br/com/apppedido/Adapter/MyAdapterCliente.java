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

import br.com.apppedido.Model.Cliente;
import br.com.apppedido.R;

public class MyAdapterCliente extends BaseAdapter {
    Context context;
    ArrayList<Cliente> listforview;
    LayoutInflater inflator=null;
    View v;
    ViewHolder vholder;
    //Constructor
    public MyAdapterCliente(Context con, ArrayList<Cliente> list)
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
        TextView nomeRazao;
        ImageView image;
        TextView telefone;
        TextView email;

    }
    // Called for each view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v=convertView;
        if(convertView==null)
        {
            //inflate the view for each row of listview
            v=inflator.inflate(R.layout.row,null);
            //ViewHolder object to contain myadapter.xml elements
            vholder=new ViewHolder();
            vholder.nomeRazao =(TextView)v.findViewById(R.id.title_cli);
            vholder.image=(ImageView)v.findViewById(R.id.image_cli);
            vholder.telefone = (TextView)v.findViewById(R.id.phone_cli_list);
            vholder.email = (TextView)v.findViewById(R.id.textMoney);
            //set holder to the view
            v.setTag(vholder);
        }
        else
            vholder=(ViewHolder)v.getTag();
        //getting MyItem Object for each position
        Cliente item=(Cliente)listforview.get(position);
        //setting the values from object to holder views for each row
        vholder.nomeRazao.setText(item.getNomeRazao());
        vholder.telefone.setText(item.getTelefone1());
        vholder.email.setText(item.getEmail());
        Picasso.get().load(item.getCaminhoFoto()).into(vholder.image);
        return v;
    }
}