package com.collabera.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "MD. SAMIUL ARAFIN",
                        email = "sami.cse.1112@gmail.com",
                        url = "https://github.com/arafinsami"
                ),
                description = "OpenApi documentation for Spring Security",
                title = "OpenApi specification - Sami",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local environment",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApi {
}
