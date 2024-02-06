package br.edu.ifma.imobiliaria.service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

import br.edu.ifma.imobiliaria.model.Aluguel;
import br.edu.ifma.imobiliaria.repository.AluguelRepository;

public class AluguelService {
	
	private AluguelRepository aluguelRepository;
	private EmailService emailService;
	private final BigDecimal TAXA_DIARIA = new BigDecimal("0.33");
	private final BigDecimal TAXA_LIMITE = new BigDecimal("0.80"); 
	
	public void enviarEmailATodosOsClienteComPagamentoAtrasado() {
		List<Aluguel> alugueis = aluguelRepository.buscarAlugueisPagosComAtraso();
		
		for (Aluguel aluguel : alugueis) {
			emailService.enviaEmail(aluguel.getCliente());
		}
	}
	
	public BigDecimal valorASerPago(Aluguel aluguel) {
		long diasDeAtraso = ChronoUnit.DAYS.between(aluguel.getDataVencimento(), aluguel.getDataPagamento());
		
		if(diasDeAtraso > 0) {
			BigDecimal multa = aluguel.getValorAluguel().multiply(TAXA_DIARIA.multiply(BigDecimal.valueOf(diasDeAtraso)));
			BigDecimal multaLimite = aluguel.getValorAluguel().multiply(TAXA_LIMITE);
			
			if(multa.compareTo(multaLimite) >= 0) {
				return aluguel.getValorAluguel().add(multaLimite).setScale(2);
			}else {
				return aluguel.getValorAluguel().add(multa).setScale(2);
			}
		}
		return aluguel.getValorAluguel().setScale(2);
	}
}
