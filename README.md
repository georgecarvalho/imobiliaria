# Sistema de biblioteca

Sistema de imobiliária desenvolvido em Java, para exemplificar o uso de testes com JUnit 5 e Mockito.
Seu objetivo é simular o CRUD de Clientes, Imóveis, Alugueis e Locações, além de outras regras de negócio, como calcular valor de multas por atraso no aluguel.
Foram realizados o testes de integração de cada repositório, além de regras da cada de serviço.
Projeto desenvolvido durante a disciplina de Engenharia de Software II, do curso de Sistemas de Informação - IFMA, em 2021.

### 🔩 Testes de Integração

Os testes desenvolvidos nesse projeto têm como objetivo verificar a validade das regras de negócio.

```
Deve Cadastrar, Buscar, Atualizar e Remover todas as Entidades (CRUD) 
Deve buscar Locações disponíveis por Bairro e Tipo do Imóvel
Deve buscar Locações disponíveis com limite no valor do aluguel sugerido
Deve buscar Alugueis pagos por Nome do Cliente
Deve buscar Alugueis pagos com atraso
Deve lancar exceção se valor pago for menor do que o valor do Aluguel
Deve retornar valor a ser pago sem multa
Deve retornar valor a ser pago com multa
Deve retornar valor a ser pago com multa limite
Deve enviar Email a todos os Clientes com atraso de pagamento
Deve continuar enviando Emails após lancamento de Exceção
```

## 🛠️ Construído com

* [Maven](https://maven.apache.org/) - Gerente de Dependências
* [Lombok Project](https://projectlombok.org/) - Biblioteca de Anotações para Getters, Setters, Builders, etc
* [Mockito](https://hibernate.org/) - Framework para ORM
* [JUnit 5](https://junit.org/junit5/) - Framework de Testes Automatizados
* [Mockito](https://site.mockito.org/) - Framework de Testes Automatizados

## ✒️ Autores

* **George Sanders** - [georgecarvalho](https://github.com/georgecarvalho)

## 📄 Licença

Este projeto está sob a licença - veja o arquivo [LICENSE.md](https://github.com/georgecarvalho/imobiliaria/blob/main/LICENSE) para detalhes.
