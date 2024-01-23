package hyunni.rest.auth.config;

import hyunni.rest.auth.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        //first Step of JwtAunthenticationFilter: Check Jwt Token
        final String authHeader = request.getHeader("authorization");
        final String jwt;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("BEARER ")) {
            filterChain.doFilter(request, response);
            return;
        }

        //second step of JwtAunthenticationFilter: extract jwt Token from authentication header
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        //third step, validation proccess
        // if the userEmail is there and user is not authenticated, we get the UserDetails from the dataBase
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            // then we check if the user is valid or not
            // if valid, we create an object of type UsernamePasswordAuthenticationToken
            // and pass userDetails, credentials, and authorities as parameters
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                // Then we inforce the authentication token with the details of our request
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // Then update the authentication token
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            // Need to pass this for the next filter to proceed
            filterChain.doFilter(request, response);
        }
    }
}
