package com.kirbey.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

//import com.javainuse.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final String[] NON_AUTH_HEADER_URIS = {"/auth/login", "/auth/register"};

	@Autowired
	private JWTTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		// JWT Token is in the form "Bearer token". Remove Bearer word and get
		// only the Token
		if (requestTokenHeader == null || requestTokenHeader.isEmpty()){
			if(!isAPermittedSite(request.getRequestURI()))
				System.out.println("Se necesita el header \"Authorization\" con el token correspondiente");
				
			chain.doFilter(request, response);
			return;
		}
		if (!requestTokenHeader.startsWith("Bearer ")){
			System.out.println("JWT Token no comienza con \"Bearer \"");
			chain.doFilter(request, response);
			return;
		}

		jwtToken = requestTokenHeader.substring(7);
		try {
			username = jwtTokenUtil.getUsernameFromToken(jwtToken);
		} catch (IllegalArgumentException e) {
			System.out.println("Incapaz de obtener el Token JWT");
		} catch (ExpiredJwtException e) {
			System.out.println("El token JWT expiró");
		} catch (MalformedJwtException e){
			System.out.println("El token tiene un formato incorrecto");
		}

		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}

	private Boolean isAPermittedSite(String uri){
		for (String permittedUriElement: NON_AUTH_HEADER_URIS){
			if(permittedUriElement.equals(uri)) return true;
		}
		return false;
	}
}
