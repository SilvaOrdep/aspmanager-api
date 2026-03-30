package br.com.ucsal.aspmanager.auth;

import br.com.ucsal.aspmanager.usuario.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private UsuarioRepository usuarios;

    public AuthService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarios.findUsuarioByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }

}
