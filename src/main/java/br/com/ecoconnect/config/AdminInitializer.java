package br.com.ecoconnect.config;

import br.com.ecoconnect.model.TipoUsuario;
import br.com.ecoconnect.model.Usuario;
import br.com.ecoconnect.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Cria o administrador padrão (RN11) na primeira execução. */
@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner criarAdmin(UsuarioRepository repo, PasswordEncoder encoder,
                                 @Value("${admin.senha:admin123}") String senhaAdmin) {
        return args -> {
            String email = "admin@ecoconnect.com";
            if (!repo.existsByEmail(email)) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail(email);
                admin.setSenha(encoder.encode(senhaAdmin));
                admin.setTipo(TipoUsuario.ADMINISTRADOR);
                repo.save(admin);
                System.out.println(">> Admin criado: " + email);
            }
        };
    }
}
