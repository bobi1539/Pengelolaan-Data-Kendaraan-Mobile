package zero.programmer.data.kendaraan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.BorrowDinasActivity;
import zero.programmer.data.kendaraan.activity.BorrowPersonalActivity;
import zero.programmer.data.kendaraan.api.GetConnection;
import zero.programmer.data.kendaraan.apikey.ApiKeyData;
import zero.programmer.data.kendaraan.entitites.BorrowVehicle;
import zero.programmer.data.kendaraan.response.ResponseListData;
import zero.programmer.data.kendaraan.session.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BorrowFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BorrowFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private CardView cardViewBorrowDinas, cardViewBorrowPersonal, cardViewReportDinas, cardViewReportPersonal;
    private BottomSheetDialog bottomSheetDialog;
    private EditText editTextMonthDinas, editTextYearDinas, editTextMonthPersonal, editTextYearPersonal;
    private Button buttonPrintReportDinas, buttonPrintReportPersonal;

    private String monthDinas, yearDinas, monthPersonal, yearPersonal;

    private List<BorrowVehicle> listBorrow = new ArrayList<>();

    public BorrowFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BorrowFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BorrowFragment newInstance(String param1, String param2) {
        BorrowFragment fragment = new BorrowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_borrow, container, false);

        SessionManager sessionManager = new SessionManager(getContext());

        // get view layout
        cardViewBorrowDinas = view.findViewById(R.id.card_borrow_dinas);
        cardViewBorrowPersonal = view.findViewById(R.id.card_borrow_personal);
        cardViewReportDinas = view.findViewById(R.id.card_borrow_dinas_report);
        cardViewReportPersonal = view.findViewById(R.id.card_borrow_personal_report);

        // on click card
        cardViewBorrowDinas.setOnClickListener(v -> startActivity(new Intent(getContext(), BorrowDinasActivity.class)));
        cardViewBorrowPersonal.setOnClickListener(v -> startActivity(new Intent(getContext(), BorrowPersonalActivity.class)));

        // cek jika tidak admin dan kabid
        if (sessionManager.getRoleId().equals("KARYAWAN")){
            cardViewReportDinas.setVisibility(View.GONE);
            cardViewReportPersonal.setVisibility(View.GONE);
        }

        cardViewReportDinas.setOnClickListener(v -> bottomSheetDialogReportDinas(view));
        cardViewReportPersonal.setOnClickListener(v -> bottomSheetDialogReportPersonal(view));

        return  view;
    }

    private void bottomSheetDialogReportDinas(View view){

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext())
                .inflate(R.layout.bottom_sheet_report_dinas, view.findViewById(R.id.bottom_sheet_report_dinas));

        editTextMonthDinas = bottomSheetView.findViewById(R.id.et_report_dinas_month);
        editTextYearDinas = bottomSheetView.findViewById(R.id.et_report_dinas_year);
        buttonPrintReportDinas = bottomSheetView.findViewById(R.id.button_print_report_dinas);

        buttonPrintReportDinas.setOnClickListener(v -> printReportDinas());





        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void bottomSheetDialogReportPersonal(View view){
        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext())
                .inflate(R.layout.bottom_sheet_report_personal, view.findViewById(R.id.bottom_sheet_report_personal));

        editTextMonthPersonal = bottomSheetView.findViewById(R.id.et_report_personal_month);
        editTextYearPersonal = bottomSheetView.findViewById(R.id.et_report_personal_year);
        buttonPrintReportPersonal = bottomSheetView.findViewById(R.id.button_print_report_personal);

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private void printReportDinas(){
        if (validateInputDinas()){
            String dateOfFillingDinas = yearDinas + "-" + monthDinas;
            Call<ResponseListData<BorrowVehicle>> getBorrowVehicle = GetConnection.apiRequest.listBorrowVehicleDinasLike(
                    ApiKeyData.getApiKey(), dateOfFillingDinas
            );
            getBorrowVehicle.enqueue(new Callback<ResponseListData<BorrowVehicle>>() {
                @Override
                public void onResponse(Call<ResponseListData<BorrowVehicle>> call, Response<ResponseListData<BorrowVehicle>> response) {

                    if (response.code() == 404){
                        Toast.makeText(getContext(), "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                    } else {
                        try {

                            listBorrow = response.body().getData();
                            createPdfReportDinas();
                            bottomSheetDialog.dismiss();

                        } catch (Exception e){
                            Toast.makeText(getContext(), "Error : " + e, Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseListData<BorrowVehicle>> call, Throwable t) {
                    Toast.makeText(getContext(), "Error : " + t, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private boolean validateInputDinas(){
        if (editTextMonthDinas.getText().toString().trim().equals("")){
            editTextMonthDinas.setError("Bulan tidak boleh kosong");
            return false;
        } else if (editTextYearDinas.getText().toString().trim().equals("")){
            editTextYearDinas.setError("Tahun tidak boleh kosong");
            return false;
        } else {
            monthDinas = editTextMonthDinas.getText().toString();
            yearDinas = editTextYearDinas.getText().toString();

            if (Integer.parseInt(monthDinas) < 1 || Integer.parseInt(monthDinas) > 12){
                Toast.makeText(getContext(), "Input bulan tidak valid", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (Integer.parseInt(yearDinas) < 1000 || Integer.parseInt(yearDinas) > 3000){
                Toast.makeText(getContext(), "Input tahun tidak valid", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }
    }

    private void createPdfReportDinas() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "Laporan-Keperluan-Dinas.pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A4.rotate());
        document.setMargins(30,30,30,30);
        document.setFontSize(6);

        Paragraph paragraph1 = new Paragraph("Laporan Peminjaman Kendaraan Keperluan Dinas").setBold().setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER);

        Paragraph paragraph2 = new Paragraph("Bulan " + monthDinas + "-" + yearDinas);

        float[] width = {10, 30};
        Table table1 = new Table(width);

        table1.addCell(new Cell().add(new Paragraph("No")));
        table1.addCell(new Cell().add(new Paragraph("Nama")));

        for (int x = 0; x < 10; x++){
            table1.addCell(new Cell().add(new Paragraph("hi")));
            table1.addCell(new Cell().add(new Paragraph("hi")));
        }

        document.add(paragraph1);
        document.add(paragraph2);
        document.add(table1);

        document.close();
        System.out.println("ukuran : " + listBorrow.size());
        Toast.makeText(getContext(), "Berhasil Download di Folder Download", Toast.LENGTH_SHORT).show();
    }
}