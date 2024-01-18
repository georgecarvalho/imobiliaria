package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "aluguel")
public class Aluguel {
	
	@Id
	@Column(name = "data_vencimento")
	@Builder.Default
	private LocalDate dataVencimento = LocalDate.now().plusDays(30);
	
	@ManyToOne(cascade= CascadeType.ALL)
	private Locacao locacao;
	
	@Column(name = "valor_pago")
	private BigDecimal valorPago;
	
	@Column(name = "data_pagamento")
	private LocalDate dataPagamento;
	
	private String obs;
	
	public Cliente getCliente() {
		return this.locacao.getCliente();
	}
	
	public BigDecimal getValorAluguel() {
		return this.locacao.getValorAluguel();
	}
	
	public void setValorAluguel(BigDecimal valorAluguel) {
		this.locacao.setValorAluguel(valorAluguel);
	}
	
	public void setValorPago(BigDecimal valorPago) {
		if (valorPago.compareTo(getValorAluguel()) < 0)
			throw new IllegalArgumentException("O valor pago não pode ser menor do que o valor do aluguel");
		this.valorPago = valorPago;
	}
	
	public BigDecimal valorASerPago() {
		final BigDecimal TAXA_DIARIA = new BigDecimal("0.33");
		final BigDecimal TAXA_LIMITE = new BigDecimal("0.80"); 
		long diasDeAtraso = ChronoUnit.DAYS.between(dataVencimento, dataPagamento);
		
		if(diasDeAtraso > 0) {
			BigDecimal multa = getValorAluguel().multiply(TAXA_DIARIA.multiply(BigDecimal.valueOf(diasDeAtraso)));
			BigDecimal multaLimite = getValorAluguel().multiply(TAXA_LIMITE);
			
			if(multa.compareTo(multaLimite) >= 0) {
				return getValorAluguel().add(multaLimite).setScale(2);
			}else {
				return getValorAluguel().add(multa).setScale(2);
			}
		}
		return getValorAluguel().setScale(2);	
	}
}
