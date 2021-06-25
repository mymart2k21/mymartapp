package com.joshi.mymart.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.joshi.mymart.R;
import com.joshi.mymart.adapter.NavCategoryAdaptor;
import com.joshi.mymart.adapter.PopularAdapters;
import com.joshi.mymart.models.NavCategoryModel;
import com.joshi.mymart.models.PopularModel;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    FirebaseFirestore db;


    RecyclerView recyclerView;
    List<NavCategoryModel> categoryModelList;
    NavCategoryAdaptor navCategoryAdaptor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);

        db = FirebaseFirestore.getInstance();
        recyclerView = root.findViewById(R.id.cat_rec);


        //Popular Item

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL,false));
        categoryModelList = new ArrayList<>();
        navCategoryAdaptor = new NavCategoryAdaptor(getActivity(),categoryModelList);
        recyclerView.setAdapter(navCategoryAdaptor);


        db.collection("NavCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavCategoryModel navCategoryModel = document.toObject(NavCategoryModel.class);
                                categoryModelList.add(navCategoryModel);
                                navCategoryAdaptor.notifyDataSetChanged();

//                                progressBar.setVisibility(View.GONE);
//                                scrollView.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(getActivity(), "ERROR"+task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


        return root;
    }
}