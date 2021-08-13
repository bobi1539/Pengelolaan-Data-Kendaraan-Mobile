package zero.programmer.data.kendaraan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.BorrowVehicle;
import zero.programmer.data.kendaraan.response.ResponseOneData;

public class BorrowVehicleAdapter extends RecyclerView.Adapter<BorrowVehicleAdapter.HolderData>{

    private final Context context;
    private List<BorrowVehicle> listBorrowVehicle;

    private Integer idBorrow;

    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

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
        holder.textViewIdBorrow.setText(String.valueOf(borrowVehicle.getIdBorrow()));
        holder.textViewFullName.setText(borrowVehicle.getUser().getFullName());
        holder.textViewMerk.setText(borrowVehicle.getVehicle().getMerk());

        // format tanggal pengajuan
        holder.textViewDateOfFilling.setText(dateFormat.format(borrowVehicle.getDateOfFilling()));

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

        TextView textViewIdBorrow, textViewFullName, textViewMerk, textViewDateOfFilling, textViewDestination;
        CardView cardViewBorrowVehicle;

        BottomSheetDialog bottomSheetDialog;
        TextView textViewDetailFullName, textViewDetailEmployeeNumber, textViewDetailPosition,
                textViewDetailVehicleName, textViewDetailMerk, textViewDetailPoliceNumber,
                textViewDetailDriverName1, textViewDetailDriverName2, textViewDetailPhoneNumber1,
                textViewDetailDateOfFilling, textViewDetailPhoneNumber2,
                textViewDetailNecessity, textViewDetailBorrowDate, textViewDetailReturnDate,
                textViewDetailDestination, textViewDetailBorrowStatus;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // get view layout
            textViewIdBorrow = itemView.findViewById(R.id.card_borrow_vehicle_id_borrow);
            textViewFullName = itemView.findViewById(R.id.card_borrow_vehicle_full_name);
            textViewMerk = itemView.findViewById(R.id.card_borrow_vehicle_merk);
            textViewDateOfFilling = itemView.findViewById(R.id.card_borrow_vehicle_date_of_filling);
            textViewDestination = itemView.findViewById(R.id.card_borrow_vehicle_destination);
            cardViewBorrowVehicle = itemView.findViewById(R.id.card_borrow_vehicle_data);

            // click card
            cardViewBorrowVehicle.setOnClickListener(v -> detailBorrowVehicle());
        }

        private void detailBorrowVehicle(){
            idBorrow = Integer.parseInt(textViewIdBorrow.getText().toString());

            Call<ResponseOneData<BorrowVehicle>> getDetailBorrowVehicle = GetConnection.apiRequest
                    .getBorrowVehicle(ApiKeyData.getApiKey(), idBorrow);

            getDetailBorrowVehicle.enqueue(new Callback<ResponseOneData<BorrowVehicle>>() {
                @Override
                public void onResponse(Call<ResponseOneData<BorrowVehicle>> call, Response<ResponseOneData<BorrowVehicle>> response) {

                    if (response.code() == 404){
                        Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    } else {
                        try {

                            BorrowVehicle borrowVehicle = response.body().getData();

                            bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
                            View bottomSheetView = LayoutInflater.from(context)
                                    .inflate(R.layout.bottom_sheet_borrow_vehicle,
                                            itemView.findViewById(R.id.bottom_sheet_borrow_vehicle));

                            // get view
                            textViewDetailFullName = bottomSheetView.findViewById(R.id.tv_user_full_name_borrow);
                            textViewDetailEmployeeNumber = bottomSheetView.findViewById(R.id.tv_user_employee_number_borrow);
                            textViewDetailPosition = bottomSheetView.findViewById(R.id.tv_user_position_borrow);
                            textViewDetailVehicleName = bottomSheetView.findViewById(R.id.tv_vehicle_name_borrow);
                            textViewDetailMerk = bottomSheetView.findViewById(R.id.tv_vehicle_merk_borrow);
                            textViewDetailPoliceNumber = bottomSheetView.findViewById(R.id.tv_vehicle_police_number_borrow);
                            textViewDetailDriverName1 = bottomSheetView.findViewById(R.id.tv_driver_full_name_borrow1);
                            textViewDetailDriverName2 = bottomSheetView.findViewById(R.id.tv_driver_full_name_borrow2);
                            textViewDetailPhoneNumber1 = bottomSheetView.findViewById(R.id.tv_driver_phone_number_borrow1);
                            textViewDetailPhoneNumber2 = bottomSheetView.findViewById(R.id.tv_driver_phone_number_borrow2);
                            textViewDetailDateOfFilling = bottomSheetView.findViewById(R.id.tv_borrow_date_of_filling);
                            textViewDetailNecessity = bottomSheetView.findViewById(R.id.tv_borrow_necessity);
                            textViewDetailBorrowDate = bottomSheetView.findViewById(R.id.tv_borrow_date);
                            textViewDetailReturnDate = bottomSheetView.findViewById(R.id.tv_borrow_return_date);
                            textViewDetailDestination = bottomSheetView.findViewById(R.id.tv_borrow_destination);
                            textViewDetailBorrowStatus = bottomSheetView.findViewById(R.id.tv_borrow_status);

                            // set detail from response
                            textViewDetailFullName.setText(borrowVehicle.getUser().getFullName());
                            textViewDetailEmployeeNumber.setText(borrowVehicle.getUser().getEmployeeNumber());
                            textViewDetailPosition.setText(borrowVehicle.getUser().getPosition());
                            textViewDetailVehicleName.setText(borrowVehicle.getVehicle().getName());
                            textViewDetailMerk.setText(borrowVehicle.getVehicle().getMerk());
                            textViewDetailPoliceNumber.setText(borrowVehicle.getVehicle().getPoliceNumber());

                            if (borrowVehicle.getDriver() == null){
                                textViewDetailDriverName1.setVisibility(View.GONE);
                                textViewDetailDriverName2.setVisibility(View.GONE);
                                textViewDetailPhoneNumber1.setVisibility(View.GONE);
                                textViewDetailPhoneNumber2.setVisibility(View.GONE);
                            } else {
                                textViewDetailDriverName2.setText(borrowVehicle.getDriver().getFullName());
                                textViewDetailPhoneNumber2.setText(borrowVehicle.getDriver().getPhoneNumber());
                            }

                            textViewDetailDateOfFilling.setText(dateFormat.format(borrowVehicle.getDateOfFilling()));
                            textViewDetailNecessity.setText(borrowVehicle.getNecessity());
                            textViewDetailBorrowDate.setText(dateFormat.format(borrowVehicle.getBorrowDate()));
                            textViewDetailReturnDate.setText(dateFormat.format(borrowVehicle.getReturnDate()));
                            textViewDetailDestination.setText(borrowVehicle.getDestination());

                            Boolean borrowStatus = borrowVehicle.getBorrowStatus();
                            String stringBorrowStatus;
                            if (borrowStatus){
                                stringBorrowStatus = "DIPINJAM";
                            } else {
                                stringBorrowStatus = "DIKEMBALIKAN";
                            }
                            textViewDetailBorrowStatus.setText(stringBorrowStatus);

                            bottomSheetDialog.setContentView(bottomSheetView);
                            bottomSheetDialog.show();

                        } catch (NullPointerException e){
                            Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseOneData<BorrowVehicle>> call, Throwable t) {
                    Toast.makeText(context, "Error + t", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
