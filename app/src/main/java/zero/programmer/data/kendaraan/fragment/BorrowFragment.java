package zero.programmer.data.kendaraan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.BorrowDinasActivity;
import zero.programmer.data.kendaraan.activity.BorrowPersonalActivity;
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

        return  view;
    }

    private void bottomSheetDialogReportDinas(View view){

        bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getContext())
                .inflate(R.layout.bottom_sheet_report_dinas, view.findViewById(R.id.bottom_sheet_report_dinas));

        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }
}