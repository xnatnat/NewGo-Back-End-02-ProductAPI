package br.newgo.apis.application.utils;

import br.newgo.apis.application.dtos.PrecoLoteDTO;
import br.newgo.apis.infrastructure.entities.Produto;
import br.newgo.apis.application.dtos.ProdutoDTO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Esta classe é responsável por converter objetos da classe Produto para objetos da classe ProdutoDTO
 * e vice-versa, além de fornecer funcionalidades para conversão de listas de objetos ProdutoDTO em JSON.
 */
public class ProdutoMapeador {
    /**
     * Converte um objeto da classe Produto para um objeto da classe ProdutoDTO.
     *
     * @param produto O objeto da classe Produto a ser convertido.
     * @return Um objeto da classe ProdutoDTO contendo os mesmos dados do objeto Produto original.
     */
    public static ProdutoDTO mapearParaDTO(Produto produto){
        return new ProdutoDTO(
                produto.getHash(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getEan13(),
                produto.getPreco(),
                produto.getQuantidade(),
                produto.getEstoqueMin(),
                produto.getDtCreate(),
                produto.getDtUpdate(),
                produto.isLativo().toString()
        );
    }

    public static ProdutoDTO mapearParaDTO(JsonObject objetoJson){
        return new Gson().fromJson(objetoJson, ProdutoDTO.class);
    }

    public static Produto mapearParaProduto(ProdutoDTO produtoDTO){
        return new Produto(
                produtoDTO.getNome(),
                produtoDTO.getDescricao(),
                produtoDTO.getEan13(),
                produtoDTO.getPreco(),
                produtoDTO.getQuantidade(),
                produtoDTO.getEstoqueMin());
    }

    public static PrecoLoteDTO mapearParaPrecoLoteDTO(JsonObject objetoJson){
        return new Gson().fromJson(objetoJson, PrecoLoteDTO.class);
    }
    public static List<ProdutoDTO> mapearParaListaDeDTOS(List<Produto> produtos){
        return produtos.stream()
                .map(ProdutoMapeador::mapearParaDTO)
                .collect(Collectors.toList());
    }
}
