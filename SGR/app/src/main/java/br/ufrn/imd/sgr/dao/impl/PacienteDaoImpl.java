package br.ufrn.imd.sgr.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import br.ufrn.imd.sgr.dao.PacienteDao;
import br.ufrn.imd.sgr.model.Paciente;


/**
 * Created by netou on 08/11/2016.
 */

public class PacienteDaoImpl implements PacienteDao{

    public static String[] COLUNAS = new String[]{"ID", "NOME", "NOME_MAE", "DATA_NASCIMENTO", "CPF", "PRONTUARIO", "CNS", "TELEFONE", "EMAIL"};


    private BaseDao baseDao;

    public PacienteDaoImpl(Context contexto) {

        baseDao = BaseDao.getInstance(contexto);

    }

    public Paciente consultarPeloProntuario(long prontuario){

        Cursor cursor = baseDao.getDatabase().query("PACIENTE",COLUNAS,  "PRONTUARIO="+ prontuario, null, null, null, null);

        Paciente paciente = new Paciente();

        if (cursor.moveToFirst()){

            paciente = montarPaciente(cursor);

        }

        cursor.close();

        return paciente;
    }

    public Paciente consultarPeloId(long id){

        Cursor cursor = baseDao.getDatabase().query("PACIENTE",COLUNAS,  "ID="+ id, null, null, null, null);

        Paciente paciente = new Paciente();

        if (cursor.moveToFirst()){

            paciente = montarPaciente(cursor);

        }

        cursor.close();

        return paciente;
    }

    public Paciente insert(Paciente paciente) {

        ContentValues values = montarContentValues(paciente);

        long id = baseDao.getDatabase().insert(PACIENTE, null, values);

        paciente.setId(id);

        return paciente;
    }

    public void update(Paciente paciente) {

        ContentValues values = montarContentValues(paciente);

        baseDao.getDatabase().update(PACIENTE, values, "id = ?",
                new String[] { "" + paciente.getId()});

    }

    private ContentValues montarContentValues(Paciente paciente) {
        ContentValues values = new ContentValues(8);

        values.put("PRONTUARIO", paciente.getProntuario());
        values.put("NOME", paciente.getNome());
        values.put("NOME_MAE", paciente.getNomeMae());
        values.put("CPF", paciente.getCpf());
        values.put("CNS", paciente.getCns());
        values.put("DATA_NASCIMENTO", paciente.getDataNascimento());
        values.put("TELEFONE", paciente.getTelefone());
        values.put("EMAIL", paciente.getEmail());
        return values;
    }

    private Paciente montarPaciente(Cursor cursor) {
        Paciente p = new Paciente();
        p.setId(cursor.getLong(0));
        p.setNome(cursor.getString(1));
        p.setNomeMae(cursor.getString(2));
        p.setDataNascimento(cursor.getString(3));
        p.setCpf(cursor.getString(4));
        p.setProntuario(cursor.getLong(5));
        p.setCns(cursor.getString(6));
        p.setTelefone(cursor.getString(7));
        p.setEmail(cursor.getString(8));
        return p;
    }


}
