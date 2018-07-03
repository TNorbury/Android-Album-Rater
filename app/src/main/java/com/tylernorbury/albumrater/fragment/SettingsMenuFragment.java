package com.tylernorbury.albumrater.fragment;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.tylernorbury.albumrater.AlbumRaterApp;
import com.tylernorbury.albumrater.R;
import com.tylernorbury.albumrater.viewModel.AlbumViewModel;


/**
 * A simple {@link Fragment} subclass.
 * This fragment will display various settings that the user can interact with and change
 */
public class SettingsMenuFragment extends Fragment {


    public SettingsMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Give the clear database button a listener that will clear the
        // database when clicked
        Button clearDatabseButton = view.findViewById(R.id.btn_clear_database);
        clearDatabseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a pop-up dialog to confirm that the user wants to clear the database
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setMessage(R.string.dialog_clear_database_msg)
                        .setTitle(R.string.dialog_clear_database_title)
                        .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(AlbumRaterApp.getContext(), "Clearing Database", Toast.LENGTH_LONG).show();

                                // Since the user has confirmed that they want to clear the database, do it
                                AlbumViewModel model = ViewModelProviders.of(getActivity()).get(AlbumViewModel.class);
                                model.deleteAll();
                            }
                        })
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // We don't need to do anything special, just close the
                        // dialog
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.show();
            }
        });
    }
}
