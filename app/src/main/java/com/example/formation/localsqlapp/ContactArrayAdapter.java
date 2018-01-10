package com.example.formation.localsqlapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.formation.localsqlapp.model.Contact;

import java.util.List;
import java.util.Map;

/**
 * Created by Formation on 09/01/2018.
 */

public class ContactArrayAdapter extends ArrayAdapter {
    private Activity context;
    private List<Contact> data;
    private LayoutInflater inflater;


    public ContactArrayAdapter(@NonNull Context context, @NonNull List<Contact> data) {
        super(context, 0, data);
        this.data = data;
        this.context = (Activity) context;
        this.inflater = this.context.getLayoutInflater();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Instanciation de la vue
        View view = this.inflater.inflate(R.layout.list_view_contact,parent,false);

        //récupération des données

        Contact  contactData  = this.data.get(position);
        // Liaison entre les données et la Vue
        TextView nameTextView = view.findViewById(R.id.listTextViewName);
        nameTextView.setText(contactData.getName());

        TextView firstnameTextView = view.findViewById(R.id.listTextViewFirstName);
        firstnameTextView.setText(contactData.getFirst_name());

        TextView emailTextView = view.findViewById(R.id.listTextViewEmail);
        emailTextView.setText(contactData.getEmail());

        return view;
    }

}
