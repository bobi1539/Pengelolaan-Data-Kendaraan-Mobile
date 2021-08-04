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
import zero.programmer.data.kendaraan.entitites.Vehicle;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.HolderData> {

    private Context context;
    private List<Vehicle> listVehicle;
    private Vehicle vehicle;

    public VehicleAdapter(Context context, List<Vehicle> listVehicle){
        this.context = context;
        this.listVehicle = listVehicle;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_vehicle, parent, false);

        return new HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        vehicle = listVehicle.get(position);

        holder.textViewRegistrationNumber.setText(vehicle.getRegistrationNumber());
        holder.textViewName.setText(vehicle.getName());
        holder.textViewMerk.setText(vehicle.getMerk());
        holder.textViewPoliceNumber.setText(vehicle.getPoliceNumber());

        if (!vehicle.getBorrow()){
            holder.textViewIsBorrow.setText(R.string.borrow_false);
        } else {
            holder.textViewIsBorrow.setText(R.string.borrow_true);
        }
    }

    @Override
    public int getItemCount() {
        return listVehicle.size();
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewName, textViewMerk, textViewPoliceNumber, textViewIsBorrow, textViewRegistrationNumber;
        CardView cardViewVehicle;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.card_vehicle_name);
            textViewMerk = itemView.findViewById(R.id.card_vehicle_merk);
            textViewPoliceNumber = itemView.findViewById(R.id.card_vehicle_police_number);
            textViewIsBorrow = itemView.findViewById(R.id.card_vehicle_is_borrow);
            textViewRegistrationNumber = itemView.findViewById(R.id.card_vehicle_registration_number);

            cardViewVehicle = itemView.findViewById(R.id.card_vehicle);

            cardViewVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detailVehicle();
                }
            });
        }

        private void detailVehicle(){

            String registrationNumber = textViewRegistrationNumber.getText().toString();

            Toast.makeText(context, "Detail Kendaraan : " + registrationNumber, Toast.LENGTH_SHORT).show();

        }
    }

}
