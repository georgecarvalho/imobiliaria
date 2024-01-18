package repository;

import java.util.List;

import model.Imovel;

public interface ImovelRepository {

	public void cadastrar(Imovel imovel);

	public void atualizar(Imovel imovel);

	public void remover(Imovel imovel);
	
	public Imovel buscarPor(Long id);
	
	public Imovel buscarPor(Double metragem);
	
	public List<Imovel> buscarTodos();
	
	public List<Imovel> buscarTodosApartamentosPor(String bairro);
}
