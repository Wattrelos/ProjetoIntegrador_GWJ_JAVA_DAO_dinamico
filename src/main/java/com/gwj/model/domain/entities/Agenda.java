package com.gwj.model.domain.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gwj.model.domain.IEntity;

public class Agenda implements IEntity{

    // Atributos simples;
	@Id
	protected Long          id  = 0L; // Inicializa variável de índice
    protected String nome;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Avisa ao módulo json utilizar este formato.
    protected LocalDateTime data;
	
    // Atributos complexos:
    protected Profissional proficional;
    protected Cliente cliente;
    private   List<Servico> listaServico = new ArrayList<>(); // Declaração da lista de Serviços.
	
	
	// Métodos
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public LocalDateTime getData() {
		return data;
	}
	public void setData(LocalDateTime data) {
		this.data = data;
	}
	public Profissional getProficional() {
		return proficional;
	}
	public void setProficional(Profissional proficional) {
		this.proficional = proficional;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<Servico> getListaServico() {
		return listaServico;
	}
	public void setListaServico(List<Servico> listaServico) {
		this.listaServico = listaServico;
	}
}
