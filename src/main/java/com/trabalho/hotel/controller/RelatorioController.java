package com.trabalho.hotel.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trabalho.hotel.model.Hospedagem;
import com.trabalho.hotel.model.Reserva;
import com.trabalho.hotel.service.HospedagemService;
import com.trabalho.hotel.service.QuartoService;
import com.trabalho.hotel.service.ReservaService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

@Controller
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {
    
    private final HospedagemService hospedagemService;
    private final ReservaService reservaService;
    private final QuartoService quartoService;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // ── Fatura da hospedagem ──────────────────────────────────────────
    // URL: /relatorios/fatura-pdf/{id}
    @GetMapping("/fatura-pdf/{id}")
    public void faturaHospedagem(@PathVariable Long id,
                                  HttpServletResponse response) throws IOException {
        Hospedagem h = hospedagemService.buscarPorId(id);

        // Parâmetros enviados ao template .jrxml
        Map<String, Object> params = new HashMap<>();
        params.put("nomeCliente",    h.getCliente().getNome());
        params.put("cpfCliente",     h.getCliente().getCpf());
        params.put("telefone",       h.getCliente().getTelefone() != null ? h.getCliente().getTelefone() : "—");
        params.put("cidade",         h.getCliente().getCidade() != null ? h.getCliente().getCidade() : "—");
        params.put("numeroQuarto",   String.valueOf(h.getQuarto().getNumero()));
        params.put("andarQuarto",    h.getQuarto().getAndar() + "º andar");
        params.put("tipoQuarto",     h.getQuarto().getTipoQuarto().getNome());
        params.put("precoDia",       h.getQuarto().getTipoQuarto().getPrecoPorDia().toString());
        params.put("dataCheckin",    h.getDataCheckin().format(FMT));
        params.put("dataCheckout",   h.getDataCheckout() != null ? h.getDataCheckout().format(FMT) : "Em andamento");
        params.put("quantidadeDias", String.valueOf(h.getQuantidadeDias()));
        params.put("totalDiarias",   h.calcularTotalDiarias().toString());
        params.put("totalServicos",  h.calcularTotalServicos().toString());
        params.put("totalGeral",     h.calcularTotal().toString());
        params.put("dataGeracao",    LocalDate.now().format(FMT));

        // Lista de serviços como datasource para a subreport/tabela do jasper
        Collection<Map<String, ?>> servicos = new ArrayList<>();
        if (h.getServicosHospedagem() != null) {
            h.getServicosHospedagem().forEach(s -> {
                Map<String, Object> row = new HashMap<>();
                row.put("nomeServico",    s.getServico().getNome());
                row.put("quantidade",     s.getQuantidade());
                row.put("valorUnitario",  s.getServico().getValor().toString());
                row.put("subtotal",       s.calcularSubtotal().toString());
                servicos.add(row);
            });
        }
        params.put("dsServicos", new JRMapCollectionDataSource(servicos));

        try {
        InputStream subStream = new ClassPathResource("relatorios/fatura-servicos.jrxml").getInputStream();
        JasperReport subReport = JasperCompileManager.compileReport(subStream);
        params.put("subReportServicos", subReport);
    } catch (JRException e) {
        throw new IOException("Erro ao compilar subreport: " + e.getMessage(), e);
    }

        gerarPdf(response, "relatorios/fatura.jrxml",
                 params, new JREmptyDataSource(),
                 "fatura-hospedagem-" + id + ".pdf");
    }

    // ── Reservas por dia ──────────────────────────────────────────────
    // URL: /relatorios/reservas-dia-pdf?data=yyyy-MM-dd
    @GetMapping("/reservas-dia-pdf")
    public void reservasPorDia(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            HttpServletResponse response) throws IOException {

        List<Reserva> reservas = reservaService.listarPorDia(data);

        // Converte lista de reservas em lista de mapas para o Jasper
        List<Map<String, Object>> rows = new ArrayList<>();
        reservas.forEach(r -> {
            Map<String, Object> row = new HashMap<>();
            row.put("nomeCliente",   r.getCliente().getNome());
            row.put("cpf",           r.getCliente().getCpf());
            row.put("cidade",        r.getCliente().getCidade() != null ? r.getCliente().getCidade() : "—");
            row.put("quarto",        "Quarto " + r.getQuarto().getNumero());
            row.put("tipo",          r.getQuarto().getTipoQuarto().getNome());
            row.put("dias",          String.valueOf(r.getQuantidadeDias()));
            row.put("precoDia",      r.getQuarto().getTipoQuarto().getPrecoPorDia().toString());
            row.put("total",         r.calcularTotal().toString());
            rows.add(row);
        });

        Map<String, Object> params = new HashMap<>();
        params.put("dataFiltro",  data.format(FMT));
        params.put("dataGeracao", LocalDate.now().format(FMT));
        params.put("totalRegistros", String.valueOf(reservas.size()));

        gerarPdf(response, "relatorios/reservas-dia.jrxml",
                 params, new JRBeanCollectionDataSource(rows),
                 "reservas-" + data + ".pdf");
    }

    // ── Quartos disponíveis por dia ───────────────────────────────────
    // URL: /relatorios/quartos-disponiveis-pdf?data=yyyy-MM-dd
    @GetMapping("/quartos-disponiveis-pdf")
public void quartosDisponiveisPorDia(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
        HttpServletResponse response) throws IOException {

    List<Object[]> quartos = quartoService.listarDisponiveisPorPeriodo(data, 1);

    List<Map<String, Object>> rows = new ArrayList<>();
    quartos.forEach(q -> {
        Map<String, Object> row = new HashMap<>();
        row.put("numero",    String.valueOf(q[1]));
        row.put("andar",     q[2] + "º");
        row.put("tipo",      String.valueOf(q[4]));
        row.put("descricao", q[3] != null ? String.valueOf(q[3]) : "—");
        row.put("precoDia",  "R$ " + q[5]);
        rows.add(row);
    });

    Map<String, Object> params = new HashMap<>();
    params.put("dataFiltro",  data.format(FMT));
    params.put("dataGeracao", LocalDate.now().format(FMT));

    gerarPdf(response, "relatorios/quartos-disponiveis.jrxml",
             params, new JRBeanCollectionDataSource(rows),
             "quartos-disponiveis-" + data + ".pdf");
}

    
    
    // ── Método central que compila e exporta o PDF ────────────────────
    // D - Dependency Inversion: lógica de geração isolada, controllers dependem deste método
    private void gerarPdf(HttpServletResponse response,
                          String caminhoJrxml,
                          Map<String, Object> params,
                          JRDataSource dataSource,
                          String nomeArquivo) throws IOException {
        try {
            // Carrega o template .jrxml do classpath (resources/)
            InputStream jrxmlStream = new ClassPathResource(caminhoJrxml).getInputStream();

            // Compila o template em memória
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            // Preenche com dados e parâmetros
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            // Configura a resposta HTTP como PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=" + nomeArquivo);

            // Exporta direto para o output stream da response
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());

        } catch (JRException e) {
            throw new IOException("Erro ao gerar relatório PDF: " + e.getMessage(), e);
        }
    }
}
