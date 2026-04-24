package br.com.ucsal.aspmanager;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(br.com.ucsal.aspmanager.support.TestSecurityConfig.class)
class ApiIntegrationSoftwareSolicitacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void rf018_rf022_admin_deve_cadastrar_software() throws Exception {
        Map<String, Object> payload = novoSoftwarePayload(List.of());

        mockMvc.perform(post("/api/v1/software")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").isString())
                .andExpect(jsonPath("$.statusRegistro").value("ATIVO"));
    }

    @Test
    void rn010_rf019_rf020_deletar_software_com_historico_deve_inativar() throws Exception {
        Long softwareId = criarSoftware();

        Map<String, Object> espacoPayload = Map.of(
                "sigla", "LAB-" + sufixo().substring(0, 4),
                "nome", "Laboratório com Software " + sufixo(),
                "descricao", "Espaço vinculado ao software",
                "capacidadeMaxima", 20,
                "localizacao", "Bloco B",
                "tipoComputadores", "Desktop",
                "tipoEspaco", "LABORATORIO",
                "idEscola", 3,
                "softwares", List.of(softwareId)
        );

        mockMvc.perform(post("/api/v1/espaco")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(espacoPayload)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/api/v1/software/{id}", softwareId)
                        .with(admin()))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/software/{id}", softwareId)
                .with(admin()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.statusRegistro").value("INATIVO"));
    }

    @Test
    void rf021_professor_deve_criar_solicitacao_de_software() throws Exception {
        Map<String, Object> payload = novaSolicitacaoSoftwarePayload();

        mockMvc.perform(post("/api/v1/software/solicitacoes")
                        .with(professor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.statusSolicitacao").value("PENDENTE"))
                .andExpect(jsonPath("$.tipoSolicitacaoSoftware").value("ATIVACAO"));
    }

    @Test
    void rf041_professor_deve_consultar_minhas_solicitacoes() throws Exception {
        criarSolicitacaoSoftware();

        mockMvc.perform(get("/api/v1/software/solicitacoes/minhas")
                        .with(professorAutenticado()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void rf021_admin_deve_aprovar_solicitacao_e_criar_software_automaticamente() throws Exception {
        Long solicitacaoId = criarSolicitacaoSoftware();

        Map<String, Object> payload = Map.of("statusSolicitacao", "APROVADO");

        MvcResult result = mockMvc.perform(patch("/api/v1/software/solicitacoes/{id}", solicitacaoId)
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusSolicitacao").value("APROVADO"))
                .andExpect(jsonPath("$.idSoftwareCriado").isNumber())
                .andReturn();

        Long idSoftwareCriado = extrairId(result, "idSoftwareCriado");

        mockMvc.perform(get("/api/v1/software/{id}", idSoftwareCriado)
                        .with(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(idSoftwareCriado));
    }

    @Test
    void rnf002_apenas_admin_pode_analisar_solicitacao_de_software() throws Exception {
        Long solicitacaoId = criarSolicitacaoSoftware();

        Map<String, Object> payload = Map.of("statusSolicitacao", "REPROVADO");

        mockMvc.perform(patch("/api/v1/software/solicitacoes/{id}", solicitacaoId)
                        .with(professor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());
    }

    @Test
    void regra_de_negocio_nao_deve_permitir_reanalise_de_solicitacao_ja_analisada() throws Exception {
        Long solicitacaoId = criarSolicitacaoSoftware();

        Map<String, Object> aprovada = Map.of("statusSolicitacao", "APROVADO");

        mockMvc.perform(patch("/api/v1/software/solicitacoes/{id}", solicitacaoId)
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aprovada)))
                .andExpect(status().isOk());

        mockMvc.perform(patch("/api/v1/software/solicitacoes/{id}", solicitacaoId)
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(aprovada)))
            .andExpect(status().isConflict());
    }

    private Long criarSoftware() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/software")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novoSoftwarePayload(List.of()))))
                .andExpect(status().isCreated())
                .andReturn();

        return extrairId(result, "id");
    }

    private Long criarSolicitacaoSoftware() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/software/solicitacoes")
                        .with(professor())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novaSolicitacaoSoftwarePayload())))
                .andExpect(status().isCreated())
                .andReturn();

        return extrairId(result, "id");
    }

    private Map<String, Object> novaSolicitacaoSoftwarePayload() {
        return Map.of(
                "idProfessor", 1,
                "software", novoSoftwarePayload(List.of())
        );
    }

    private Map<String, Object> novoSoftwarePayload(List<Long> disciplinas) {
        return Map.of(
                "nome", "Software " + sufixo(),
                "versao", "v" + sufixo().substring(0, 3),
                "urlDownload", "https://example.com/download/" + sufixo(),
                "tipoLicenca", "Educacional",
                "objetivoUso", "Uso em laboratório acadêmico",
                "idDisciplinas", disciplinas
        );
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

    private org.springframework.test.web.servlet.request.RequestPostProcessor professorAutenticado() {
        UsuarioResponse usuario = new UsuarioResponse(
                2L,
                "OsvaldoRequiao Mello",
                "osvaldo.requiao@ucsal.com.br",
                Perfil.PROFESSOR,
                StatusRegistro.ATIVO,
                "200033111",
                3L,
                List.of()
        );

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                usuario,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_PROFESSOR"))
        );

        return SecurityMockMvcRequestPostProcessors.authentication(auth);
    }

    private String sufixo() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
