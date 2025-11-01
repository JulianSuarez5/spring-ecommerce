package ppi.e_commerce.Config;package ppi.e_commerce.Config;



import org.springframework.context.annotation.Configuration;import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.beans.factory.annotation.Value;import org.springframework.beans.factory.annotation.Value;

import org.springframework.lang.NonNull;import org.springframework.lang.NonNull;

import java.nio.file.Paths;import java.nio.file.Paths;



/**/**

 * Configuración de MVC para servir recursos estáticos (imágenes subidas). * Configuración de MVC para servir recursos estáticos (imágenes subidas).

 * Esto es CRUCIAL para que las imágenes de productos se muestren. * Esto es CRUCIAL para que las imágenes de productos se muestren.

 */ */

@Configuration@Configuration

public class MvcConfig implements WebMvcConfigurer {public class MvcConfig implements WebMvcConfigurer {



    // Lee la ruta del directorio de subida desde application.properties    // Lee la ruta del directorio de subida desde application.properties

    // Si no se define, usa "./uploads/" (una carpeta 'uploads' junto al JAR)    // Si no se define, usa "./uploads/" (una carpeta 'uploads' junto al JAR)

    @Value("${app.upload-dir:./uploads/}")    @Value("${app.upload-dir:./uploads/}")

    private String uploadDir;    private String uploadDir;

        @Override

    @Override    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {

    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {        // Resuelve la ruta absoluta al directorio de subidas

        // Resuelve la ruta absoluta al directorio de subidas        // (ej. "file:/home/user/my-app/uploads/")

        // (ej. "file:/home/user/my-app/uploads/")        String resolvedUploadPath = Paths.get(uploadDir).toAbsolutePath().toUri().toString();

        String resolvedUploadPath = Paths.get(uploadDir).toAbsolutePath().toUri().toString();        

                // Mapea la URL /uploads/** a la carpeta física de subidas

        // Mapea la URL /uploads/** a la carpeta física de subidas        // Ahora, una imagen en 'uploads/products/img.jpg' será accesible en

        // Ahora, una imagen en 'uploads/products/img.jpg' será accesible en        // http://localhost:8081/uploads/products/img.jpg

        // http://localhost:8081/uploads/products/img.jpg        registry.addResourceHandler("/uploads/**")

        registry.addResourceHandler("/uploads/**")                .addResourceLocations(resolvedUploadPath);

                .addResourceLocations(resolvedUploadPath);    }

    }}

}