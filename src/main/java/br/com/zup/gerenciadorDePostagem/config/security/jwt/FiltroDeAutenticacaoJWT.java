package br.com.zup.gerenciadorDePostagem.config.security.jwt;

import br.com.zup.gerenciadorDePostagem.config.security.UsuarioLogado;
import br.com.zup.gerenciadorDePostagem.config.security.jwt.exceptions.AcessoNegadoException;
import br.com.zup.gerenciadorDePostagem.usuario.dtos.LoginDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class FiltroDeAutenticacaoJWT extends UsernamePasswordAuthenticationFilter {

    private JWTComponent jwtComponent;
    private AuthenticationManager authenticationManager;

    public FiltroDeAutenticacaoJWT(JWTComponent jwtComponent, AuthenticationManager authenticationManager) {
        this.jwtComponent = jwtComponent;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            LoginDto login =  objectMapper.readValue(request.getInputStream(), LoginDto.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    login.getEmail(), login.getSenha(), new ArrayList<>()
            );

            Authentication autenticacao = authenticationManager.authenticate(authToken);
            return autenticacao;
        }catch (IOException e){
            throw new AcessoNegadoException();
        }


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UsuarioLogado usuarioLogado = (UsuarioLogado) authResult.getPrincipal();
        String username = usuarioLogado.getUsername();
        String id = usuarioLogado.getId();

        String token = jwtComponent.gerarToken(username, id);

        response.setHeader("Access-Control-Expose-Headers","Authorization");
        response.addHeader("Authorization", "Token "+token);
    }







}
