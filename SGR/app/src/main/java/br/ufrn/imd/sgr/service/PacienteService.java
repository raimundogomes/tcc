package br.ufrn.imd.sgr.service;

import android.app.MediaRouteButton;
import android.widget.ProgressBar;

import java.util.List;

import br.ufrn.imd.sgr.model.Paciente;

/**
 * Created by netou on 25/06/2017.
 */

public interface PacienteService {

    List<Paciente> pesquisarPaciente(String prontuario, ProgressBar progressBar);

    List<Paciente> pesquisarPacientesPeloNome(String nome, ProgressBar progressBar);

    void update(Paciente paciente);

    Paciente consultarPeloProntuario(Long prontuario);

    Paciente insert(Paciente paciente);
}
