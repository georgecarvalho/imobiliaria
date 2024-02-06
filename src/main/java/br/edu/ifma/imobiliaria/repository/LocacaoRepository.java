package br.edu.ifma.imobiliaria.repository;

import java.math.BigDecimal;
import java.util.List;

import br.edu.ifma.imobiliaria.model.Locacao;

public interface LocacaoRepository {
	
	public void cadastrar(Locacao locacao);
	
	public void atualizar(Locacao locacao);
	
	public void remover(Locacao locacao);
	
	public Locacao buscarPor(Long id);
	
	public Locacao buscarPor(BigDecimal valorAluguel);
	
	public List<Locacao> buscarTodos();
	
	public List<Locacao> buscarPor(String bairro, String tipoImovel, boolean ativo);

	List<Locacao> buscarLocacoesComValorAluguelSugeridoMenorOuIgualA(BigDecimal valor, boolean ativo);
}
