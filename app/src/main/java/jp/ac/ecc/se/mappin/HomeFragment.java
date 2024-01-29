package jp.ac.ecc.se.mappin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import jp.ac.ecc.se.mappin.databinding.ActivityHomeBinding;

public class HomeFragment extends Fragment {

    private ActivityHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Home home=
                new ViewModelProvider(this).get(Home.class);

        binding = ActivityHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        home.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}