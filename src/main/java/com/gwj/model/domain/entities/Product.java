package com.gwj.model.domain.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_cliente")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    // Campo obrigatório. Não pode ser nulo no banco de dados.
    private String nome;

    private String sobrenome;

    @Column(unique = true, nullable = false)
    private String mail;

    private String telefone;

    @Column(unique = true)
    // Campo único (não pode repetir, mas pode ser nulo)
    private String cpf;

    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    // Campos simples (mapeados automaticamente como colunas)
    private LocalDateTime dataCadastro;
    // Campo para armazenar data e hora do cadastro
    @PrePersist
    // Método executado automaticamente ANTES de salvar no banco de dados
    public void prePersist(){
        this.dataCadastro = LocalDateTime.now();
    }
}
