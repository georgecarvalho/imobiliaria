package br.edu.ifma.imobiliaria.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "imovel")
public class Imovel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "tipo_imovel")
	private String tipo;
	
	@Column(name = "endereco")
	private String rua;
	private String bairro;
	private String cep;
	private Double metragem;
	private Integer dormitorios;
	private Integer banheiros;
	private Integer suites;
	
	@Column(name = "vagas_garagem")
	private Integer vagasGaragem;
	
	@Column(name = "valor_aluguel_sugerido")
	private BigDecimal valorAluguelSugerido;
	
	private String obs;
}
