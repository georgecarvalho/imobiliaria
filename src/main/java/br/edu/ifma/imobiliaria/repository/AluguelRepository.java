package br.edu.ifma.imobiliaria.repository;

import java.time.LocalDate;
import java.util.List;

import br.edu.ifma.imobiliaria.model.Aluguel;

public interface AluguelRepository  {
	
	public void cadastrar(Aluguel aluguel);

	public void atualizar(Aluguel aluguel);

	public void remover(Aluguel aluguel);

	public List<Aluguel> buscarTodos();
	
	public Aluguel buscarPor(Long id);
	
	public Aluguel buscarPor(LocalDate dataVencimento);

	List<Aluguel> buscarAlugueisPagosPor(String nomeCliente);

	List<Aluguel> buscarAlugueisPagosComAtraso();
}