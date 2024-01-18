package repository;

import java.util.List;

import javax.persistence.EntityManager;

import model.Cliente;

public class ClienteRepositoryImpl implements ClienteRepository {
	private EntityManager entityManager;

	public ClienteRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void cadastrar(Cliente cliente) {
		this.entityManager.persist(cliente);
	}

	@Override
	public void atualizar(Cliente cliente) {
		this.entityManager.merge(cliente);
	}

	@Override
	public void remover(Cliente cliente) {
		cliente = this.entityManager.merge(cliente);
		this.entityManager.remove(cliente);
	}
	
	@Override
	public List<Cliente> buscarTodos() {
		return this.entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
	}
	
	@Override
	public Cliente buscarPor(Long id) {
		return this.entityManager.find(Cliente.class, id);
	}
	
	@Override
	public Cliente buscarPor(String nome) {
		String jqpl = "SELECT c FROM Cliente c WHERE c.nome = :nome";
		return this.entityManager
				.createQuery(jqpl, Cliente.class)
				.setParameter("nome", nome)
				.getSingleResult();
	}
}
