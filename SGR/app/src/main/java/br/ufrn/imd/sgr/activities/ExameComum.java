package br.ufrn.imd.sgr.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import br.ufrn.imd.sgr.utils.DateUtils;

/**
 * Created by netou on 01/07/2017.
 */

public class ExameComum {

    protected Button botaoData;

    protected Button botaoHora;

    protected Calendar dataColeta;

    public ExameComum(){
        dataColeta = Calendar.getInstance();
    }

    public Date getDataColeta() {
        return dataColeta.getTime();
    }

    public DatePickerDialog.OnDateSetListener dataPickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            dataColeta.set(Calendar.YEAR, year);
            dataColeta.set(Calendar.MONTH, monthOfYear);
            dataColeta.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            botaoData.setText(" " + DateUtils.obterDataDDMMYYY(dataColeta.getTime()));
        }
    };

    public TimePickerDialog.OnTimeSetListener timePickerListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int selectedHour,
                                      int selectedMinute) {

                    dataColeta.set(Calendar.HOUR_OF_DAY, selectedHour);
                    dataColeta.set(Calendar.MINUTE, selectedMinute);
                    botaoHora.setText(DateUtils.obterHora(dataColeta.getTime()));

                }
            };
}
