package br.edu.ifma.imobiliaria.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;

import br.edu.ifma.imobiliaria.model.Aluguel;

public class AluguelRepositoryImpl implements AluguelRepository {
	private EntityManager entityManager;

	public AluguelRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void cadastrar(Aluguel aluguel) {
		this.entityManager.persist(aluguel);
	}

	@Override
	public void atualizar(Aluguel aluguel) {
		this.entityManager.merge(aluguel);
	}

	@Override
	public void remover(Aluguel aluguel) {
		aluguel = this.entityManager.merge(aluguel);
		this.entityManager.remove(aluguel);
	}
	
	@Override
	public List<Aluguel> buscarTodos() {
		return this.entityManager.createQuery("SELECT a FROM Aluguel a", Aluguel.class).getResultList();
	}
	
	@Override
	public Aluguel buscarPor(Long id) {
		return this.entityManager.find(Aluguel.class, id);
	}
	
	@Override
	public Aluguel buscarPor(LocalDate dataVencimento) {
		String jqpl = "SELECT a FROM Aluguel a WHERE a.dataVencimento = :dataVencimento";
		return this.entityManager
				.createQuery(jqpl, Aluguel.class)
				.setParameter("dataVencimento", dataVencimento)
				.getSingleResult();
	}
	
	@Override
	public List<Aluguel> buscarAlugueisPagosPor(String nomeCliente) {
		String jqpl = "SELECT a FROM Aluguel a WHERE a.valorPago IS NOT NULL AND a.locacao.cliente.nome = :nomeCliente";
		
		return this.entityManager
				.createQuery(jqpl, Aluguel.class)
				.setParameter("nomeCliente", nomeCliente)
				.getResultList();
	}
	
	@Override
	public List<Aluguel> buscarAlugueisPagosComAtraso() {
		String jqpl = "SELECT a FROM Aluguel a WHERE a.valorPago IS NOT NULL AND a.dataPagamento > a.dataVencimento";
		
		return this.entityManager
				.createQuery(jqpl, Aluguel.class)
				.getResultList();
	}
}
