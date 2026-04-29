package com.gwj.model.domain.entities;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario { // A classe Cliente é uma especialização de Usuario, herdando atributos e métodos
	
	// Atributos primitivos:
	protected String nome;
	protected String sobrenome;
	protected String telefone;
	protected String cpf;
	protected String observacao;

	// Atributos complexos:
	private List<Endereco> listaEndereco = new ArrayList<>(); // Declaração da lista de Endereço. Note que inicializei a lista com new ArrayList<>() para evitar que o método tente remover algo de uma lista nula.

	// Métodos:

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public List<Endereco> getListaEndereco() {
		return listaEndereco;
	}

	public void setListaEndereco(List<Endereco> listaEndereco) {
		this.listaEndereco = listaEndereco;
	}
	public void addEndereco(Endereco endereco) {
		this.listaEndereco.add(endereco);
	}
}