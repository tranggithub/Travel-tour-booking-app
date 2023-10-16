package com.example.travel_tour_booking_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.travel_tour_booking_app.databinding.ActivityHomeBinding;
import com.example.travel_tour_booking_app.databinding.ActivityMainBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.btn_home)
            {
                replaceFragment(new HomeFragment());
            }
            if (item.getItemId() == R.id.btn_search)
            {
                replaceFragment(new SearchFragment());
            }
            if (item.getItemId() == R.id.btn_discover)
            {
                replaceFragment(new DiscoverFragment());
            }
            if (item.getItemId() == R.id.btn_user)
            {
                replaceFragment(new UserFragment());
            }
            return true;
        });


    }
    private void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}