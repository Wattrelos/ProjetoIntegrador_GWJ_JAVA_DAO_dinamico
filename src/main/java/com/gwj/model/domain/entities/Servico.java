package com.gwj.model.domain.entities;
import java.math.BigDecimal;

import com.gwj.model.domain.IEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Servico implements IEntity{
    
	@Id
    protected Long        id  = 0L; // Inicializa variável de índice
    protected String  	  nome;
    protected String 	  descricao;
    protected BigDecimal  preco;
    protected int         duracao;
    protected String      tipo;
    protected Boolean     ativo;
    
	public Servico() {
		super();
	}
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
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public BigDecimal getPreco() {
		return preco;
	}
	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}
	public Integer getDuracao() {
		return duracao;
	}
	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

}
