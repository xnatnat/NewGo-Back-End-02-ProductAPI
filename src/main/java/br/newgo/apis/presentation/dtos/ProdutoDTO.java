package br.newgo.apis.presentation.dtos;

import br.newgo.apis.domain.model.Produto;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Esta classe representa um objeto de transferência de dados (DTO) para produtos.
 * Ela contém informações relevantes sobre um produto, como nome, descrição, preço, etc.
 */
public class ProdutoDTO {
    private UUID hash;
    private String nome;
    private String descricao;
    private String ean13;
    private Double preco;
    private Double quantidade;
    private Double estoqueMin;

    private LocalDateTime dtCreate;
    private LocalDateTime dtUpdate;
    private boolean lativo;

    public ProdutoDTO(){
    }
    public ProdutoDTO(Produto produto){
        setHash(produto.getHash());
        setNome(produto.getNome());
        setDescricao(produto.getDescricao());
        setEan13(produto.getEan13());
        setPreco(produto.getPreco());
        setQuantidade(produto.getQuantidade());
        setEstoqueMin(produto.getEstoqueMin());
        setDtCreate(produto.getDtCreate());
        setDtUpdate(produto.getDtUpdate());
        setLativo(produto.isLativo());
    }
    /**
     * Converte o objeto ProdutoDTO atual em uma representação JSON.
     * @return Uma string contendo a representação JSON do objeto.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Converte uma string JSON em um objeto ProdutoDTO.
     * @param json A string JSON a ser convertida.
     * @return Um objeto ProdutoDTO criado a partir da string JSON.
     */
    public static ProdutoDTO fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, ProdutoDTO.class);
    }

    public UUID getHash() {
        return hash;
    }

    public void setHash(UUID hash) {
        this.hash = hash;
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

    public String getEan13() {
        return ean13;
    }

    public void setEan13(String ean13) {
        this.ean13 = ean13;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getEstoqueMin() {
        return estoqueMin;
    }

    public void setEstoqueMin(Double estoqueMin) {
        this.estoqueMin = estoqueMin;
    }

    public LocalDateTime getDtCreate() {
        return dtCreate;
    }

    public void setDtCreate(LocalDateTime dtCreate) {
        this.dtCreate = dtCreate;
    }

    public LocalDateTime getDtUpdate() {
        return dtUpdate;
    }

    public void setDtUpdate(LocalDateTime dtUpdate) {
        this.dtUpdate = dtUpdate;
    }

    public boolean isLativo() {
        return lativo;
    }

    public void setLativo(boolean lativo) {
        this.lativo = lativo;
    }}
