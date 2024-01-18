package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import model.Aluguel;
import model.Cliente;
import model.Imovel;
import model.Locacao;
import repository.AluguelRepository;

@ExtendWith(MockitoExtension.class)
public class AluguelServiceTest {
	
	@InjectMocks
	AluguelService aluguelService;
	
	@Mock
	AluguelRepository aluguelRepository;
	
	@Mock
	EmailService emailService;
	
	private Aluguel aluguel1;
	private Aluguel aluguel2;
	private Aluguel aluguel3;
	
	private List<Aluguel> alugueisComAtraso;
	
	@BeforeEach
	public void init() {
		aluguel1 = Aluguel.builder()
				.locacao(Locacao.builder()
						.cliente(Cliente.builder()
								.nome("João")
								.email("joao@email.com")
								.build())
						.imovel(Imovel.builder().metragem(53.00).build())
						.valorAluguel(new BigDecimal("1500"))
						.build())
				.dataVencimento(LocalDate.now().plusDays(30))
				.valorPago(new BigDecimal("1500"))
				.dataPagamento(LocalDate.now().plusDays(30))
				.build();
		
		aluguel2 = Aluguel.builder()
				.locacao(Locacao.builder()
						.cliente(Cliente.builder()
								.nome("Pedro")
								.email("pedro@email.com")
								.build())
						.imovel(Imovel.builder().metragem(85.00).build())
						.valorAluguel(new BigDecimal("3000"))
						.build())
				.dataVencimento(LocalDate.now().plusDays(60))
				.valorPago(new BigDecimal("3000"))
				.dataPagamento(LocalDate.now().plusDays(61))
				.build();
		
		aluguel3 = Aluguel.builder()
				.locacao(Locacao.builder()
						.cliente(Cliente.builder()
								.nome("Lucas")
								.email("lucas@email.com")
								.build())
						.imovel(Imovel.builder().metragem(42.00).build())
						.valorAluguel(new BigDecimal("900"))
						.build())
				.dataVencimento(LocalDate.now().plusDays(45))
				.valorPago(new BigDecimal("900"))
				.dataPagamento(LocalDate.now().plusDays(46))
				.build();
		
		alugueisComAtraso = Arrays.asList(aluguel1, aluguel2, aluguel3);
	}
	
	@Test
	void deveEnviarEmailATodosOsClientesComAtrasoDePagamento() {
			
		when(aluguelRepository.buscarAlugueisPagosComAtraso()).thenReturn(alugueisComAtraso);
		
		aluguelService.enviarEmailATodosOsClienteComPagamentoAtrasado();
		verify(emailService, times(1)).enviaEmail(aluguel1.getCliente());
		verify(emailService, times(1)).enviaEmail(aluguel2.getCliente());
		verify(emailService, times(1)).enviaEmail(aluguel3.getCliente());
		
		verifyNoMoreInteractions(emailService);
	}
	
	@Test
	void deveContinuarEnviandoEmailsAposLancamentoDeExcecao() {
		Cliente cliente4 = Mockito.mock(Cliente.class);
		
		when(aluguelRepository.buscarAlugueisPagosComAtraso()).thenReturn(alugueisComAtraso);
		
		aluguelService.enviarEmailATodosOsClienteComPagamentoAtrasado();
		
		lenient().when(emailService.enviaEmail(cliente4))
			.thenThrow(new RuntimeException("Não foi possível enviar E-mail"));
		
		verify(emailService, times(1)).enviaEmail(aluguel1.getCliente());
		verify(emailService, times(1)).enviaEmail(aluguel2.getCliente());
		verify(emailService, times(1)).enviaEmail(aluguel3.getCliente());
		
		verifyNoMoreInteractions(emailService);
	}
	
	@Test
	void deveLancarExcecaoCasoPagamentoMenorDoQueValorAluguel() {
		assertThrows(IllegalArgumentException.class, 
				() -> aluguel1.setValorPago(new BigDecimal("1400")),
				"Deve lançar IllegalArgumentException");
		
	}
	
	@Test
	void deveRetornarValorASerPagoSemMulta() {
		assertEquals(new BigDecimal("1500").setScale(2), 
				aluguelService.valorASerPago(aluguel1), 
				"Valores devem ser iguais");
	}
	
	@Test
	void deveRetornarValorASerPagoComMulta() {
		assertEquals(new BigDecimal("3990").setScale(2), 
				aluguelService.valorASerPago(aluguel2), 
				"Valores devem ser iguais");
	}
}
