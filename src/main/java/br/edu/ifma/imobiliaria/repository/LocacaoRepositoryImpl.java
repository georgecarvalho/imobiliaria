package br.edu.ifma.imobiliaria.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ifma.imobiliaria.model.Locacao;

public class LocacaoRepositoryImpl implements LocacaoRepository{
	private EntityManager entityManager;
	
	public LocacaoRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void cadastrar(Locacao locacao) {
		this.entityManager.persist(locacao);
	}

	@Override
	public void atualizar(Locacao locacao) {
		this.entityManager.merge(locacao);
	}

	@Override
	public void remover(Locacao locacao) {
		locacao = this.entityManager.merge(locacao);
		this.entityManager.remove(locacao);
	}

	@Override
	public List<Locacao> buscarTodos() {
		return this.entityManager
				.createQuery("SELECT l FROM Locacao l", Locacao.class)
				.getResultList();
	}
	
	@Override
	public Locacao buscarPor(Long id) {
		return this.entityManager.find(Locacao.class, id);
	}
	
	@Override
	public Locacao buscarPor(BigDecimal valorAuguel) {
		String jqpl = "SELECT l FROM Locacao l WHERE l.valorAluguel = :valorAuguel";
		return this.entityManager
				.createQuery(jqpl, Locacao.class)
				.setParameter("valorAuguel", valorAuguel)
				.getSingleResult();
	}
	
	@Override
	public List<Locacao> buscarPor(String bairro, String tipoImovel, boolean ativo){
		String jqpl = "SELECT l FROM Locacao l WHERE l.ativo = :ativo AND l.imovel.bairro = :bairro AND l.imovel.tipo = :tipoImovel";
		
		return this.entityManager
				.createQuery(jqpl, Locacao.class)
				.setParameter("bairro", bairro)
				.setParameter("tipoImovel", tipoImovel)
				.setParameter("ativo", ativo)
				.getResultList();
	}
	
	@Override
	public List<Locacao> buscarLocacoesComValorAluguelSugeridoMenorOuIgualA(BigDecimal valor, boolean ativo){
		String jqpl = "SELECT l FROM Locacao l WHERE l.ativo = :ativo AND :valor <= l.imovel.valorAluguelSugerido";
		
		return this.entityManager
				.createQuery(jqpl, Locacao.class)
				.setParameter("valor", valor)
				.setParameter("ativo", ativo)
				.getResultList();
	}

}
