package com.example.asfalis;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserAdapterVh> implements Filterable {

    private List<TitleList> titleList;
    private List<TitleList> titleListfiltered;
    private Context context;
    private SelectedUser selectedUser;

    public UserAdapter(List<TitleList> titleList, SelectedUser selectedUser) {
        this.titleList = titleList;
        this.titleListfiltered = titleList;
        this.selectedUser = selectedUser;
    }

    @NonNull
    @Override
    public UserAdapter.UserAdapterVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new UserAdapterVh(LayoutInflater.from(context).inflate(R.layout.row_title, null));
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserAdapterVh holder, int position) {
        TitleList titlemodel = titleList.get(position);
        String title = titlemodel.getTitle();

        holder.titlename.setText(title);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint == null | constraint.length() == 0){
                    filterResults.count = titleListfiltered.size();
                    filterResults.values = titleListfiltered;
                }else{
                    String searchchr = constraint.toString().toLowerCase();

                    List<TitleList> resultData = new ArrayList<>();

                    for(TitleList titlel: titleListfiltered){
                        if(titlel.getTitle().toLowerCase().contains(searchchr)){
                            resultData.add(titlel);
                        }
                    }
                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                titleList = (List<TitleList>) results.values;
                notifyDataSetChanged();

            }
        };
        return filter;
    }

    public interface SelectedUser{

        void selectedUser(TitleList titleList);
    }

    public class UserAdapterVh extends RecyclerView.ViewHolder {
        TextView titlename;
        ImageView next;
        public UserAdapterVh(@NonNull View itemView) {
            super(itemView);

            titlename= itemView.findViewById(R.id.titlename);
            next = itemView.findViewById(R.id.next);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedUser.selectedUser(titleList.get(getAdapterPosition()));
                }
            });

        }
    }
}
