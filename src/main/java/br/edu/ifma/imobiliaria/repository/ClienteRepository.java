package br.edu.ifma.imobiliaria.repository;

import java.util.List;

import br.edu.ifma.imobiliaria.model.Cliente;

public interface ClienteRepository  {
	
	public void cadastrar(Cliente cliente);

	public void atualizar(Cliente cliente);

	public void remover(Cliente cliente);

	public List<Cliente> buscarTodos();
	
	public Cliente buscarPor(Long id);
	
	public Cliente buscarPor(String nome);
}