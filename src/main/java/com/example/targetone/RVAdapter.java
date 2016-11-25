package com.example.targetone;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MenuItemsViewHolder> {

    public static class MenuItemsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView itemName;
        TextView itemDesc;
        ImageView itemPhoto;

        MenuItemsViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            itemName = (TextView)itemView.findViewById(R.id.menu_name);
            itemDesc = (TextView)itemView.findViewById(R.id.menu_item_desc);
            itemPhoto = (ImageView)itemView.findViewById(R.id.menu_photo);
        }
    }

    List<MenuItems> mainItems;

    RVAdapter(List<MenuItems> mainItems){
        this.mainItems = mainItems;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public MenuItemsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);

        MenuItemsViewHolder mivh = new MenuItemsViewHolder(v);
        return mivh;
    }

    @Override
    public void onBindViewHolder(MenuItemsViewHolder MenuItemsViewHolder, int i) {
        MenuItemsViewHolder.itemName.setText(mainItems.get(i).i_name);
        MenuItemsViewHolder.itemDesc.setText(mainItems.get(i).i_desc);
        MenuItemsViewHolder.itemPhoto.setImageResource(mainItems.get(i).iconId);
    }

    @Override
    public int getItemCount() {
        return mainItems.size();
    }


}