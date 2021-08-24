package com.example.tges.Fotos.galeriaLista;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tges.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdapterGaleria
    extends RecyclerView.Adapter<AdapterGaleria.ProductViewHolder>
        implements View.OnClickListener{

        private Context mCtx;
        private List<VOGaleria> productList;
        private List<VOGaleria> OriginalproductList;
        private View.OnClickListener listener;

    public AdapterGaleria(Context mCtx, List<VOGaleria> productList) {
            this.mCtx = mCtx;
            this.productList = productList;
            this.OriginalproductList= new ArrayList<>();
            OriginalproductList.addAll(productList);
        }

        @Override
        public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(mCtx);
            View view = inflater.inflate(R.layout.item_list_galeria, null);
            view.setOnClickListener(this);
            return new ProductViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ProductViewHolder holder, int position) {
            VOGaleria product = productList.get(position);
            holder.idId.setText((product.getId()));
            holder.idTitulo.setText(product.getNombre());
            holder.idDescripcion.setText(product.getDescripcion());
            byte [] Foto = product.getFoto();
            Bitmap bitmap = BitmapFactory.decodeByteArray(Foto,0,Foto.length);
            holder.idFoto.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }

        public void filter(String strSearch){
        if(strSearch.length()==0){
            productList.clear();
            productList.addAll(OriginalproductList);
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                productList.clear();
                List<VOGaleria> collect = OriginalproductList.stream()
                        .filter(p -> p.getNombre().toLowerCase().contains(strSearch))
                        .collect(Collectors.toList());
                productList.addAll(collect);
            }else{
                productList.clear();
                for(VOGaleria p: OriginalproductList){
                    if(p.getNombre().toLowerCase().contains(strSearch)){
                        productList.add(p);
                    }

                }
            }
        }
        notifyDataSetChanged();
        }

        public void setOnClickListener(View.OnClickListener listener){
            this.listener=listener;
        }

        @Override
        public void onClick(View v) {
            if (listener!=null){
                listener.onClick(v);
            }
        }

        class ProductViewHolder extends RecyclerView.ViewHolder {

            TextView idId, idTitulo, idDescripcion;
            ImageView idFoto;

            public ProductViewHolder(View itemView) {
                super(itemView);
                idId= itemView.findViewById(R.id.idId);
                idTitulo=itemView.findViewById(R.id.idTitulo);
                idDescripcion=itemView.findViewById(R.id.idDescripcion);
                idFoto=itemView.findViewById(R.id.idFoto);
            }
    }

}
