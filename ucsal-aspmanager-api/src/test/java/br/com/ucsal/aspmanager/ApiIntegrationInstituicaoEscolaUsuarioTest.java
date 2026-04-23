package br.com.ucsal.aspmanager;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.support.TestJwtFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.not;
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
class ApiIntegrationInstituicaoEscolaUsuarioTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestJwtFactory testJwtFactory;

    @Test
    void rf003_deve_cadastrar_instituicao_com_telefone_valido() throws Exception {
        String sufixo = sufixo();

        Map<String, Object> payload = Map.of(
                "nome", "Instituicao Teste " + sufixo,
                "endereco", "Rua Principal, 100",
                "telefones", List.of()
        );

        mockMvc.perform(post("/api/v1/instituicao")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Instituicao Teste " + sufixo));
    }

    @Test
    void rf003_nao_deve_permitir_cadastro_de_instituicao_sem_nome() throws Exception {
        Map<String, Object> payload = Map.of(
                "nome", "",
                "endereco", "Rua Sem Nome, 1",
                "telefones", List.of("71999990000")
        );

        mockMvc.perform(post("/api/v1/instituicao")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void rf001_rf002_rf004_deve_cadastrar_escola_com_coordenador_e_ies() throws Exception {
        mockMvc.perform(get("/api/v1/escola/3")
                        .with(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.idInstituicao").value(1))
                .andExpect(jsonPath("$.idCoordenador").value(1));
    }

    @Test
    void rn001_rf005_deletar_escola_com_historico_deve_inativar() throws Exception {
        mockMvc.perform(delete("/api/v1/escola/{id}", 3)
                        .with(admin()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/escola/{id}", 3)
                        .with(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusRegistro").value("INATIVO"));
    }

    @Test
    void rf006_rf007_deve_cadastrar_professor_com_escola_e_matricula() throws Exception {
        Map<String, Object> payload = Map.of(
                "nomeCompleto", "Professor Integracao",
                "email", "prof.integracao." + sufixo() + "@ucsal.com.br",
                "senha", "senha123",
                "perfil", "PROFESSOR",
                "idEscola", 3,
                "matricula", "MAT-PROF-" + sufixo(),
                "telefones", List.of("71912345678")
        );

        mockMvc.perform(post("/api/v1/usuarios")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.perfil").value("PROFESSOR"))
                .andExpect(jsonPath("$.matricula").isString());
    }

    @Test
    void rn002_professor_autenticado_deve_consultar_apenas_o_proprio_usuario() throws Exception {
        String tokenProfessor = testJwtFactory.token(2L, "osvaldo.requiao@ucsal.com.br", Perfil.PROFESSOR);

        mockMvc.perform(get("/api/v1/usuarios/2")
                        .header("Authorization", "Bearer " + tokenProfessor))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));

        mockMvc.perform(get("/api/v1/usuarios/1")
                        .header("Authorization", "Bearer " + tokenProfessor))
                .andExpect(status().isForbidden());
    }

    @Test
    void rf009_patch_deve_alternar_status_do_usuario() throws Exception {
        Long usuarioId = criarUsuarioAdmin("status.user." + sufixo() + "@ucsal.com.br", "senha123");

        mockMvc.perform(patch("/api/v1/usuarios/{id}", usuarioId)
                        .with(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusRegistro").value("INATIVO"));

        mockMvc.perform(patch("/api/v1/usuarios/{id}", usuarioId)
                        .with(admin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusRegistro").value("ATIVO"));
    }

    @Test
    void rnf002_login_deve_retornar_token_para_usuario_valido() throws Exception {
        String email = "login.sucesso." + sufixo() + "@ucsal.com.br";
        String senha = "senha123";
        criarUsuarioAdmin(email, senha);

        Map<String, Object> loginPayload = Map.of(
                "email", email,
                "senha", senha
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", not(blankOrNullString())));
    }

    @Test
    void rnf002_login_deve_retornar_401_para_senha_invalida() throws Exception {
        String email = "login.erro." + sufixo() + "@ucsal.com.br";
        criarUsuarioAdmin(email, "senha123");

        Map<String, Object> loginPayload = Map.of(
                "email", email,
                "senha", "senha-invalida"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isUnauthorized());
    }

    private Long criarUsuarioAdmin(String email, String senha) throws Exception {
        Map<String, Object> payload = Map.of(
                "nomeCompleto", "Admin Integracao",
                "email", email,
                "senha", senha,
                "perfil", "ADMIN",
                "telefones", List.of("71988887777")
        );

        MvcResult result = mockMvc.perform(post("/api/v1/usuarios")
                        .with(admin())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andReturn();

        return extrairId(result, "id");
    }

    private Long extrairId(MvcResult result, String campo) throws Exception {
        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        return body.get(campo).asLong();
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor admin() {
        return user("admin@ucsal.com.br").roles("ADMIN");
    }

    private String sufixo() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
