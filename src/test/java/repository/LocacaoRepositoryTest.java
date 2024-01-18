package repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
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

import model.Cliente;
import model.Imovel;
import model.Locacao;

public class LocacaoRepositoryTest {
	private Locacao locacao1 = Locacao.builder()
			.cliente(Cliente.builder().nome("João").build())
			.imovel(Imovel.builder()
					.bairro("Ficticio")
					.tipo("Apartamento")
					.valorAluguelSugerido(new BigDecimal("3300"))
					.build())
			.valorAluguel(new BigDecimal("3500"))
			.ativo(false)
			.build();

	private Locacao locacao2 = Locacao.builder()
			.cliente(Cliente.builder().nome("Pedro").build())
			.imovel(Imovel.builder()
					.bairro("Ficticio")
					.tipo("Apartamento")
					.valorAluguelSugerido(new BigDecimal("1800"))
					.build())
			.valorAluguel(new BigDecimal("2000"))
			.ativo(false)
			.build();
	
	private Locacao locacao3 = Locacao.builder()
			.cliente(Cliente.builder().nome("Lucas").build())
			.imovel(Imovel.builder()
					.bairro("Ficticio")
					.tipo("Apartamento")
					.valorAluguelSugerido(new BigDecimal("900"))
					.build())
			.valorAluguel(new BigDecimal("900"))
			.ativo(true)
			.build();
	
	private Locacao locacao4 = Locacao.builder()
			.cliente(Cliente.builder().nome("Lucas").build())
			.imovel(Imovel.builder()
					.bairro("FazDeConta")
					.tipo("Apartamento")
					.valorAluguelSugerido(new BigDecimal("1500"))
					.build())
			.valorAluguel(new BigDecimal("1800"))
			.ativo(false)
			.build();
	
	private Locacao locacao5 = Locacao.builder()
			.cliente(Cliente.builder().nome("Lucas").build())
			.imovel(Imovel.builder()
					.bairro("Ficticio")
					.tipo("Luxo")
					.valorAluguelSugerido(new BigDecimal("3800"))
					.build())
			.valorAluguel(new BigDecimal("4000"))
			.ativo(false)
			.build();
	
	private static EntityManagerFactory emf;
	private EntityManager em;
	private LocacaoRepository locacaoDao;

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
		locacaoDao = new LocacaoRepositoryImpl(em);
	}

	@AfterEach
	void depois() {
		em.getTransaction().rollback();
	}

	@Test
	void deveCadastrarLocacao() {
		locacaoDao.cadastrar(locacao1);
		Locacao locacaoDB = locacaoDao.buscarPor(new BigDecimal("3500"));

		assertNotNull(locacaoDB);
	}

	@Test
	void deveBuscarLocacaoPor() {
		locacaoDao.cadastrar(locacao1);
		Locacao locacaoDB = locacaoDao.buscarPor(new BigDecimal("3500"));

		assertEquals(new BigDecimal("3500"), 
				locacaoDB.getValorAluguel(), 
				"Valor dos alugueis devem ser iguais");
	}

	@Test
	void deveAtualizarLocacao() {
		locacaoDao.cadastrar(locacao2);
		
		Locacao locacaoDB = locacaoDao.buscarPor(new BigDecimal("2000"));
		locacaoDB.setValorAluguel(new BigDecimal("2100"));
		locacaoDao.atualizar(locacaoDB);
		
		locacaoDB = locacaoDao.buscarPor(new BigDecimal("2100"));

		assertEquals(new BigDecimal("2100"), 
				locacaoDB.getValorAluguel(), 
				"Valor dos alugueis devem ser iguais");
	}

	@Test
	void deveRemoverLocacao() {
		locacaoDao.cadastrar(locacao1);
		
		Locacao locacaoDB = locacaoDao.buscarPor(new BigDecimal("3500"));
		locacaoDao.remover(locacaoDB);

		assertThrows(NoResultException.class,
				() -> locacaoDao.buscarPor(new BigDecimal("3500")),
				"Deve lançar NoResultException");
	}
	
	@Test
	void deveBuscarLocacoesDisponiveisPorBairroTipoImovel() {
		locacaoDao.cadastrar(locacao1);
		locacaoDao.cadastrar(locacao2);
		locacaoDao.cadastrar(locacao3);
		locacaoDao.cadastrar(locacao4);
		locacaoDao.cadastrar(locacao5);
		
		List<Locacao> locacoes = locacaoDao.buscarPor("Ficticio", "Apartamento", false);
		
		assertEquals(2, locacoes.size(), "Tamanho da lista deve ser dois");
	}
	
	@Test
	void deveBuscarLocacoesDisponiveisComLimiteValorAluguelSugerido() {
		locacaoDao.cadastrar(locacao1);
		locacaoDao.cadastrar(locacao2);
		locacaoDao.cadastrar(locacao3);
		locacaoDao.cadastrar(locacao4);
		locacaoDao.cadastrar(locacao5);
		
		List<Locacao> locacoes = locacaoDao.buscarLocacoesComValorAluguelSugeridoMenorOuIgualA(new BigDecimal("1800"), false);
		
		assertEquals(3, locacoes.size(), "Tamanho da lista deve ser três");
	}
}