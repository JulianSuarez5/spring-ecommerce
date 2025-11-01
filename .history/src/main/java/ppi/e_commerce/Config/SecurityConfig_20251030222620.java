package ppi.e_commerce.Config;package ppi.e_commerce.Config;



import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.core.userdetails.UserDetailsService;import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UsernameNotFoundException;import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.core.userdetails.User;import org.springframework.security.core.userdetails.User;

import ppi.e_commerce.Repository.UserRepository;import ppi.e_commerce.Repository.UserRepository;

import ppi.e_commerce.Service.AuthServiceImpl;import ppi.e_commerce.Service.AuthServiceImpl;



import org.springframework.security.web.SecurityFilterChain;import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.AuthenticationSuccessHandler;import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import org.springframework.security.web.authentication.AuthenticationFailureHandler;import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import org.springframework.security.core.Authentication;import org.springframework.security.core.Authentication;

import jakarta.servlet.ServletException;import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpServletResponse;import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;import java.io.IOException;

import java.util.Collection;import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;import org.springframework.security.core.GrantedAuthority;

import org.springframework.beans.factory.annotation.Autowired;import org.springframework.beans.factory.annotation.Autowired;



@Configuration@Configuration

@EnableWebSecurity@EnableWebSecurity

public class SecurityConfig {public class SecurityConfig {



    @Autowired    @Autowired

    private UserRepository userRepository;    private UserRepository userRepository;



    @Autowired    @Autowired

    private AuthServiceImpl authService;    private AuthServiceImpl authService;



    @Bean    @Bean

    public PasswordEncoder passwordEncoder() {    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();        return new BCryptPasswordEncoder();

    }    }



    @Bean    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http        http

            .authorizeHttpRequests(authz -> authz            .authorizeHttpRequests(authz -> authz

                // Rutas públicas (sin autenticación)                // Rutas públicas (sin autenticación)

                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/vendor/**", "/webjars/**").permitAll()                .requestMatchers("/", "/css/**", "/js/**", "/images/**", "/vendor/**", "/webjars/**").permitAll()

                .requestMatchers("/login", "/register", "/admin/login", "/admin/register").permitAll()                .requestMatchers("/login", "/register", "/admin/login", "/admin/register").permitAll()

                                

                // Rutas de recuperación de contraseña (SIN autenticación)                // Rutas de recuperación de contraseña (SIN autenticación)

                .requestMatchers("/auth/forgot-password").permitAll()                .requestMatchers("/auth/forgot-password").permitAll()

                                

                .requestMatchers("/products", "/products/**", "/categories/**", "/brands/**").permitAll()                .requestMatchers("/products", "/products/**", "/categories/**", "/brands/**").permitAll()

                .requestMatchers("/error").permitAll()                .requestMatchers("/error").permitAll()

                                

                // Rutas de ADMINISTRADOR (SOLO ROLE_ADMIN)                // Rutas de ADMINISTRADOR (SOLO ROLE_ADMIN)

                .requestMatchers("/admin/**").hasRole("ADMIN")                .requestMatchers("/admin/**").hasRole("ADMIN")

                                

                // Rutas de USUARIO AUTENTICADO (USER o ADMIN)                // Rutas de USUARIO AUTENTICADO (USER o ADMIN)

                .requestMatchers("/cart/**", "/orders/**", "/payment/**", "/profile/**").authenticated()                .requestMatchers("/cart/**", "/orders/**", "/payment/**", "/profile/**").authenticated()

                                

                // Cambio de contraseña REQUIERE estar autenticado (con contraseña temporal)                // Cambio de contraseña REQUIERE estar autenticado (con contraseña temporal)

                .requestMatchers("/auth/change-password").authenticated()                .requestMatchers("/auth/change-password").authenticated()

                                

                // Todas las demás rutas requieren autenticación                // Todas las demás rutas requieren autenticación

                .anyRequest().authenticated()                .anyRequest().authenticated()

            )            )

            .formLogin(form -> form            .formLogin(form -> form

                .loginPage("/login")                .loginPage("/login")

                .loginProcessingUrl("/login")                .loginProcessingUrl("/login")

                .successHandler(authenticationSuccessHandler())                .successHandler(authenticationSuccessHandler())

                .failureHandler(authenticationFailureHandler())                .failureHandler(authenticationFailureHandler())

                .permitAll()                .permitAll()

            )            )

            .logout(logout -> logout            .logout(logout -> logout

                .logoutUrl("/logout")                .logoutUrl("/logout")

                .logoutSuccessUrl("/?logout=true")                .logoutSuccessUrl("/?logout=true")

                .invalidateHttpSession(true)                .invalidateHttpSession(true)

                .deleteCookies("JSESSIONID")                .deleteCookies("JSESSIONID")

                .permitAll()                .permitAll()

            )            )

            .sessionManagement(session -> session            .sessionManagement(session -> session

                .maximumSessions(1)                .maximumSessions(1)

                .maxSessionsPreventsLogin(false)                .maxSessionsPreventsLogin(false)

            )            )

            .exceptionHandling(exceptions -> exceptions            .exceptionHandling(exceptions -> exceptions

                .accessDeniedPage("/access-denied")                .accessDeniedPage("/access-denied")

            )            )

            .csrf(csrf -> csrf            .csrf(csrf -> csrf

                .ignoringRequestMatchers("/cart/**", "/orders/**", "/payment/**")                .ignoringRequestMatchers("/cart/**", "/orders/**", "/payment/**")

            );            );



        return http.build();        return http.build();

    }    }



    @Bean    @Bean

    public UserDetailsService userDetailsService(UserRepository userRepository, AuthServiceImpl authService) {    public UserDetailsService userDetailsService(UserRepository userRepository, AuthServiceImpl authService) {

        return username -> {        return username -> {

            System.out.println("🔍 Intentando autenticar usuario: " + username);            System.out.println("🔍 Intentando autenticar usuario: " + username);

                        

            // Buscar por username O email            // Buscar por username O email

            java.util.Optional<ppi.e_commerce.Model.User> maybeUser = userRepository.findByUsername(username);            java.util.Optional<ppi.e_commerce.Model.User> maybeUser = userRepository.findByUsername(username);

            if (maybeUser.isEmpty()) {            if (maybeUser.isEmpty()) {

                maybeUser = userRepository.findByEmail(username);                maybeUser = userRepository.findByEmail(username);

            }            }



            ppi.e_commerce.Model.User appUser = maybeUser            ppi.e_commerce.Model.User appUser = maybeUser

                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));



            // Verificar que el usuario esté activo            // Verificar que el usuario esté activo

            if (!appUser.isActive()) {            if (!appUser.isActive()) {

                System.out.println("❌ Usuario inactivo: " + username);                System.out.println("❌ Usuario inactivo: " + username);

                throw new org.springframework.security.authentication.DisabledException("Usuario desactivado");                throw new org.springframework.security.authentication.DisabledException("Usuario desactivado");

            }            }



            // Normalizar el rol            // Normalizar el rol

            String rawRole = appUser.getRole();            String rawRole = appUser.getRole();

            if (rawRole == null || rawRole.isBlank()) {            if (rawRole == null || rawRole.isBlank()) {

                rawRole = "USER";                rawRole = "USER";

            }            }

                        

            rawRole = rawRole.trim().toUpperCase();            rawRole = rawRole.trim().toUpperCase();

            if (rawRole.startsWith("ROLE_")) {            if (rawRole.startsWith("ROLE_")) {

                rawRole = rawRole.substring(5);                rawRole = rawRole.substring(5);

            }            }

                        

            String finalRole = rawRole;            String finalRole = rawRole;

                        

            System.out.println("✅ Usuario encontrado: " + appUser.getUsername());            System.out.println("✅ Usuario encontrado: " + appUser.getUsername());

            System.out.println("📋 Role: " + finalRole);            System.out.println("📋 Role: " + finalRole);

            System.out.println("🟢 Activo: " + appUser.isActive());            System.out.println("🟢 Activo: " + appUser.isActive());

            System.out.println("🔐 Usando contraseña temporal: " + appUser.isUsingTempPassword());            System.out.println("🔐 Usando contraseña temporal: " + appUser.isUsingTempPassword());



            // CRÍTICO: Determinar qué contraseña usar            // CRÍTICO: Determinar qué contraseña usar

            String passwordToUse;            String passwordToUse;

            if (authService.estaUsandoContrasenaTemporal(appUser)) {            if (authService.estaUsandoContrasenaTemporal(appUser)) {

                passwordToUse = appUser.getTempPasswordHash();                passwordToUse = appUser.getTempPasswordHash();

                System.out.println("🔑 Usando contraseña temporal");                System.out.println("🔑 Usando contraseña temporal");

            } else {            } else {

                passwordToUse = appUser.getPassword();                passwordToUse = appUser.getPassword();

                System.out.println("🔑 Usando contraseña permanente");                System.out.println("🔑 Usando contraseña permanente");

            }            }



            return User.withUsername(appUser.getUsername())            return User.withUsername(appUser.getUsername())

                .password(passwordToUse)                .password(passwordToUse)

                .roles(finalRole)                .roles(finalRole)

                .disabled(!appUser.isActive())                .disabled(!appUser.isActive())

                .build();                .build();

        };        };

    }    }



    @Bean    @Bean

    public AuthenticationFailureHandler authenticationFailureHandler() {    public AuthenticationFailureHandler authenticationFailureHandler() {

        return (request, response, exception) -> {        return (request, response, exception) -> {

            System.out.println("❌ Fallo de autenticación: " + exception.getMessage());            System.out.println("❌ Fallo de autenticación: " + exception.getMessage());

                        

            String adminParam = request.getParameter("admin");            String adminParam = request.getParameter("admin");

            boolean attemptedAdminLogin = adminParam != null && "true".equalsIgnoreCase(adminParam);            boolean attemptedAdminLogin = adminParam != null && "true".equalsIgnoreCase(adminParam);



            String target;            String target;

            if (attemptedAdminLogin) {            if (attemptedAdminLogin) {

                if (exception instanceof org.springframework.security.authentication.DisabledException) {                if (exception instanceof org.springframework.security.authentication.DisabledException) {

                    target = "/admin/login?disabled=true";                    target = "/admin/login?disabled=true";

                } else {                } else {

                    target = "/admin/login?error=true";                    target = "/admin/login?error=true";

                }                }

            } else {            } else {

                if (exception instanceof org.springframework.security.authentication.DisabledException) {                if (exception instanceof org.springframework.security.authentication.DisabledException) {

                    target = "/login?disabled=true";                    target = "/login?disabled=true";

                } else {                } else {

                    target = "/login?error=true";                    target = "/login?error=true";

                }                }

            }            }

                        

            System.out.println("↪️ Redirigiendo a: " + target);            System.out.println("↪️ Redirigiendo a: " + target);

            response.sendRedirect(request.getContextPath() + target);            response.sendRedirect(request.getContextPath() + target);

        };        };

    }    }



    @Bean    @Bean

    public AuthenticationSuccessHandler authenticationSuccessHandler() {    public AuthenticationSuccessHandler authenticationSuccessHandler() {

        return new AuthenticationSuccessHandler() {        return new AuthenticationSuccessHandler() {

            @Override            @Override

            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,

                                                Authentication authentication) throws IOException, ServletException {                                                Authentication authentication) throws IOException, ServletException {

                                

                System.out.println("\n🎉 Autenticación exitosa!");                System.out.println("\n🎉 Autenticación exitosa!");

                System.out.println("👤 Usuario: " + authentication.getName());                System.out.println("👤 Usuario: " + authentication.getName());

                                

                // ⭐ VERIFICAR SI ESTÁ USANDO CONTRASEÑA TEMPORAL                // ⭐ VERIFICAR SI ESTÁ USANDO CONTRASEÑA TEMPORAL

                String username = authentication.getName();                String username = authentication.getName();

                java.util.Optional<ppi.e_commerce.Model.User> maybeUser = userRepository.findByUsername(username);                java.util.Optional<ppi.e_commerce.Model.User> maybeUser = userRepository.findByUsername(username);

                if (maybeUser.isEmpty()) {                if (maybeUser.isEmpty()) {

                    maybeUser = userRepository.findByEmail(username);                    maybeUser = userRepository.findByEmail(username);

                }                }

                                

                if (maybeUser.isPresent()) {                if (maybeUser.isPresent()) {

                    ppi.e_commerce.Model.User user = maybeUser.get();                    ppi.e_commerce.Model.User user = maybeUser.get();

                                        

                    // Si está usando contraseña temporal, FORZAR cambio de contraseña                    // Si está usando contraseña temporal, FORZAR cambio de contraseña

                    if (authService.estaUsandoContrasenaTemporal(user)) {                    if (authService.estaUsandoContrasenaTemporal(user)) {

                        System.out.println("⚠️ Contraseña temporal detectada - redirigiendo a cambio obligatorio");                        System.out.println("⚠️ Contraseña temporal detectada - redirigiendo a cambio obligatorio");

                        response.sendRedirect(request.getContextPath() + "/auth/change-password?temp=true");                        response.sendRedirect(request.getContextPath() + "/auth/change-password?temp=true");

                        return;                        return;

                    }                    }

                }                }

                                

                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();                Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

                System.out.println("🔑 Authorities: " + authorities);                System.out.println("🔑 Authorities: " + authorities);

                                

                // Verificar si tiene rol ADMIN                // Verificar si tiene rol ADMIN

                boolean isAdmin = authorities.stream()                boolean isAdmin = authorities.stream()

                    .anyMatch(a -> {                    .anyMatch(a -> {

                        String auth = a.getAuthority();                        String auth = a.getAuthority();

                        System.out.println("  - Verificando authority: " + auth);                        System.out.println("  - Verificando authority: " + auth);

                        return auth.equals("ROLE_ADMIN");                        return auth.equals("ROLE_ADMIN");

                    });                    });



                System.out.println("🛡️ Es Admin? " + isAdmin);                System.out.println("🛡️ Es Admin? " + isAdmin);



                String adminParam = request.getParameter("admin");                String adminParam = request.getParameter("admin");

                boolean attemptedAdminLogin = adminParam != null && "true".equalsIgnoreCase(adminParam);                boolean attemptedAdminLogin = adminParam != null && "true".equalsIgnoreCase(adminParam);

                                

                System.out.println("🔐 Intentó login admin? " + attemptedAdminLogin);                System.out.println("🔐 Intentó login admin? " + attemptedAdminLogin);



                String targetUrl;                String targetUrl;

                                

                if (attemptedAdminLogin) {                if (attemptedAdminLogin) {

                    // Intentó acceder al panel admin                    // Intentó acceder al panel admin

                    if (isAdmin) {                    if (isAdmin) {

                        targetUrl = "/admin";                        targetUrl = "/admin";

                        System.out.println("✅ Acceso admin concedido → " + targetUrl);                        System.out.println("✅ Acceso admin concedido → " + targetUrl);

                    } else {                    } else {

                        // NO es admin pero intentó acceder al panel admin                        // NO es admin pero intentó acceder al panel admin

                        System.out.println("⛔ Usuario sin permisos de admin, cerrando sesión");                        System.out.println("⛔ Usuario sin permisos de admin, cerrando sesión");

                        request.getSession().invalidate();                        request.getSession().invalidate();

                        response.sendRedirect(request.getContextPath() + "/admin/login?not_admin=true");                        response.sendRedirect(request.getContextPath() + "/admin/login?not_admin=true");

                        return;                        return;

                    }                    }

                } else {                } else {

                    // Login normal de cliente                    // Login normal de cliente

                    if (isAdmin) {                    if (isAdmin) {

                        // Admin usando login de cliente → redirigir a admin                        // Admin usando login de cliente → redirigir a admin

                        targetUrl = "/admin";                        targetUrl = "/admin";

                        System.out.println("ℹ️ Admin detectado en login cliente → " + targetUrl);                        System.out.println("ℹ️ Admin detectado en login cliente → " + targetUrl);

                    } else {                    } else {

                        // Usuario normal                        // Usuario normal

                        targetUrl = "/products";                        targetUrl = "/products";

                        System.out.println("✅ Usuario normal → " + targetUrl);                        System.out.println("✅ Usuario normal → " + targetUrl);

                    }                    }

                }                }

                                

                System.out.println("↪️ Redirigiendo a: " + targetUrl + "\n");                System.out.println("↪️ Redirigiendo a: " + targetUrl + "\n");

                response.sendRedirect(request.getContextPath() + targetUrl);                response.sendRedirect(request.getContextPath() + targetUrl);

            }            }

        };        };

    }    }

}}