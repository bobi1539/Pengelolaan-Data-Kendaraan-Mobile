package zero.programmer.data.kendaraan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.entitites.Driver;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.HolderData>{

    private final Context context;
    private List<Driver> listDriver;

    private String idDriver;

    private final ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);

    public DriverAdapter(Context context, List<Driver> listDriver) {
        this.context = context;
        this.listDriver = listDriver;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // membuat view card ke rycler view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_driver, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        // set data dari class user ke card driver
        Driver driver = listDriver.get(position);
        holder.textViewIdDriver.setText(driver.getIdDriver());
        holder.textViewFullName.setText(driver.getFullName());
        holder.textViewPhoneNumber.setText(driver.getPhoneNumber());

        if (!driver.getOnDuty()){
            holder.textViewIsOnDuty.setText(R.string.driver_not_on_duty);
        } else {
            holder.textViewIsOnDuty.setText(R.string.driver_on_duty);
        }
    }

    @Override
    public int getItemCount() {
        try {
            return listDriver.size();
        } catch (NullPointerException e){
            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewIdDriver, textViewFullName, textViewPhoneNumber, textViewIsOnDuty;
        CardView cardViewDriver;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // get view recycler dari card
            textViewIdDriver = itemView.findViewById(R.id.card_driver_id);
            textViewFullName = itemView.findViewById(R.id.card_driver_full_name);
            textViewPhoneNumber = itemView.findViewById(R.id.card_driver_phone_number);
            textViewIsOnDuty = itemView.findViewById(R.id.card_driver_is_on_duty);
        }
    }
}
