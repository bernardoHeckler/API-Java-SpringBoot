package br.edu.atitus.api_sample.components;

import br.edu.atitus.api_sample.entities.UserEntity;
import br.edu.atitus.api_sample.entities.UserType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ExtractEmail {

    public static String extrairEmail() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new Exception("Usuário não está autenticado");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserEntity) {
            return ((UserEntity) principal).getEmail();
        }

        throw new Exception("Usuário não existe ou não está autenticado");
    }
}