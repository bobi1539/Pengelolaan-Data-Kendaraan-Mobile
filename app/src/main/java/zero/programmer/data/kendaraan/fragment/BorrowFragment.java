package zero.programmer.data.kendaraan.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zero.programmer.data.kendaraan.R;
import zero.programmer.data.kendaraan.activity.BorrowDinasActivity;
import zero.programmer.data.kendaraan.activity.BorrowPersonalActivity;

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

    private CardView cardViewBorrowDinas, cardViewBorrowPersonal;

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

        // get view layout
        cardViewBorrowDinas = view.findViewById(R.id.card_borrow_dinas);
        cardViewBorrowPersonal = view.findViewById(R.id.card_borrow_personal);

        // on click card
        cardViewBorrowDinas.setOnClickListener(v -> startActivity(new Intent(getContext(), BorrowDinasActivity.class)));
        cardViewBorrowPersonal.setOnClickListener(v -> startActivity(new Intent(getContext(), BorrowPersonalActivity.class)));

        return  view;
    }
}