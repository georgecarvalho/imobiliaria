package br.edu.ifma.imobiliaria.repository;

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

import br.edu.ifma.imobiliaria.model.Imovel;

public class ImovelRepositoryTest {
	private Imovel imovel1 = Imovel.builder()
			.tipo("Sobrado")
			.metragem(42.00)
			.build();

	private Imovel imovel2 = Imovel.builder()
			.tipo("Sobrado")
			.metragem(85.00)
			.build();
	
	private static EntityManagerFactory emf;
	private EntityManager em;
	private ImovelRepository dao;

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
		dao = new ImovelRepositoryImpl(em);
	}

	@AfterEach
	void depois() {
		em.getTransaction().rollback();
	}

	@Test
	void deveCadastrarImovel() {
		dao.cadastrar(imovel1);
		Imovel imovelDB = dao.buscarPor(42.00);
		
		assertNotNull(imovelDB);
	}

	@Test
	void deveBuscarImovelPor() {
		dao.cadastrar(imovel1);
		Imovel imovelDB = dao.buscarPor(42.00);

		assertEquals(42.00, imovelDB.getMetragem(), "Metragens devem ser iguais");
	}

	@Test
	void deveAtualizarImovel() {
		dao.cadastrar(imovel2);
		
		Imovel imovelDB = dao.buscarPor(85.00);
		imovelDB.setMetragem(90.00);
		dao.atualizar(imovelDB);
		
		imovelDB = dao.buscarPor(90.00);

		assertEquals(90.00, imovelDB.getMetragem(), "Metragens devem ser iguais");
	}

	@Test
	void deveRemoverImovel() {
		dao.cadastrar(imovel1);
		
		Imovel imovelDB = dao.buscarPor(42.00);
		dao.remover(imovelDB);

		assertThrows(NoResultException.class,
				() -> dao.buscarPor(42.00),
				"Deve lançar NoResultException");
	}
}