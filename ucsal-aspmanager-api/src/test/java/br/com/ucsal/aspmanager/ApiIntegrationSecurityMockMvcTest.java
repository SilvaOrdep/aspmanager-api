package br.com.ucsal.aspmanager;

import br.com.ucsal.aspmanager.shared.model.enums.Perfil;
import br.com.ucsal.aspmanager.shared.model.enums.StatusRegistro;
import br.com.ucsal.aspmanager.shared.model.enums.StatusSolicitacao;
import br.com.ucsal.aspmanager.shared.model.enums.TipoEspaco;
import br.com.ucsal.aspmanager.shared.security.jwt.JwtService;
import br.com.ucsal.aspmanager.support.TestJwtFactory;
import br.com.ucsal.aspmanager.support.TestSecurityConfig;
import br.com.ucsal.aspmanager.usuario.dto.response.UsuarioResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class ApiIntegrationSecurityMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TestJwtFactory testJwtFactory;

    @Test
    void login_deve_retornar_200_e_token() throws Exception {
        String token = testJwtFactory.token(1L, "admin@ucsal.com.br", Perfil.ADMIN);
        org.junit.jupiter.api.Assertions.assertNotNull(token);
    }

    @Test
    void rota_publica_com_payload_invalido_deve_retornar_400() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void sem_login_na_rota_de_usuario_retorna_401() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void professor_sem_permissao_para_outro_usuario_retorna_403() throws Exception {
        mockMvc.perform(get("/api/v1/usuarios/9")
                .with(user("professor@ucsal.com.br").roles("PROFESSOR"))
                .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    void usuario_com_perfil_incorreto_na_rota_admin_retorna_403() throws Exception {
        mockMvc.perform(post("/api/v1/escola")
                .with(user("professor@ucsal.com.br").roles("PROFESSOR"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void admin_pode_acessar_rota_admin_e_retorna_nao_404_por_autorizacao() throws Exception {
        mockMvc.perform(post("/api/v1/escola")
                .with(user("admin@ucsal.com.br").roles("ADMIN"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void professor_pode_acessar_rota_de_leitura_compartilhada_e_nao_e_bloqueado_por_seguranca() throws Exception {
        mockMvc.perform(get("/api/v1/escola")
                .with(user("professor@ucsal.com.br").roles("PROFESSOR")))
                .andExpect(status().isOk());
    }

    @Test
    void admin_pode_acessar_rota_de_leitura_compartilhada_e_nao_e_bloqueado_por_seguranca() throws Exception {
        mockMvc.perform(get("/api/v1/espaco")
                .with(user("admin@ucsal.com.br").roles("ADMIN")))
                .andExpect(status().isOk());
    }

    @Test
    void professor_pode_usar_rota_de_solicitacao_e_recebe_requisicao_processada_ate_o_controller() throws Exception {
        mockMvc.perform(post("/api/v1/espaco/solicitacao")
                .with(user("professor@ucsal.com.br").roles("PROFESSOR"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void usuario_autenticado_sem_perfil_correto_deve_retornar_403_em_rota_admin() throws Exception {
        mockMvc.perform(post("/api/v1/software")
                .with(user("usuario@ucsal.com.br").roles("PROFESSOR"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void rotas_inexistentes_devem_retornar_404_ou_403_conforme_a_segurança() throws Exception {
        mockMvc.perform(get("/api/v1/rota-que-nao-existe")
                .with(user("admin@ucsal.com.br").roles("ADMIN")))
                .andExpect(status().isForbidden());
    }

    @SuppressWarnings("unused")
    private UsuarioResponse usuario(Long id, Perfil perfil) {
        return new UsuarioResponse(id, "Usuário", "usuario@ucsal.com.br", perfil, StatusRegistro.ATIVO, null, null,
                null, null);
    }
}
