package zero.programmer.data.kendaraan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.entitites.BorrowVehicle;

public class BorrowVehicleAdapter extends RecyclerView.Adapter<BorrowVehicleAdapter.HolderData>{

    private final Context context;
    private List<BorrowVehicle> listBorrowVehicle;

    private Integer idBorrow;

    public BorrowVehicleAdapter(Context context, List<BorrowVehicle> listBorrowVehicle) {
        this.context = context;
        this.listBorrowVehicle = listBorrowVehicle;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view card ke rycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_borrow_vehicle, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        // set data dari class borrow vehicle ke card
        BorrowVehicle borrowVehicle = listBorrowVehicle.get(position);
        holder.textViewFullName.setText(borrowVehicle.getUser().getFullName());
        holder.textViewMerk.setText(borrowVehicle.getVehicle().getMerk());
        holder.textViewDateOfFilling.setText(String.valueOf(borrowVehicle.getDateOfFilling()));
        holder.textViewDestination.setText(borrowVehicle.getDestination());
    }

    @Override
    public int getItemCount() {
        try {
            return listBorrowVehicle.size();
        } catch (NullPointerException e){
            Toast.makeText(context, "Error : No Data Found" + e, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewFullName, textViewMerk, textViewDateOfFilling, textViewDestination;
        CardView cardViewBorrowVehicle;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // get view layout
            textViewFullName = itemView.findViewById(R.id.card_borrow_vehicle_full_name);
            textViewMerk = itemView.findViewById(R.id.card_borrow_vehicle_merk);
            textViewDateOfFilling = itemView.findViewById(R.id.card_borrow_vehicle_date_of_filling);
            textViewDestination = itemView.findViewById(R.id.card_borrow_vehicle_destination);
        }
    }
}
