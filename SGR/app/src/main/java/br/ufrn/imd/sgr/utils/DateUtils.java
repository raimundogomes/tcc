package br.ufrn.imd.sgr.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by thiago on 28/05/16.
 */
public class DateUtils {

    private static String[] meses = {"janeiro", "fevereiro", "março", "abril", "maio", "junho",
            "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"};

    public static String obterDataPorExtenso(Date data){
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(data);
        return calendar.get(Calendar.DAY_OF_MONTH) + " de " +
                meses[calendar.get(Calendar.MONTH)] + " de " +
                calendar.get(Calendar.YEAR);
    }

    public static String obterData(Date data){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.format(data);
    }

    public static String obterDataPorExtenso(int year, int monthOfYear, int dayOfMonth) {
        return dayOfMonth + " de " + meses[monthOfYear] + " de " + year;
    }

    public static Date obterData(int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        return calendar.getTime();
    }

    public static String obterDataServico(Date data){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(data);
    }
}
