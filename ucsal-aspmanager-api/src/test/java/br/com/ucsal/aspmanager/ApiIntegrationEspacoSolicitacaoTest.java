package br.com.ucsal.aspmanager;

import br.com.ucsal.aspmanager.espaco.repository.SolicitacaoEspacoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(br.com.ucsal.aspmanager.support.TestSecurityConfig.class)
class ApiIntegrationEspacoSolicitacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SolicitacaoEspacoRepository solicitacaoEspacoRepository;

    @Test
    void rf010_rf013_rf014_rf015_rf016_deve_cadastrar_espaco_com_status_ativo() throws Exception {
        Map<String, Object> payload = novoEspacoPayload("LABORATORIO", List.of());

        mockMvc.perform(post("/api/v1/espaco")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipoEspaco").value("LABORATORIO"))
                .andExpect(jsonPath("$.statusRegistro").value("ATIVO"))
                .andExpect(jsonPath("$.idEscola").value(3));
    }

    @Test
    void rf013_nao_deve_permitir_tipo_de_espaco_fora_do_enum() throws Exception {
        Map<String, Object> payload = novoEspacoPayload("QUADRA", List.of());

        mockMvc.perform(post("/api/v1/espaco")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void rf029_rn012_apenas_professor_autenticado_pode_solicitar_reserva() throws Exception {
        Long idEspaco = criarEspaco();

        Map<String, Object> payload = Map.of(
                "descricao", "Aula prática de integração",
                "dataUso", LocalDate.now().plusDays(3).toString(),
                "horaInicio", LocalTime.of(8, 0).toString(),
                "horaFim", LocalTime.of(10, 0).toString(),
                "idEspaco", idEspaco,
                "idProfessor", 1
        );

        mockMvc.perform(post("/api/v1/espaco/solicitacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/v1/espaco/solicitacao")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/v1/espaco/solicitacao")
                        .with(professor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEspaco").value(idEspaco))
                .andExpect(jsonPath("$.idProfessor").value(1))
                .andExpect(jsonPath("$.statusSolicitacao").value("PENDENTE"));
    }

    @Test
    void rf037_deve_validar_campos_obrigatorios_da_solicitacao() throws Exception {
        Map<String, Object> payload = Map.of(
                "descricao", "Sem dados obrigatórios"
        );

        mockMvc.perform(post("/api/v1/espaco/solicitacao")
                        .with(professor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rf028_rf040_busca_disponiveis_deve_considerar_reservas_aprovadas_no_periodo() throws Exception {
        Long idEspaco = criarEspaco();
        LocalDate data = LocalDate.now().plusDays(5);

        Long solicitacaoId = criarSolicitacao(idEspaco, data, LocalTime.of(8, 0), LocalTime.of(10, 0));

        Map<String, Object> aprovacao = Map.of(
                "idEspaco", idEspaco,
                "idProfessor", 1,
                "statusSolicitacao", "APROVADO"
        );

        mockMvc.perform(put("/api/v1/espaco/solicitacao/{id}", solicitacaoId)
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aprovacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSolicitacao").value("APROVADO"));

        MvcResult sobreposto = mockMvc.perform(get("/api/v1/espaco/disponiveis")
                        .with(admin())
                        .queryParam("dataUso", data.toString())
                        .queryParam("horaInicio", "08:30:00")
                        .queryParam("horaFim", "09:30:00"))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult livre = mockMvc.perform(get("/api/v1/espaco/disponiveis")
                        .with(admin())
                        .queryParam("dataUso", data.toString())
                        .queryParam("horaInicio", "10:30:00")
                        .queryParam("horaFim", "11:30:00"))
                .andExpect(status().isOk())
                .andReturn();

        assertContemIdNoConteudo(sobreposto.getResponse().getContentAsString(), idEspaco, false);
        assertContemIdNoConteudo(livre.getResponse().getContentAsString(), idEspaco, true);
    }

    @Test
    void rn007_rf011_rf012_deletar_espaco_com_historico_deve_inativar() throws Exception {
        Long idEspaco = criarEspaco();

        criarSolicitacao(idEspaco, LocalDate.now().plusDays(7), LocalTime.of(14, 0), LocalTime.of(16, 0));

        mockMvc.perform(delete("/api/v1/espaco/{id}", idEspaco)
                        .with(admin()))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/espaco/{id}", idEspaco)
                .with(admin()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusRegistro").value("INATIVO"));
    }

    private Long criarEspaco() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/espaco")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoEspacoPayload("LABORATORIO", List.of()))))
                .andExpect(status().isCreated())
                .andReturn();

        return extrairId(result, "id");
    }

    private Long criarSolicitacao(Long idEspaco, LocalDate data, LocalTime inicio, LocalTime fim) throws Exception {
        Map<String, Object> payload = Map.of(
                "descricao", "Solicitação automática",
                "dataUso", data.toString(),
                "horaInicio", inicio.toString(),
                "horaFim", fim.toString(),
                "idEspaco", idEspaco,
                "idProfessor", 1
        );

        MvcResult result = mockMvc.perform(post("/api/v1/espaco/solicitacao")
                        .with(professor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();

        return solicitacaoEspacoRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
            .getFirst()
            .getId();
    }

    private Map<String, Object> novoEspacoPayload(String tipo, List<Long> softwares) {
        return Map.of(
                "sigla", "LAB-" + sufixo().substring(0, 4),
                "nome", "Laboratório " + sufixo(),
                "descricao", "Laboratório para testes de integração",
                "capacidadeMaxima", 30,
                "localizacao", "Prédio A - Sala 101",
                "tipoComputadores", "Desktop",
                "tipoEspaco", tipo,
                "idEscola", 3,
                "softwares", softwares
        );
    }

    private void assertContemIdNoConteudo(String body, Long idEspaco, boolean esperado) throws Exception {
        JsonNode root = objectMapper.readTree(body);
        JsonNode content = root.get("content");

        boolean encontrou = false;
        if (content != null && content.isArray()) {
            for (JsonNode item : content) {
                if (item.has("id") && item.get("id").asLong() == idEspaco) {
                    encontrou = true;
                    break;
                }
            }
        }

        if (esperado && !encontrou) {
            throw new AssertionError("Espaço esperado não foi encontrado na lista de disponíveis");
        }

        if (!esperado && encontrou) {
            throw new AssertionError("Espaço não deveria estar disponível para período sobreposto");
        }
    }

    private Long extrairId(MvcResult result, String campo) throws Exception {
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get(campo).asLong();
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin() {
        return user("admin@ucsal.com.br").roles("ADMIN");
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor professor() {
        return user("osvaldo.requiao@ucsal.com.br").roles("PROFESSOR");
    }

    private String sufixo() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
