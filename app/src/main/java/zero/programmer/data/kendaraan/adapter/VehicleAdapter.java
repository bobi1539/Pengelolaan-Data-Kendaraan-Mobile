package zero.programmer.data.kendaraan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.entitites.Vehicle;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.HolderData> {

    private Context context;
    private List<Vehicle> listVehicle;
    private String name, merk;
    private Vehicle vehicle;

    public VehicleAdapter(Context context, List<Vehicle> listVehicle){
        this.context = context;
        this.listVehicle = listVehicle;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vehicle, parent, false);
        HolderData holderData = new HolderData(layout);

        return holderData;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        vehicle = listVehicle.get(position);

        holder.textViewName.setText(vehicle.getName());
        holder.textViewMerk.setText(vehicle.getMerk());
    }

    @Override
    public int getItemCount() {
        return listVehicle.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewName, textViewMerk;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.card_vehicle_name);
            textViewMerk = itemView.findViewById(R.id.card_vehicle_merk);
        }
    }

}
