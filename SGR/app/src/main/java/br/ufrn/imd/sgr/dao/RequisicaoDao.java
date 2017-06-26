package br.ufrn.imd.sgr.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufrn.imd.sgr.model.Laboratorio;
import br.ufrn.imd.sgr.model.Paciente;
import br.ufrn.imd.sgr.model.Requisicao;

/**
 * Created by netou on 07/11/2016.
 */

public class RequisicaoDao {

    public static final String REQUISICAO = "requisicao";

    public static String[] COLUNAS_REQUISICAO = new String[]{"ID", "NUMERO", "DATA_REQUISICAO", "ID_SITUACAO", "ID_LABORATORIO",
            "ID_PACIENTE", "DATA_COLETA", "DATA_ULTIMA_ATUALIZACAO"};

    private BaseDao baseDao;

    public RequisicaoDao(Context contexto) {

        baseDao = BaseDao.getInstance(contexto);

    }

    public void insert(Requisicao requisicao) {

        ContentValues values = new ContentValues(7);

        values.put("NUMERO", requisicao.getNumero());
        values.put("ID_SITUACAO", requisicao.getStatus().getCodigo());
        values.put("ID_LABORATORIO", requisicao.getLaboratorio().getId());

        values.put("ID_PACIENTE", requisicao.getPaciente().getId());

        values.put("DATA_REQUISICAO", BaseDao.FORMATE_DATE.format(requisicao.getDataRequisicao()));

        values.put("DATA_COLETA", BaseDao.FORMATE_DATE.format(requisicao.getDataRequisicao()));

        values.put("DATA_ULTIMA_ATUALIZACAO", BaseDao.FORMATE_DATE.format(requisicao.getDataRequisicao()));

        long id = baseDao.getDatabase().insert(REQUISICAO, null, values);

        requisicao.setId(id);/////


    }

    public void delete(Requisicao requisicao) {

        int alt = baseDao.getDatabase().delete(REQUISICAO, "ID=?",
                new String[] { String.valueOf(requisicao.getId()) });

    }

    public List<Requisicao> listar(){


        String rawQuery = "SELECT * FROM "+ REQUISICAO +" INNER JOIN " + PacienteDao.PACIENTE
                + " ON " + REQUISICAO+ ".ID_PACIENTE" + " = " + PacienteDao.PACIENTE +".ID";

        Cursor cursor = baseDao.getDatabase().rawQuery(rawQuery, null );

        List<Requisicao> requisicoes = new ArrayList<Requisicao>();
        if (cursor.moveToFirst()){
            do{
                Requisicao requisicao = new Requisicao();

                requisicao.setId(cursor.getLong(0));
                requisicao.setNumero(cursor.getInt(cursor.getColumnIndex("NUMERO")));

                Log.d("Teste", cursor.getInt(cursor.getColumnIndex("NUMERO"))+"");
                Log.d("Teste", cursor.getString(cursor.getColumnIndex("DATA_REQUISICAO"))+"");



                requisicao.setDataRequisicao(montarData(cursor.getString(cursor.getColumnIndex("DATA_REQUISICAO"))));
                requisicao.setSituacao(cursor.getInt(cursor.getColumnIndex("ID_SITUACAO")));
                requisicao.setLaboratorio(Laboratorio.getLaboratorioById(cursor.getInt(cursor.getColumnIndex("ID_LABORATORIO"))));
                requisicao.setDataUltimaModificacao(montarData(cursor.getString(cursor.getColumnIndex("DATA_ULTIMA_ATUALIZACAO"))));

                Paciente paciente = montarPaciente(cursor);

                requisicao.setPaciente(paciente);

                requisicoes.add(requisicao);
            }while(cursor.moveToNext());
        }
        cursor.close();
        return requisicoes;
    }

    private Paciente montarPaciente(Cursor cursor) {
        Paciente p = new Paciente();
        p.setId(cursor.getLong(cursor.getColumnIndex("ID_PACIENTE")));
        p.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
        p.setNomeMae(cursor.getString(cursor.getColumnIndex("NOME_MAE")));
        p.setDataNascimento(cursor.getString(cursor.getColumnIndex("DATA_NASCIMENTO")));
        p.setCpf(cursor.getString(cursor.getColumnIndex("CPF")));
        p.setProntuario(cursor.getLong(cursor.getColumnIndex("PRONTUARIO")));
        p.setCns(cursor.getString(cursor.getColumnIndex("CNS")));
        p.setTelefone(cursor.getString(cursor.getColumnIndex("TELEFONE")));
        p.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
        return p;
    }

    private Date montarData(String string) {
        Date data;

        try {
            data = BaseDao.FORMATE_DATE.parse(string );
        } catch (ParseException e) {
            e.printStackTrace();
            data = new Date();

        }
        return data;
    }

    public void cancelar(Requisicao requisicao) {

        ContentValues values = montarContentValues(requisicao);

        baseDao.getDatabase().update(REQUISICAO, values, "id = ?",
                new String[] { "" + requisicao.getId()});

    }

    private ContentValues montarContentValues(Requisicao requisicao) {
        ContentValues values = new ContentValues(1);

        values.put("ID_SITUACAO", requisicao.getStatus().getCodigo());

        return values;
    }

}
