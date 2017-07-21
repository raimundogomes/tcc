package br.ufrn.imd.sgr.TO;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListaRequisicaoTO implements Serializable{
	
	private Date dataUltimaAtualizacao;
	
	private String emailSolicitante;
	
	private Map<Long, Date> mapIdDataUltimaAtualizao;

	public Date getDataUltimaAtualizacao() {
		return dataUltimaAtualizacao;
	}

	public void setDataUltimaAtualizacao(Date dataUltimaAtualizacao) {
		this.dataUltimaAtualizacao = dataUltimaAtualizacao;
	}

	public String getEmailSolicitante() {
		return emailSolicitante;
	}

	public void setEmailSolicitante(String emailSolicitante) {
		this.emailSolicitante = emailSolicitante;
	}

	public Map<Long, Date> getMapIdDataUltimaAtualizao() {
		return mapIdDataUltimaAtualizao;
	}

	public void setMapIdDataUltimaAtualizao(Map<Long, Date> mapIdDataUltimaAtualizao) {
		this.mapIdDataUltimaAtualizao = mapIdDataUltimaAtualizao;
	}
}
