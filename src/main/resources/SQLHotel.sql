CREATE DATABASE HotelDB;
USE HotelDB;

INSERT INTO tipo_quarto (descricao, nome, preco_por_dia)
VALUES
    ('Quarto simples com cama de solteiro', 'Solteiro',    150.00),
    ('Quarto com cama de casal',              'Casal',       250.00),
    ('Suite com sala de estar e varanda',     'Suite',       450.00),
    ('Quarto duplo com duas camas',           'Duplo',       300.00);

INSERT INTO quarto (andar, descricao, disponivel, numero, tipo_quarto_id)
VALUES
    (1, 'Quarto tranquilo com vista para o jardim',    1, '101', 1),
    (1, 'Quarto aconchegante com varanda',              1, '102', 2),
    (2, 'Suite espaçosa com vista para a piscina',      1, '201', 3),
    (2, 'Quarto duplo ideal para famílias',             0, '202', 4),
    (3, 'Quarto com vista panorâmica da cidade',        1, '301', 2);


INSERT INTO cliente (cidade, cpf, nome, telefone)
VALUES
    ('São Paulo',    '111.222.333-44', 'Ana Souza',      '(11) 91234-5678'),
    ('Rio de Janeiro', '222.333.444-55', 'Bruno Lima',     '(21) 98765-4321'),
    ('Curitiba',     '333.444.555-66', 'Carla Mendes',   '(41) 93456-7890'),
    ('Belo Horizonte','444.555.666-77', 'Diego Ferreira', '(31) 92345-6789'),
    ('Salvador',     '555.666.777-88', 'Elena Costa',   '(71) 91111-2222');


INSERT INTO reserva (data_checkin, data_checkout, data_criacao, quantidade_dias, cliente_id, quarto_id)
VALUES
    ('2025-07-10', '2025-07-13', '2025-06-01', 3, 1, 1),
    ('2025-07-15', '2025-07-20', '2025-06-05', 5, 2, 2),
    ('2025-08-01', '2025-08-05', '2025-06-10', 4, 3, 3),
    ('2025-08-12', '2025-08-14', '2025-06-20', 2, 4, 5),
    ('2025-09-01', '2025-09-08', '2025-07-01', 7, 5, 4);


INSERT INTO hospedagem (ativa, data_checkin, data_checkout, quantidade_dias, cliente_id, quarto_id)
VALUES
    (1, '2025-06-01', '2025-06-04', 3, 1, 2),
    (1, '2025-06-10', '2025-06-15', 5, 2, 3),
    (0, '2025-05-20', '2025-05-23', 3, 3, 1),
    (0, '2025-05-25', '2025-05-28', 3, 4, 4),
    (1, '2025-06-18', '2025-06-22', 4, 5, 5);

INSERT INTO servico (descricao, identificador_interno, nome, valor)
VALUES
    ('Café da manhã completo',                'SRV-001', 'Café da Manhã',     35.00),
    ('Serviço de lavanderia por peça',        'SRV-002', 'Lavanderia',        20.00),
    ('Massagem relaxante de 60 minutos',      'SRV-003', 'Massagem',          120.00),
    ('Transfer aeroporto-hotel',              'SRV-004', 'Transfer',          80.00),
    ('Estacionamento por diária',             'SRV-005', 'Estacionamento',    50.00);


INSERT INTO servico_hospedagem (data_solicitacao, quantidade, hospedagem_id, servico_id)
VALUES
    ('2025-06-01', 3, 1, 1),  -- café da manhã por 3 dias
    ('2025-06-02', 2, 1, 2),  -- lavanderia 2 peças
    ('2025-06-11', 1, 2, 3),  -- massagem
    ('2025-06-10', 1, 2, 4),  -- transfer
    ('2025-06-18', 4, 5, 1),  -- café por 4 dias
    ('2025-06-19', 1, 5, 5);  -- estacionamento


INSERT INTO reserva (data_checkin, data_checkout, data_criacao, quantidade_dias, cliente_id, quarto_id)
VALUES ('2025-06-08', '2025-06-12', '2025-05-01', 4, 1, 2)

INSERT INTO hospedagem (ativa, data_checkin, data_checkout, quantidade_dias, cliente_id, quarto_id)
VALUES (1, '2025-06-13', '2025-06-17', 4, 2, 5)



CREATE FUNCTION  fn_hospedagem (@idQuarto INT, @InicioPeriodo DATE, @FimPeriodo DATE, @disponivel INT) 
RETURNS INT
AS
BEGIN 
	DECLARE @idHospedagem INT,
		@InicioHospedagem DATE,
		@FimHospedagem DATE,
		@ativo INT,
		@ContIDQuarto INT


	DECLARE h CURSOR FOR
		SELECT quarto.id, ho.id, ho.data_checkin, ho.data_checkout, ho.ativa
		FROM hospedagem ho, quarto 
		WHERE ho.quarto_id = quarto.id AND ho.ativa = 1 AND quarto.id = @idQuarto
		ORDER BY quarto.id
		OPEN h;
		FETCH NEXT FROM h
		INTO @ContIDQuarto,@idHospedagem,@InicioHospedagem,@FimHospedagem,@ativo
		WHILE @@FETCH_STATUS = 0
		BEGIN
			IF NOT ((@InicioPeriodo < @InicioHospedagem AND @FimPeriodo < @InicioHospedagem) OR
				(@InicioPeriodo > @FimHospedagem AND @FimPeriodo > @FimHospedagem))
				BEGIN
					SET @disponivel = 0
				END
				 FETCH NEXT FROM h INTO  @ContIDQuarto,@idHospedagem,@InicioHospedagem,@FimHospedagem,@ativo
		END
		CLOSE h;
		DEALLOCATE h;
		RETURN @disponivel
END



CREATE FUNCTION fn_reserva(@idQuarto INT, @InicioPeriodo DATE, @FimPeriodo DATE, @disponivel INT)
RETURNS INT 
AS
BEGIN
	DECLARE @InicioReserva DATE,
			@qtdDias INT,
			@FimReserva DATE,
			@IdReserva INT,
			@ContIDQuarto INT
	DECLARE r CURSOR FOR
		SELECT qu.id, re.id, re.data_checkin, re.quantidade_dias
		FROM reserva re, quarto qu 
		WHERE re.quarto_id = qu.id AND qu.id = @idQuarto
		ORDER BY qu.id
		OPEN r;
		FETCH NEXT FROM r
		INTO @ContIDQuarto,@idReserva,@InicioReserva,@qtdDias
		WHILE @@FETCH_STATUS = 0
		BEGIN
			SET @FimReserva = DATEADD(DAY,@qtdDias,@InicioReserva)
			IF NOT ((@InicioPeriodo < @InicioReserva AND @FimPeriodo < @InicioReserva) OR
				(@InicioPeriodo > @FimReserva AND @FimPeriodo > @FimReserva))
				BEGIN
					SET @disponivel = 0
				END
			 FETCH NEXT FROM r INTO  @ContIDQuarto,@idReserva,@InicioReserva,@qtdDias
		END
		CLOSE r;
		DEALLOCATE r;
		RETURN @disponivel	
END




CREATE FUNCTION fn_quartos_disponiveis(@InicioPeriodo DATE, @qtdDias INT)
RETURNS @tabela TABLE (
	id_quarto INT,
	numero INT,
	andar INT
)
AS
BEGIN
	DECLARE @FimPeriodo DATE
	DECLARE @contadorIdQuarto INT,
		@idHospedagem INT,
		@disponivel INT,
		@numero INT,
		@andar INT

	SET @FimPeriodo = DATEADD(DAY,@qtdDias,@InicioPeriodo)
	DECLARE q CURSOR FOR 
	SELECT q.id FROM quarto q
	ORDER BY q.id
	OPEN q;
	FETCH NEXT FROM q
	INTO @contadorIdQuarto
	WHILE @@FETCH_STATUS = 0 
	BEGIN
		SET @disponivel = 1
		SET @disponivel = dbo.fn_hospedagem(@contadorIdQuarto, @InicioPeriodo, @FimPeriodo, @disponivel)
		SET @disponivel = dbo.fn_reserva(@contadorIdQuarto, @InicioPeriodo, @FimPeriodo, @disponivel)
			IF (@disponivel = 1)
				BEGIN
					SET @numero = (SELECT numero FROM quarto WHERE quarto.id = @contadorIdQuarto)
					SET @andar = (SELECT andar FROM quarto WHERE quarto.id = @contadorIdQuarto)
					INSERT INTO @Tabela VALUES (@contadorIdQuarto, @numero, @andar)
				END
		FETCH NEXT FROM q INTO  @contadorIdQuarto
	END
	CLOSE q;
	DEALLOCATE q;
	RETURN 
END





CREATE FUNCTION fn_listaDisponivel(@data DATE)
RETURNS @tabela TABLE(
		id_quarto INT,
		numero INT, 
		andar INT,
		tipo_quarto VARCHAR(100),
		valor DECIMAL(7,2)
)
AS
BEGIN
	INSERT INTO @tabela SELECT q.id, q.numero, q.andar, tp.nome,tp.preco_por_dia
					FROM quarto q, tipo_quarto tp
					WHERE q.tipo_quarto_id = tp.id AND
					q.id IN (
						SELECT id_quarto FROM dbo. fn_quartos_disponiveis(@data,1)
					)
			RETURN
END




-- Colocar esse no arquivo Jaspersoft do relatório
--SELECT * FROM dbo.fn_listaDisponivel($P{DATA})

CREATE FUNCTION fn_periodo_consumo_cliente (@dataInicio DATE, @qtdDias INT) 
RETURNS @tabela TABLE(
		cpf VARCHAR(14),
		nome VARCHAR(100),
		telefone VARCHAR(20),
		cidade VARCHAR(100),
		ativa BIT,
		data_checkin DATE,
		data_checkout DATE,
		qtd_dias INT,
		nome_servico VARCHAR(100) ,
		valor_total NUMERIC(10,2)
)
AS
BEGIN
	DECLARE @dataFim DATE

	SET @dataFim = DATEADD(DAY, @qtdDias, @dataInicio)

	INSERT INTO @tabela SELECT cli.cpf, cli.nome, cli.telefone, cli.cidade,
		   ho.ativa, ho.data_checkin, ho.data_checkout, ho.quantidade_dias, ser.nome, dbo.fn_calculaTotal(@dataInicio, @dataFim) as valorTotal
		   FROM cliente cli, hospedagem ho, servico ser, servico_hospedagem sh
		   WHERE cli.id = ho.cliente_id AND sh.hospedagem_id = ho.id AND ser.id = sh.servico_id
		   AND sh.data_solicitacao <= @dataFim AND sh.data_solicitacao >= @dataInicio
		   
		   GROUP BY cli.cpf, cli.nome, cli.telefone, cli.cidade, 
		   ho.ativa, ho.data_checkin, ho.data_checkout, ho.quantidade_dias, ser.nome
	RETURN 
END


CREATE FUNCTION fn_calculaTotal(@dataInicio DATE, @dataFim DATE)
	RETURNS NUMERIC (10,2)
	AS
	BEGIN
	DECLARE @valorTotal NUMERIC(10,2)
	SET @valorTotal = (SELECT SUM(ser.valor * sh.quantidade) FROM servico_hospedagem sh, servico ser
	WHERE sh.data_solicitacao >= @dataInicio AND sh.data_solicitacao <= @dataFim AND
	sh.servico_id = ser.id)
	RETURN @valorTotal
	END


--Exibir o valor_total APENAS no cabeçalho
-- SELECT * FROM dbo.fn_periodo_consumo_cliente('2025-06-01', 30)


ALTER FUNCTION fn_dadosReserva(@dia DATE)
 RETURNS @Tabela TABLE(
		cpf VARCHAR(14),
		nome VARCHAR(100),
		telefone VARCHAR(20),
		cidade VARCHAR(100),
		andar INT,
		descricao VARCHAR(255),
		numero INT,
		tipo VARCHAR(255),
		preco NUMERIC(10,2)
		)
	AS
	BEGIN
	INSERT INTO @Tabela SELECT cli.cpf, cli.nome, cli.telefone, cli.cidade,
	q.andar, q.descricao, q.numero, tp.nome, tp.preco_por_dia
	FROM cliente cli, quarto q, tipo_quarto tp, reserva r
	WHERE cli.id = r.cliente_id AND tp.id = q.tipo_quarto_id AND q.id = r.quarto_id
	AND @dia <= r.data_checkout AND @dia >= r.data_checkin
	RETURN
	END


-- SELECT * FROM dbo.fn_dadosReserva('2025-10-01') no Jaspersoft

SELECT * FROM dbo.fn_dadosReserva('2025-07-11')
SELECT * FROM dbo.fn_dadosReserva('2025-08-12')
SELECT * FROM dbo.fn_quartos_disponiveis('2025-10-01', 3)



SELECT * FROM quarto;
SELECT * FROM reserva;
SELECT * FROM cliente
SELECT * FROM hospedagem
SELECT * FROM quarto
SELECT * FROM servico
SELECT * FROM servico_hospedagem
SELECT * FROM tipo_quarto
SELECT * FROM dbo.fn_listaDisponivel('2025-06-10')
