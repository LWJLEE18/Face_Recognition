package com.example.academic_festival;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

import com.google.firebase.FirebaseApp;

import org.jetbrains.annotations.Nullable;

public class Smartdoor_setting extends PreferenceFragmentCompat {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    Smartdoormain2 smartdoormain2;
    SwitchPreference Unknown_notifi, Doorstatus_notifi;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        smartdoormain2 = (Smartdoormain2) getActivity();
        FirebaseApp.initializeApp(getActivity());

    }
    @Override
    public void onDetach() {
        super.onDetach();
        smartdoormain2 = null;
    }
    public void onCreatePreferences(Bundle savedInstanceState, String rootkey){
        addPreferencesFromResource(R.xml.preferences);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = preferences.edit();
        Unknown_notifi = findPreference("Unknown_notifi");
        Doorstatus_notifi = findPreference("doorstatus_notifi");
        Unknown_notifi.setDefaultValue(true);
        Doorstatus_notifi.setDefaultValue(true);

    }


}
