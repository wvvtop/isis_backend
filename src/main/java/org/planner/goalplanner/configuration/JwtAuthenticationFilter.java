package org.planner.goalplanner.configuration;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.planner.goalplanner.repository.UserRepository;
import org.planner.goalplanner.service.JwtService;
import org.planner.goalplanner.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    private final UserRepository userRepository;
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Если нет заголовка или он не начинается с Bearer — сразу пропускаем
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Убираем "Bearer " и лишние пробелы
            final String jwt = authHeader.substring(7).trim();

            // Дополнительная защита от пустой строки
            if (jwt.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            final String email = jwtService.extractEmail(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    userRepository.findByEmail(email).ifPresent(user -> {
                        user.setLastActiveAt(LocalDateTime.now());
                        userRepository.save(user);
                    });
                }
            }
        } catch (ExpiredJwtException e) {
            // Токен истёк — отправляем 401
            System.err.println("JWT Filter Error for " + request.getRequestURI() + ": " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expired\", \"message\": \"" + e.getMessage() + "\"}");
            return; // прерываем цепочку фильтров
        } catch (Exception e) {
            // Другие ошибки (невалидная подпись и т.п.) — тоже 401
            System.err.println("JWT Filter Error for " + request.getRequestURI() + ": " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token\", \"message\": \"" + e.getMessage() + "\"}");
            return;
        }
//        } catch (Exception e) {
//            // Критично: НЕ бросаем исключение, иначе весь запрос падает
//            // Просто логируем и продолжаем без аутентификации
//            System.err.println("JWT Filter Error for " + request.getRequestURI() + ": " + e.getMessage());
//            // В продакшене лучше использовать Logger
//        }


        filterChain.doFilter(request, response);
    }
}
