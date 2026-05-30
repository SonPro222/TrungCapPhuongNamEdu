package org.example.trungcapphuongnam.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class PublicFileConfig implements WebMvcConfigurer {

    @Value("${app.upload.root:uploads/tep-dinh-kem}")
    private String uploadRoot;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path rootPath = Paths.get(uploadRoot).toAbsolutePath().normalize();
        registry.addResourceHandler("/public/tep-dinh-kem/**")
                .addResourceLocations(rootPath.toUri().toString());
    }
}
