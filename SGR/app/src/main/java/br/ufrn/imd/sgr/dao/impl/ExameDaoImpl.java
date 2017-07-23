package br.ufrn.imd.sgr.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.ufrn.imd.sgr.dao.ExameDao;
import br.ufrn.imd.sgr.model.Exame;
import br.ufrn.imd.sgr.model.SituacaoExame;
import br.ufrn.imd.sgr.model.TipoColeta;
import br.ufrn.imd.sgr.model.TipoExame;
import br.ufrn.imd.sgr.model.TipoMaterial;


public class ExameDaoImpl implements ExameDao{

    public static final String EXAME = "exame";
    public static String[] COLUNAS = new String[]{"ID_EXAME","NUMERO_EXAME",  "DATA_COLETA", "ID_SITUACAO", "ID_REQUISICAO",
            "TIPO_EXAME", "TIPO_COLETA", "TIPO_MATERIAL", "RESULTADO"};


    private BaseDao baseDao;

    public ExameDaoImpl(Context contexto) {

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

        ContentValues values = new ContentValues(8);

        values.put("ID_SITUACAO", exame.getSituacaoExame().getCodigo());
        values.put("RESULTADO", exame.getResultado());

        baseDao.getDatabase().update(EXAME, values, "id = ?",
                new String[] { "" + exame.getId()});

    }

    private ContentValues montarContentValues(Exame exame) {
        ContentValues values = new ContentValues(8);

        values.put("NUMERO_EXAME", exame.getNumero());
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
        exame.setNumero(cursor.getLong(1));
        exame.setDataColeta(baseDao.montarData(cursor.getString(2)));
        exame.setSituacaoExame(SituacaoExame.getSituacaoExamePeloCodigo(cursor.getInt(3)));
        exame.setIdRequisicao(cursor.getLong(4));
        exame.setTipoExame(TipoExame.getTipoExamePeloCodigo(cursor.getInt(5)));
        exame.setTipoColeta(TipoColeta.getTipoColetaPeloCodigo(cursor.getInt(6)));
        exame.setTipoMaterial(TipoMaterial.getTipoMaterialPeloCodigo(cursor.getInt(7)));
        exame.setResultadoCompleto(cursor.getString(8));

        return exame;
    }

    public void delete(long idRequisicao) {

        int alt = baseDao.getDatabase().delete(EXAME, "ID_REQUISICAO=?",
                new String[] { String.valueOf(idRequisicao) });

    }
}
