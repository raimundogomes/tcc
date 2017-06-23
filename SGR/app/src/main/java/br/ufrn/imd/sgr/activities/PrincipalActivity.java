package br.ufrn.imd.sgr.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import br.ufrn.imd.sgr.utils.DetectaConexao;


/**
 * Created by netou on 27/08/2016.
 */
public class PrincipalActivity  extends Activity {

    private BroadcastReceiver receiver;

    private ActionBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bar =  getActionBar();

        if(savedInstanceState==null) {
          //  iniciarBroadCastReceiver();
        }

    }

    private void iniciarBroadCastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);

//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                DetectaConexao detectaConexao = new DetectaConexao(getApplicationContext());
//                if(detectaConexao.existeConexao()){
//                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#339999")));
//
//                }else{
//                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#991100")));
//                }
//            }
//        };
     //   registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

}
