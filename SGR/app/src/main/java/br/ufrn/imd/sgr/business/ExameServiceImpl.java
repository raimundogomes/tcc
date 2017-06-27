package br.ufrn.imd.sgr.business;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.sgr.dao.ExameDao;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.service.ExameService;
import br.ufrn.imd.sgr.service.PacienteService;
import br.ufrn.imd.sgr.utils.Constantes;
import br.ufrn.imd.sgr.utils.VolleyApplication;

/**
 * Created by netou on 25/06/2017.
 */

public class ExameServiceImpl implements ExameService {

    private ExameDao exameDao;

    public ExameServiceImpl(Context contexto){
        exameDao = new ExameDao(contexto);
    }

    public List<Exame> consultarExames(long idRequisicao){

        return exameDao.consultarPeloIdRequisicao(idRequisicao);
    }



}
