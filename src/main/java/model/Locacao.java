package model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "locacao")
public class Locacao {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST})
	private Imovel imovel;
	
	@ManyToOne(cascade= {CascadeType.MERGE, CascadeType.PERSIST})
	private Cliente cliente;
	
	@Column(name = "valor_aluguel")
	private BigDecimal valorAluguel;
	
	@Column(name = "valor_multa")
	private BigDecimal valorMulta;
	
	@Column(name = "dia_vencimento")
	private Integer diaVencimento;
	
	@Column(name = "data_inicio")
	private LocalDate dataInicio;
	
	@Column(name = "data_fim")
	private LocalDate dataFim;
	
	private boolean ativo;
	private String obs;
}
