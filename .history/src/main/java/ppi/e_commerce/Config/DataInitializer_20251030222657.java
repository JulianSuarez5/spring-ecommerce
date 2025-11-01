package ppi.e_commerce.Config;package ppi.e_commerce.Config;



import org.springframework.boot.CommandLineRunner;import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;import org.springframework.context.annotation.Configuration;

import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Value;import org.springframework.beans.factory.annotation.Value;

import ppi.e_commerce.Model.User;import ppi.e_commerce.Model.User;

import ppi.e_commerce.Repository.UserRepository;import ppi.e_commerce.Repository.UserRepository;



@Configuration@Configuration

public class DataInitializer {public class DataInitializer {



    @Value("${app.seed.create-default-admin:false}")    @Value("${app.seed.create-default-admin:false}")

    private boolean createDefaultAdmin;    private boolean createDefaultAdmin;



    @Bean    @Bean

    public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {    public CommandLineRunner createDefaultAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {

        return args -> {        return args -> {

            if (!createDefaultAdmin) {            if (!createDefaultAdmin) {

                return; // Do not create a default admin unless explicitly enabled                return; // Do not create a default admin unless explicitly enabled

            }            }



            String adminUsername = "admin";            String adminUsername = "admin";

            if (userRepository.findByUsername(adminUsername).isEmpty()) {            if (userRepository.findByUsername(adminUsername).isEmpty()) {

                User admin = new User();                User admin = new User();

                admin.setUsername(adminUsername);                admin.setUsername(adminUsername);

                admin.setName("Administrador");                admin.setName("Administrador");

                admin.setEmail("admin@example.com");                admin.setEmail("admin@example.com");

                admin.setPassword(passwordEncoder.encode("admin123"));                admin.setPassword(passwordEncoder.encode("admin123"));

                admin.setRole("ADMIN");                admin.setRole("ADMIN");

                userRepository.save(admin);                userRepository.save(admin);

                System.out.println("Default admin user created: admin / admin123");                System.out.println("Default admin user created: admin / admin123");

            }            }

        };        };

    }    }

}}
