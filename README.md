# Sistema de Gestão de Hotel

Sistema web para gerenciamento de um hotel, desenvolvido com Spring Boot. Permite cadastrar clientes, quartos, reservas, hospedagens e serviços.

---

## Tecnologias utilizadas

- Java 21
- Spring Boot 4
- Spring Web, Spring Data JPA, Spring Validation
- Thymeleaf (templates HTML)
- SQL Server (banco de dados)
- Lombok
- Bootstrap (interface)

---

## Funcionalidades

- **Clientes** — cadastro com CPF, nome, telefone e cidade
- **Tipos de quarto** — categorias com nome, descrição e preço por diária
- **Quartos** — número, andar, descrição e tipo
- **Reservas** — reserva de quarto para uma data e quantidade de dias
- **Hospedagens** — check-in (via reserva ou direto) e check-out
- **Serviços** — alimentação, spa, lavanderia e outros serviços solicitados durante a estadia
- 
---

## Estrutura do projeto

```
hotel/
├── controller/     # Recebe as requisições HTTP
├── service/        # Regras de negócio
├── repository/     # Acesso ao banco de dados
├── model/          # Entidades JPA
├── resources/
│   ├── templates/  # Páginas HTML (Thymeleaf)
│   ├── relatorios/ # Templates de PDF (.jrxml)
│   └── static/     # CSS e arquivos estáticos
└── doc/            # Diagramas do sistema (DER e Classes)
```

---

## Como executar

**Pré-requisitos:** Java 21, Maven e SQL Server instalados.

1. Clone o repositório
2. Configure o banco de dados em `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=HotelDB
   spring.datasource.username=SEU_USUARIO
   spring.datasource.password=SUA_SENHA
   ```
3. Execute o projeto:
   ```bash
   mvn spring-boot:run
   ```
4. Acesse no navegador: `http://localhost:8080`

---

## Modelo de dados

As principais entidades e seus relacionamentos:

- `Cliente` realiza `Reservas` e `Hospedagens`
- `TipoQuarto` classifica os `Quartos`
- `Quarto` é reservado em `Reservas` e ocupado em `Hospedagens`
- `Hospedagem` pode ter vários `Serviços` solicitados via `ServicoHospedagem`

---

## Desenvolvido para

Avaliação 3 — Laboratório de Banco de Dados  
FATEC ZL — Prof. Leandro Colevati
