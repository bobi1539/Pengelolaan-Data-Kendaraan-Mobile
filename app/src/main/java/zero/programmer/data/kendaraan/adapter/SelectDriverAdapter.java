package zero.programmer.data.kendaraan.adapter;

import android.content.Context;
import android.os.Vibrator;
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
import zero.programmer.data.kendaraan.entitites.Driver;

public class SelectDriverAdapter extends RecyclerView.Adapter<SelectDriverAdapter.HolderData>{

    private final Context context;
    private List<Driver> listDriver;

    private String idDriver, fullName, phoneNumber, isOnDuty;

    public SelectDriverAdapter(Context context, List<Driver> listDriver) {
        this.context = context;
        this.listDriver = listDriver;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_select_driver, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        Driver driver = listDriver.get(position);

        holder.textViewFullName.setText(driver.getFullName());
        holder.textViewIdDriver.setText(driver.getIdDriver());
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
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewFullName, textViewIdDriver, textViewPhoneNumber, textViewIsOnDuty;
        CardView cardViewSelectDriver;
        Vibrator vibrator;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // get view card
            textViewFullName = itemView.findViewById(R.id.card_select_driver_full_name);
            textViewIdDriver = itemView.findViewById(R.id.card_select_driver_id);
            textViewPhoneNumber = itemView.findViewById(R.id.card_select_driver_phone_number);
            textViewIsOnDuty = itemView.findViewById(R.id.card_select_driver_is_on_duty);
            cardViewSelectDriver = itemView.findViewById(R.id.card_select_driver);
            vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

            cardViewSelectDriver.setOnClickListener(v -> selectThisDriver());
        }

        private void selectThisDriver(){

            idDriver = textViewIdDriver.getText().toString();
            fullName = textViewFullName.getText().toString();
            phoneNumber = textViewPhoneNumber.getText().toString();
            isOnDuty = textViewIsOnDuty.getText().toString();

            if (isOnDuty.equals("Sedang Bertugas")){
                vibrator.vibrate(80);
                Toast.makeText(context, "Driver sedang bertugas", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Driver dipilih : " + idDriver, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
