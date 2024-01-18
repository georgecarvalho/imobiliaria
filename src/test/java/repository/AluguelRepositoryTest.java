package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Aluguel;
import model.Cliente;
import model.Imovel;
import model.Locacao;

public class AluguelRepositoryTest {
	private Aluguel aluguel1 = Aluguel.builder()
			.locacao(Locacao.builder()
					.cliente(Cliente.builder().nome("João").build())
					.imovel(Imovel.builder().metragem(42.00).build())
					.build())
			.build();
	
	private Aluguel aluguel2 = Aluguel.builder()
			.locacao(Locacao.builder()
					.cliente(Cliente.builder().nome("Pedro").build())
					.imovel(Imovel.builder().metragem(85.00).build())
					.valorAluguel(new BigDecimal("3000"))
					.build())
			.dataVencimento(LocalDate.now().plusDays(45))
			.valorPago(new BigDecimal("3000"))
			.dataPagamento(LocalDate.now().plusDays(46))
			.build();
	
	private Aluguel aluguel3 = Aluguel.builder()
			.locacao(Locacao.builder()
					.cliente(Cliente.builder().nome("Pedro").build())
					.imovel(Imovel.builder().metragem(53.00).build())
					.valorAluguel(new BigDecimal("1500"))
					.build())
			.dataVencimento(LocalDate.now().plusDays(60))
			.valorPago(new BigDecimal("1500"))
			.dataPagamento(LocalDate.now().plusDays(61))
			.build();
	
	private static EntityManagerFactory emf;
	private EntityManager em;
	private AluguelRepository dao;
	
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
		dao = new AluguelRepositoryImpl(em);
	}

	@AfterEach
	void depois() {
		em.getTransaction().rollback();
	}

	@Test
	void deveCadastrarAluguel() {
		LocalDate dataVencimento = LocalDate.now().plusDays(30);
		dao.cadastrar(aluguel1);
		Aluguel aluguelDB = dao.buscarPor(dataVencimento);
		
		assertNotNull(aluguelDB);
	}
	
	
	@Test
	void deveBuscarAluguelPor() {
		LocalDate dataVencimento = LocalDate.now().plusDays(30);
		dao.cadastrar(aluguel1);
		Aluguel aluguelDB = dao.buscarPor(dataVencimento);

		assertEquals(dataVencimento, aluguelDB.getDataVencimento(), "Datas devem ser iguais");
	}

	@Test
	void deveAtualizarAluguel() {
		LocalDate dataVencimento = LocalDate.now().plusDays(30);
		dao.cadastrar(aluguel1);
		
		Aluguel aluguelDB = dao.buscarPor(dataVencimento);
		aluguelDB.setObs("Teste");
		dao.atualizar(aluguelDB);
		
		aluguelDB = dao.buscarPor(dataVencimento);

		assertEquals("Teste", aluguelDB.getObs(), "Obs devem ser iguais");
	}

	
	@Test
	void deveRemoverAluguel() {
		LocalDate dataVencimento = LocalDate.now().plusDays(30);
		dao.cadastrar(aluguel1);
		
		Aluguel aluguelDB = dao.buscarPor(dataVencimento);
		dao.remover(aluguelDB);

		assertThrows(NoResultException.class,
				() -> dao.buscarPor(dataVencimento),
				"Deve lançar NoResultException");
	}
	
	@Test
	void deveBuscarAlugueisPagosPorNomeCliente() {
		dao.cadastrar(aluguel1);
		dao.cadastrar(aluguel2);
		dao.cadastrar(aluguel3);
		
		List<Aluguel> alugueis = dao.buscarAlugueisPagosPor("Pedro");

		assertEquals(2, alugueis.size(), "Tamanho da lista deve ser dois");
	}
	
	@Test
	void deveBuscarAlugueisPagosComAtraso() {
		
		dao.cadastrar(aluguel1);
		dao.cadastrar(aluguel2);
		dao.cadastrar(aluguel3);
		
		List<Aluguel> alugueis = dao.buscarAlugueisPagosComAtraso();

		assertEquals(2, alugueis.size(), "Tamanho da lista deve ser dois");
	}
	
	@Test
	void deveLancarExcecaoSeValorPagoMenorDoQueValorAluguel() {
		Aluguel aluguel = Aluguel.builder()
				.locacao(Locacao.builder()
						.cliente(Cliente.builder().nome("Lucas").build())
						.imovel(Imovel.builder().metragem(85.00).build())
						.valorAluguel(new BigDecimal("1800"))
						.build())
				.dataPagamento(LocalDate.now().plusDays(30))
				.valorPago(new BigDecimal("1500"))
				.build();

		assertThrows(IllegalArgumentException.class, 
				() -> aluguel.setValorPago(new BigDecimal("1500")), 
				"Deve lançar IllegalArgumentException");
	}
	
	@Test
	void deveRetornarValorASerPagoSemMulta() {
		Aluguel aluguel = Aluguel.builder()
				.locacao(Locacao.builder()
						.cliente(Cliente.builder().nome("Lucas").build())
						.imovel(Imovel.builder().metragem(85.00).build())
						.valorAluguel(new BigDecimal("1800"))
						.build())
				.build();
		
		aluguel.setDataPagamento(LocalDate.now().plusDays(30));

		assertEquals(new BigDecimal("1800").setScale(2), 
				aluguel.valorASerPago(), 
				"Valores devem ser iguais");
	}
	
	@Test
	void deveRetornarValorASerPagoComMulta() {
		Aluguel aluguel = Aluguel.builder()
				.locacao(Locacao.builder()
						.cliente(Cliente.builder().nome("Lucas").build())
						.imovel(Imovel.builder().metragem(85.00).build())
						.valorAluguel(new BigDecimal("1800"))
						.build())
				.build();
		
		aluguel.setDataPagamento(LocalDate.now().plusDays(32));

		assertEquals(new BigDecimal("2988").setScale(2), 
				aluguel.valorASerPago(), 
				"Valores devem ser iguais");
	}
	
	@Test
	void deveRetornarValorASerPagoComMultaLimite() {
		Aluguel aluguel = Aluguel.builder()
				.locacao(Locacao.builder()
						.cliente(Cliente.builder().nome("Lucas").build())
						.imovel(Imovel.builder().metragem(85.00).build())
						.valorAluguel(new BigDecimal("1800"))
						.build())
				.build();
		
		aluguel.setDataPagamento(LocalDate.now().plusDays(60));

		assertEquals(new BigDecimal("3240").setScale(2), 
				aluguel.valorASerPago(), 
				"Valores devem ser iguais");
	}
}