package br.edu.ifma.imobiliaria.service;

import br.edu.ifma.imobiliaria.model.Cliente;

public interface EmailService {

	public boolean enviaEmail(Cliente cliente);
}
