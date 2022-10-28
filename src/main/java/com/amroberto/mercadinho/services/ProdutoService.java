package com.amroberto.mercadinho.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amroberto.mercadinho.model.Produto;
import com.amroberto.mercadinho.model.exception.ResourceNotFoundException;
import com.amroberto.mercadinho.repositories.ProdutoRepository;
import com.amroberto.mercadinho.shared.ProdutoDTO;

/**
 * @author alexandre
 *
 */
/**
 * @author alexandre
 *
 */
@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	/**
	 * Método para obter todos os produtos da lista.
	 * @return Todos os produtos cadastrados.
	 */
	public List<ProdutoDTO> obterTodos(){
		
		List<Produto> produtos = produtoRepository.findAll();
		
		return produtos.stream()
				.map(produto -> new ModelMapper().map(produto, ProdutoDTO.class))
				.collect(Collectors.toList());
		
	}
	/**
	 * Método que retorna o produto encontrado pelo seu id.
	 * @param id do produto ue será localizado.
	 * @return Retorna um produto caso seja encontrado.
	 */
	public Optional<ProdutoDTO> obterPorId(Integer id) {
		// Obtendo optional de produto pelo id.
		Optional<Produto> produto = produtoRepository.findById(id);
		
		// Se não encontrat, lança exception
		if(produto.isEmpty()) {
			throw new ResourceNotFoundException("Produto com id: " + id + " não encontrado!");
		}
		
		// Convertendo o meu optional de produto em um produtoDTO
		ProdutoDTO dto = new ModelMapper().map(produto.get(), ProdutoDTO.class);
		
		// Criando e retornando um optional de produtoDto.
		return Optional.of(dto);
	}
	
	/**
	 * Método para adicionar produto na lista
	 * @param produto que será adicionado
	 * @return Retorna o produto que será adicionado na lista.
	 */
	public ProdutoDTO adicionar(ProdutoDTO produtoDto) {
		// Removendo o id para conseguir fazer o cadastro		
		produtoDto.setId(null);
		
		// Criar um objeto de mapeamento.
		ModelMapper mapper = new ModelMapper();
		
		// Converter nosso produtoDTO em um produto
		Produto produto = mapper.map(produtoDto, Produto.class);		
		
		// Salvar o produto no Banco.
		produto = produtoRepository.save(produto);
		
		produtoDto.setId(produto.getId());
		
		// Retornar o ProdutoDTO Atualizado.		
		return produtoDto;
	}
	
	/**
	 * Método para deletar o produto por id.
	 * @param id do produto a ser deletado.
	 */
	public void deletar(Integer id) {
		produtoRepository.deleteById(id);
	}
	
	/**
	 * Método para atualizar o produto na lista.
	 * @param produto que será atualizado.
	 * @return Retorna o produto após atualizar a lista.
	 */
	public ProdutoDTO atualizar(Integer id, ProdutoDTO produtoDto) {
		
		// Passar o id para o produtoDto
		produtoDto.setId(id);
		
		// Criar um objeto de mapeamento
		ModelMapper mapper = new ModelMapper();
		
		// Converter o ProdutoDTO em um Produto.
		Produto produto = mapper.map(produtoDto, Produto.class);
		
		// Atualizar o produto no Banco de dados.
		produtoRepository.save(produto);
		
		// Retorna o produtoDto atualizado
		return produtoDto;
	}
}
