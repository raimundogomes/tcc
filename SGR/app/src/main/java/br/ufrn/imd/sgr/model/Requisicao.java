package br.ufrn.imd.sgr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Neto on 02/06/2016.
 */
public class Requisicao implements Serializable {


    private static final String FORMATO_NUMERO = "%08d";

    private Paciente paciente;

    private String emailSolicitante;

    private SituacaoRequisicao status;

    private Long id;

    private Long numero;

    private Laboratorio laboratorio;

    private Date dataRequisicao;

    private Date dataUltimaModificacao;

    private Date dataEntrega;

    private List<Exame> exames = new ArrayList<Exame>();

    private Boolean internadoUltimas72Horas;
    private Boolean submetidoProcedimentoInvasivo;
    private Boolean fezUsoAntibiotico;

    private boolean temHemocultura = false;
    private boolean temUrocultura = false;
    private boolean temSecrecao = false;

    public Requisicao() {

    }

    public Requisicao(Date dataRequisicao, Paciente paciente, SituacaoRequisicao status) {
        this.dataRequisicao = dataRequisicao;
        this.paciente = paciente;
        this.status = status;
    }
    
    public Date getDataRequisicao() {
        return dataRequisicao;
    }

    public void setDataRequisicao(Date dataRequisicao) {
        this.dataRequisicao = dataRequisicao;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public SituacaoRequisicao getStatus() {
        return status;
    }

    public void setStatus(SituacaoRequisicao status) {
        this.status = status;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public Date getDataUltimaModificacao() {
        return dataUltimaModificacao;
    }

    public void setDataUltimaModificacao(Date dataFim) {
        this.dataUltimaModificacao = dataFim;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getNumeroFormatado() {
        return String.format(FORMATO_NUMERO, this.numero);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDataEntrega() {
        return dataEntrega;
    }

    public void setDataEntrega(Date dataEntrega) {
        this.dataEntrega = dataEntrega;
    }

    @Override
    public String toString() {
        return "Requisicao{" +
                "dataRequisicao=" + dataRequisicao +
                ", paciente=" + paciente +
                ", status=" + status +
                ", id=" + id +
                ", numero=" + numero +
                ", laboratorio=" + laboratorio +
                ", dataUltimaModificacao=" + dataUltimaModificacao +
                '}';
    }

    public void setSituacao(int situacao) {

        this.setStatus(SituacaoRequisicao.getStatusRequisicaoByCodigo(situacao));
    }

    public void setExames(List<Exame> exames) {
        this.exames = exames;
    }

    public List<Exame> getExames() {
        return this.exames;
    }

    public String getEmailSolicitante() {
        return emailSolicitante;
    }

    public String setEmailSolicitante(String email) {
        return this.emailSolicitante = email;
    }

    public void setInternadoUltimas72Horas(Boolean internadoUltimas72Horas) {
        this.internadoUltimas72Horas = internadoUltimas72Horas;
    }

    public Boolean getInternadoUltimas72Horas() {
        return internadoUltimas72Horas;
    }

    public void setSubmetidoProcedimentoInvasivo(Boolean submetidoProcedimentoInvasivo) {
        this.submetidoProcedimentoInvasivo = submetidoProcedimentoInvasivo;
    }

    public Boolean getSubmetidoProcedimentoInvasivo() {
        return submetidoProcedimentoInvasivo;
    }

    public void setFezUsoAntibiotico(Boolean fezUsoAntibiotico) {
        this.fezUsoAntibiotico = fezUsoAntibiotico;
    }

    public Boolean getUsouAntibiotico() {
        return fezUsoAntibiotico;
    }

    public boolean getTemHemocultura() {
        return temHemocultura;
    }


    public void setTemHemocultura(boolean temHemocultura) {
        this.temHemocultura = temHemocultura;
    }

    public boolean getTemUrocultura() {
        return temUrocultura;
    }

    public boolean getTemSecrecao() {
        return temSecrecao;
    }

    public void setTemUrocultura(boolean temUrocultura) {
        this.temUrocultura = temUrocultura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Requisicao that = (Requisicao) o;

        return numero.equals(that.numero);

    }

    @Override
    public int hashCode() {
        return numero.hashCode();
    }

    public void setTemSecrecao(boolean temSecrecao) {
        this.temSecrecao = temSecrecao;
    }
}
