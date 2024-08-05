package com.example.proyectoaula;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private DatePickerListener listener;

    public interface DatePickerListener {
        void onDateSet(String date);
    }

    public void setDatePickerListener(DatePickerListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Usa el calendario para obtener la fecha actual
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea un nuevo DatePickerDialog y retorna
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Formatea la fecha seleccionada
        String date = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
        if (listener != null) {
            listener.onDateSet(date);
        }
    }
}
