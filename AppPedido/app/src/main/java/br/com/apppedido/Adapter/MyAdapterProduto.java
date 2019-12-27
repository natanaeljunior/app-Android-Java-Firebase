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

import br.com.apppedido.Model.Produto;
import br.com.apppedido.R;

public class MyAdapterProduto extends BaseAdapter {
    Context context;
    ArrayList<Produto> listforview;
    LayoutInflater inflator=null;
    View v;
    ViewHolder vholder;
    //Constructor
    public MyAdapterProduto(Context con, ArrayList<Produto> list)
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
        TextView preco;

    }
    // Called for each view
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        v=convertView;
        if(convertView==null)
        {
            //inflate the view for each row of listview
            v=inflator.inflate(R.layout.row_produto,null);
            //ViewHolder object to contain myadapter.xml elements
            vholder=new ViewHolder();
            vholder.nome =(TextView)v.findViewById(R.id.title_produto);
            vholder.image=(ImageView)v.findViewById(R.id.image_produto);
            vholder.preco = (TextView)v.findViewById(R.id.textPreco);

            //set holder to the view
            v.setTag(vholder);
        }
        else
            vholder=(ViewHolder)v.getTag();
        //getting MyItem Object for each position
        Produto item=(Produto)listforview.get(position);
        //setting the values from object to holder views for each row
        vholder.nome.setText(item.getNome());
        vholder.preco.setText(item.getPreco());
        Picasso.get().load(item.getImgProduto()).into(vholder.image);
        return v;
    }
}