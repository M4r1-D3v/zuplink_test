package br.com.zup.gerenciadorDePostagem.config.security.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class FiltroAutorizacaoJWT extends BasicAuthenticationFilter {

    @Autowired
    private JWTComponent jwtComponent;
    @Autowired
    private UserDetailsService userDetailsService;

    public FiltroAutorizacaoJWT(AuthenticationManager authenticationManager, JWTComponent jwtComponent,
                                UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.jwtComponent = jwtComponent;
        this.userDetailsService = userDetailsService;
    }

    public UsernamePasswordAuthenticationToken pegarAutenticacao(String token){
        if (!jwtComponent.tokenValido(token)){
            throw new TokenInvalidoException();
        }

        Claims claims = jwtComponent.pegarClaims(token);
        UserDetails usuarioLogado = userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(usuarioLogado,null,usuarioLogado.getAuthorities());
    }

}
