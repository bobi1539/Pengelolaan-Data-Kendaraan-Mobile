package zero.programmer.data.kendaraan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.ApiRequest;
import zero.programmer.data.kendaraan.api.RetroServer;
import zero.programmer.data.kendaraan.entitites.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.HolderData>{

    private final Context context;
    private List<User> listUser;

    private String username;

    private final ApiRequest apiRequest = RetroServer.getRetrofit().create(ApiRequest.class);

    public UserAdapter(Context context, List<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user, parent,false);
        return new HolderData(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        User user = listUser.get(position);
        holder.textViewFullName.setText(user.getFullName());
        holder.textViewUsername.setText(user.getUsername());
        holder.textViewEmployeeNumber.setText(user.getEmployeeNumber());
        holder.textViewPosition.setText(user.getPosition());
    }

    @Override
    public int getItemCount() {
        try{
            return listUser.size();
        } catch (NullPointerException e){
            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    public class HolderData extends RecyclerView.ViewHolder{

        TextView textViewFullName, textViewUsername, textViewEmployeeNumber, textViewPosition;

        public HolderData(@NonNull View itemView) {
            super(itemView);
            textViewFullName = itemView.findViewById(R.id.card_user_full_name);
            textViewUsername = itemView.findViewById(R.id.card_user_username);
            textViewEmployeeNumber = itemView.findViewById(R.id.card_user_employee_number);
            textViewPosition = itemView.findViewById(R.id.card_user_position);
        }
    }

}
