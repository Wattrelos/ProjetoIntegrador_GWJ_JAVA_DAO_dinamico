package com.gwj.model.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gwj.model.domain.IEntity;

@Entity
public class Usuario implements IEntity{

	// Atributos primitivos:
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long          id  = 0L; // Inicializa variável de índice
	protected Long          grupoUsuarioId;
	protected String        nomeUsuario;
	protected String        email;
	protected Boolean       status;
	protected String        senha;
	protected String        token;
	protected String        ip;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Avisa ao módulo json utilizar este formato.
	protected LocalDateTime dataCadastro;
	
	// Métodos
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getGrupoUsuarioId() {
		return grupoUsuarioId;
	}
	public void setGrupoUsuarioId(Long grupoUsuarioId) {
		this.grupoUsuarioId = grupoUsuarioId;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

}
