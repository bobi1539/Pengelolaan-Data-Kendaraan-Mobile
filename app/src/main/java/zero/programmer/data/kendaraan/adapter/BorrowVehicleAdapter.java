package zero.programmer.data.kendaraan.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.EditBorrowVehicleActivity;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.BorrowVehicle;
import zero.programmer.data.kendaraan.response.ResponseOneData;
import zero.programmer.data.kendaraan.session.SessionManager;
import zero.programmer.data.kendaraan.utils.RoleId;

public class BorrowVehicleAdapter extends RecyclerView.Adapter<BorrowVehicleAdapter.HolderData>{

    private final Context context;
    private List<BorrowVehicle> listBorrowVehicle;

    private Integer idBorrow;
    private String fullName, employeeNumber, position, vehicleName, merk, policeNumber, driverName,
            phoneNumber, necessity, borrowDate, returnDate, destination, borrowStatusVariable, borrowType;

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
        Button buttonEditBorrow, buttonDeleteBorrow, buttonPrintBorrow;

        SessionManager sessionManager;

        public HolderData(@NonNull View itemView) {
            super(itemView);

            // inject session manager
            sessionManager = new SessionManager(context);

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
                            buttonEditBorrow = bottomSheetView.findViewById(R.id.button_edit_borrow_vehicle);
                            buttonDeleteBorrow = bottomSheetView.findViewById(R.id.button_delete_borrow_vehicle);
                            buttonPrintBorrow = bottomSheetView.findViewById(R.id.button_print_borrow_vehicle);

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

                            // set to variable
                            fullName = textViewDetailFullName.getText().toString();
                            employeeNumber = textViewDetailEmployeeNumber.getText().toString();
                            position = textViewDetailPosition.getText().toString();
                            vehicleName = textViewDetailVehicleName.getText().toString();
                            merk = textViewDetailMerk.getText().toString();
                            policeNumber = textViewDetailPoliceNumber.getText().toString();

                            if (borrowVehicle.getBorrowType().equals("DINAS")){
                                driverName = textViewDetailDriverName2.getText().toString();
                                phoneNumber = textViewDetailPhoneNumber2.getText().toString();
                            }
                            necessity = textViewDetailNecessity.getText().toString();
                            borrowDate = textViewDetailBorrowDate.getText().toString();
                            returnDate = textViewDetailReturnDate.getText().toString();
                            destination = textViewDetailDestination.getText().toString();
                            borrowStatusVariable = textViewDetailBorrowStatus.getText().toString();
                            borrowType = borrowVehicle.getBorrowType();

                            // cek jika role id bukan admin
                            if (!sessionManager.getRoleId().equals(RoleId.ADMIN.toString())){
                                buttonEditBorrow.setVisibility(View.GONE);
                                buttonDeleteBorrow.setVisibility(View.GONE);
                            }


                            //click button
                            buttonEditBorrow.setOnClickListener(v -> editBorrowVehicle());
                            buttonDeleteBorrow.setOnClickListener(v -> deleteBorrowVehicle());
                            buttonPrintBorrow.setOnClickListener(v -> {
                                if (borrowVehicle.getBorrowType().equals("DINAS")) {
                                    try {
                                        printBorrowVehicleDinas(
                                                fullName, employeeNumber, position, borrowVehicle.getUser().getWorkUnit(),
                                                borrowDate, destination, driverName, policeNumber,
                                                dateFormat.format(borrowVehicle.getDateOfFilling())
                                        );
                                    } catch (FileNotFoundException e){
                                        Toast.makeText(context, "File not found", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    printBorrowVehiclePersonal();
                                }
                            });

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

        private void editBorrowVehicle(){
            Intent intent = new Intent(context, EditBorrowVehicleActivity.class);
            intent.putExtra("idBorrow", String.valueOf(idBorrow));
            intent.putExtra("fullName", fullName);
            intent.putExtra("employeeNumber", employeeNumber);
            intent.putExtra("position", position);
            intent.putExtra("vehicleName", vehicleName);
            intent.putExtra("merk", merk);
            intent.putExtra("policeNumber", policeNumber);
            intent.putExtra("driverName", driverName);
            intent.putExtra("phoneNumber", phoneNumber);
            intent.putExtra("necessity", necessity);
            intent.putExtra("borrowDate", borrowDate);
            intent.putExtra("returnDate", returnDate);
            intent.putExtra("destination", destination);
            intent.putExtra("borrowStatusVariable", borrowStatusVariable);
            intent.putExtra("borrowType", borrowType);
            context.startActivity(intent);
            bottomSheetDialog.dismiss();
        }

        private void deleteBorrowVehicle(){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Yakin data dihapus?");
            alertDialog.setCancelable(true);

            alertDialog.setPositiveButton("Tidak", (dialog, which) -> dialog.dismiss());

            alertDialog.setNegativeButton("Ya", (dialog, which) -> {

                Call<ResponseOneData<BorrowVehicle>> deleteBorrowVehicle = GetConnection.apiRequest
                        .deleteBorrowVehicle(ApiKeyData.getApiKey(), idBorrow);

                deleteBorrowVehicle.enqueue(new Callback<ResponseOneData<BorrowVehicle>>() {
                    @Override
                    public void onResponse(Call<ResponseOneData<BorrowVehicle>> call, Response<ResponseOneData<BorrowVehicle>> response) {
                        if (response.code() == 400){
                            Toast.makeText(context, "Data tidak bisa dihapus karena kendaraan masih belum dikembalikan", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404){
                            Toast.makeText(context, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 200){
                            try{

                                Toast.makeText(context, response.body().getMessages().get(0), Toast.LENGTH_SHORT).show();
                                bottomSheetDialog.dismiss();

                            }catch (NullPointerException e){
                                Toast.makeText(context, "Error : " + e, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseOneData<BorrowVehicle>> call, Throwable t) {
                        Toast.makeText(context, "Error : " + t, Toast.LENGTH_SHORT).show();
                    }
                });

            });
            alertDialog.show();
        }

        private void printBorrowVehicleDinas(
                String printName, String printEmployeeNumber, String printPosition, String printWorkUnit,
                String printDate, String printDestination, String printDriverName, String printPoliceNumber,
                String printDateOfFilling
        ) throws FileNotFoundException {

            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(pdfPath, "Keperluan-Dinas.pdf");
            OutputStream outputStream = new FileOutputStream(file);

            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);

            pdfDocument.setDefaultPageSize(PageSize.A6);
            document.setMargins(30,30,30,30);
            document.setFontSize(6);

            Drawable drawable = context.getDrawable(R.drawable.image_logo);
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

            byte[] bitmapData = stream.toByteArray();

            ImageData imageData = ImageDataFactory.create(bitmapData);
            Image image = new Image(imageData);
            image.setWidth(60);

            float[] columnWidthTitle = {65,180,65};
            Table table1 = new Table(columnWidthTitle);
            table1.setBorder(Border.NO_BORDER);

            // table1 -- 01
            table1.addCell(new Cell().add(image).setBorder(Border.NO_BORDER));
            table1.addCell(new Cell().add(new Paragraph("SURAT JALAN KENDARAAN DINAS").setBold()
                    .setHorizontalAlignment(HorizontalAlignment.CENTER).setFontSize(7).setUnderline())
                    .setBorder(Border.NO_BORDER).setVerticalAlignment(VerticalAlignment.BOTTOM));
            table1.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

            // paragraf 1
            Paragraph paragraph1 = new Paragraph("Yang bertanda tangan di bawah ini :")
                    .setTextAlignment(TextAlignment.LEFT);

            // ------table 2---------------
            float[] width2 = {70,10,100};
            Table table2 = new Table(width2);
            table2.addCell(new Cell().add(new Paragraph("Nama")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(printName)).setBorder(Border.NO_BORDER));

            table2.addCell(new Cell().add(new Paragraph("NPK")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(printEmployeeNumber)).setBorder(Border.NO_BORDER));

            table2.addCell(new Cell().add(new Paragraph("Jabatan")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(printPosition)).setBorder(Border.NO_BORDER));

            table2.addCell(new Cell().add(new Paragraph("Unit Kerja")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table2.addCell(new Cell().add(new Paragraph(printWorkUnit)).setBorder(Border.NO_BORDER));

            // ----------table 3-----------------
            float[] width3 = {70,10,100};
            Table table3 = new Table(width3);
            table3.addCell(new Cell().add(new Paragraph("Hari / Tgl Perjalanan")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(printDate)).setBorder(Border.NO_BORDER));

            table3.addCell(new Cell().add(new Paragraph("Tujuan")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(printDestination)).setBorder(Border.NO_BORDER));

            table3.addCell(new Cell().add(new Paragraph("Nama Pengemudi")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(printDriverName)).setBorder(Border.NO_BORDER));

            table3.addCell(new Cell().add(new Paragraph("No Polisi Kendaraan")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(":")).setBorder(Border.NO_BORDER));
            table3.addCell(new Cell().add(new Paragraph(printPoliceNumber)).setBorder(Border.NO_BORDER));


            // -----generate qr code-------
            BarcodeQRCode qrCode = new BarcodeQRCode(printName + ", " + printEmployeeNumber + ", " + printPosition);
            PdfFormXObject qrCodeObject = qrCode.createFormXObject(ColorConstants.BLACK, pdfDocument);
            Image qrCodeImage = new Image(qrCodeObject).setWidth(50).setHorizontalAlignment(HorizontalAlignment.LEFT);

            // -----------table 4-------------
            float[] width4 = {90,90,90};
            Table table4 = new Table(width4);
            table4.addCell(new Cell().add(new Paragraph("dikeluarkan tgl : " + printDateOfFilling)).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("diketahui, \n Bidang USDM")).setBorder(Border.NO_BORDER));

            table4.addCell(new Cell().add(qrCodeImage).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

            table4.addCell(new Cell().add(new Paragraph("............................")).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("............................")).setBorder(Border.NO_BORDER));

            table4.addCell(new Cell().add(new Paragraph(printName)).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            table4.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));


            document.add(table1);
            document.add(new Paragraph("\n\n"));
            document.add(paragraph1);
            document.add(table2);
            document.add(new Paragraph("\nAkan menggunakan kendaraan dinas untuk " +
                    "kegiatan operasional kantor / Dinas dengan rincian sebagai berikut :"));
            document.add(table3);
            document.add(new Paragraph("Surat jalan kendaraan ini dibuat untuk dipergunakan seperlunya.\n"));
            document.add(table4);

            document.close();
            Toast.makeText(context, "Berhasil Download di Folder Download", Toast.LENGTH_SHORT).show();

        }

        private void printBorrowVehiclePersonal(){

        }

    }
}
