package br.ufrn.imd.sgr.dao;

import br.ufrn.imd.sgr.model.Paciente;


/**
 * Created by netou on 08/11/2016.
 */

public interface PacienteDao {

    final String PACIENTE = "paciente";

    Paciente consultarPeloProntuario(long prontuario);

    Paciente consultarPeloId(long id);

    Paciente insert(Paciente paciente);

    void update(Paciente paciente);
}
