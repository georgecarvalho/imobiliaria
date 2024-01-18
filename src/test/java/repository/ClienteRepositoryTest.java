package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Cliente;

public class ClienteRepositoryTest {
	private Cliente cliente1 = Cliente.builder().nome("João").build();
	
	private static EntityManagerFactory emf;
	private EntityManager em;
	private ClienteRepository dao;
	
    @BeforeAll
    static void inicio() {
        emf = Persistence.createEntityManagerFactory("imobiliaria");
    }
    
	@AfterAll
	static void fim() {
		emf.close();
	}

	@BeforeEach
	void antes() {
		em = emf.createEntityManager();
		em.getTransaction().begin();
		dao = new ClienteRepositoryImpl(em);
	}

	@AfterEach
	void depois() {
		em.getTransaction().rollback();
	}

	@Test
	void deveCadastrarCliente() {
		dao.cadastrar(cliente1);
		Cliente clienteDB = dao.buscarPor("João");
		
		assertNotNull(clienteDB);
	}
	
	@Test
	void deveBuscarClientePor() {
		dao.cadastrar(cliente1);
		Cliente clienteDB = dao.buscarPor("João");

		assertEquals("João", clienteDB.getNome(), "Nomes devem ser iguais");
	}

	@Test
	void deveAtualizarCliente() {
		dao.cadastrar(cliente1);
		
		Cliente clienteDB = dao.buscarPor("João");
		clienteDB.setNome("Joãozinho");
		dao.atualizar(clienteDB);
		
		clienteDB = dao.buscarPor("Joãozinho");

		assertEquals("Joãozinho", clienteDB.getNome(), "Nomes devem ser iguais");
	}

	
	@Test
	void deveRemoverCliente() {
		dao.cadastrar(cliente1);
		
		Cliente clienteDB = dao.buscarPor("João");
		dao.remover(clienteDB);

		assertThrows(NoResultException.class,
				() -> dao.buscarPor("João"),
				"Deve lançar NoResultException");
	}
}