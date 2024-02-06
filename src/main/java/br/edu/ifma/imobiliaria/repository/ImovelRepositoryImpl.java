package br.edu.ifma.imobiliaria.repository;

import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ifma.imobiliaria.model.Imovel;

public class ImovelRepositoryImpl implements ImovelRepository {
	private EntityManager entityManager;

	public ImovelRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public void cadastrar(Imovel imovel) {
		this.entityManager.persist(imovel);
	}

	@Override
	public void atualizar(Imovel imovel) {
		this.entityManager.merge(imovel);
	}

	@Override
	public void remover(Imovel imovel) {
		imovel = this.entityManager.merge(imovel);
		this.entityManager.remove(imovel);
	}

	@Override
	public List<Imovel> buscarTodos() {
		return this.entityManager
				.createQuery("SELECT i FROM Imovel i", Imovel.class)
				.getResultList();
	}
	
	@Override
	public Imovel buscarPor(Long id) {
		return this.entityManager.find(Imovel.class, id);
	}
	
	@Override
	public Imovel buscarPor(Double metragem) {
		String jqpl = "SELECT i FROM Imovel i WHERE i.metragem = :metragem";
		return this.entityManager
				.createQuery(jqpl, Imovel.class)
				.setParameter("metragem", metragem)
				.getSingleResult();
	}
	
	@Override
	public List<Imovel> buscarTodosApartamentosPor(String bairro) {
		String jqpl = "SELECT i FROM Imovel i WHERE i.bairro = :bairro";
		return this.entityManager
				.createQuery(jqpl, Imovel.class)
				.setParameter("bairro", bairro)
				.getResultList();
	}
}
