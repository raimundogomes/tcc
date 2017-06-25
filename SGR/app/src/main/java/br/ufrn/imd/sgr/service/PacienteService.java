package br.ufrn.imd.sgr.service;

import java.util.List;

import br.ufrn.imd.sgr.model.Paciente;

/**
 * Created by netou on 25/06/2017.
 */

public interface PacienteService {

    List<Paciente> pesquisarPaciente(String prontuario);

    List<Paciente> pesquisarPacientesPeloNome(String nome);
}
