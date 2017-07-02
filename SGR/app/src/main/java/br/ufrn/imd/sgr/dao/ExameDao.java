package br.ufrn.imd.sgr.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.Laboratorio;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;
import br.ufrn.imd.sgr.model.SituacaoExame;
import br.ufrn.imd.sgr.model.TipoColeta;
import br.ufrn.imd.sgr.model.TipoExame;
import br.ufrn.imd.sgr.model.TipoMaterial;



public class ExameDao {

    public static final String EXAME = "exame";
    public static String[] COLUNAS = new String[]{"ID_EXAME", "DATA_COLETA", "ID_SITUACAO", "ID_REQUISICAO",
            "TIPO_EXAME", "TIPO_COLETA", "TIPO_MATERIAL", "RESULTADO"};


    private BaseDao baseDao;

    public ExameDao(Context contexto) {

        baseDao = BaseDao.getInstance(contexto);

    }


    public Exame consultarPeloId(long id){

        Cursor cursor = baseDao.getDatabase().query(EXAME, COLUNAS,  "ID="+ id, null, null, null, null);

        Exame exame = new Exame();

        if (cursor.moveToFirst()){

            exame = montarExame(cursor);

        }

        cursor.close();

        return exame;
    }

    public List<Exame> consultarPeloIdRequisicao(long id){


        String rawQuery = "SELECT * FROM EXAME WHERE ID_REQUISICAO = " + id ;

        Cursor cursor = baseDao.getDatabase().rawQuery(rawQuery, null );

        List<Exame> exames = new ArrayList<Exame>();
        if (cursor.moveToFirst()){
            do{
                Exame exame = montarExame(cursor);
                exames.add(exame);
            }while(cursor.moveToNext());
        }

        cursor.close();

        return exames;
    }

    public Exame insert(Exame exame) {

        ContentValues values = montarContentValues(exame);

        long id = baseDao.getDatabase().insert(EXAME, null, values);

        exame.setId(id);

        return exame;
    }

    public void update(Exame exame) {

        ContentValues values = montarContentValues(exame);

        baseDao.getDatabase().update(EXAME, values, "id = ?",
                new String[] { "" + exame.getId()});

    }

    private ContentValues montarContentValues(Exame exame) {
        ContentValues values = new ContentValues(8);

        values.put("DATA_COLETA", BaseDao.FORMATE_DATE.format(exame.getDataColeta()));
        values.put("ID_SITUACAO", exame.getSituacaoExame().getCodigo());
        values.put("ID_REQUISICAO", exame.getIdRequisicao());
        values.put("TIPO_EXAME", exame.getTipoExame().getCodigo());
        values.put("TIPO_COLETA", exame.getTipoColeta().getCodigo());
        values.put("TIPO_MATERIAL", exame.getTipoMaterial().getCodigo());
        values.put("RESULTADO", exame.getResultado());
        return values;
    }

    private Exame montarExame(Cursor cursor) {

        Exame exame = new Exame();

        exame.setId(cursor.getLong(0));
        exame.setDataColeta(baseDao.montarData(cursor.getString(1)));
        exame.setSituacaoExame(SituacaoExame.getSituacaoExamePeloCodigo(cursor.getInt(2)));
        exame.setIdRequisicao(cursor.getLong(3));
        exame.setTipoExame(TipoExame.getTipoExamePeloCodigo(cursor.getInt(4)));
        exame.setTipoColeta(TipoColeta.getTipoColetaPeloCodigo(cursor.getInt(5)));
        exame.setTipoMaterial(TipoMaterial.getTipoMaterialPeloCodigo(cursor.getInt(6)));
        exame.setResultadoCompleto(cursor.getString(7));

        return exame;
    }

    public void delete(long idRequisicao) {

        int alt = baseDao.getDatabase().delete(PacienteDao.PACIENTE, "ID_REQUISICAO=?",
                new String[] { String.valueOf(idRequisicao) });

    }
}
