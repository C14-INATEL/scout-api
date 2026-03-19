package com.balbuena.Scout.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI scoutOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("⚽ Scout API - Fantasy Game")
                        .description("""
                                ## API REST - Fantasy Game estilo cartola fc
                                
                                ### Fluxo do jogo:
                                1. **REGISTRATION** → Cadastrar presidentes
                                2. **DRAFT_AUCTION** → Leilao: Hulk, Depay, Arrascaeta, Vitor Roque, Neymar
                                3. **DRAFT_LOTTERY** → Sorteio automatico dos demais jogadores
                                4. **CHAMPIONSHIP** → Simulacao das rodadas
                                5. **TRANSFER_WINDOW** → Janela apos rodada 3
                                6. **FINISHED** → Relatorio final
                                
                                ### Regras:
                                - Cada presidente comeca com **R$ 100,00**
                                - Time com **5 jogadores** (obrigatorio 1 goleiro)
                                - Maximo **1 transferencia** por janela
                                """)
                        .version("1.0.0")
                        .contact(new Contact().name("com.balbuena")))
                .tags(List.of(
                        new Tag().name("1. Gerenciamento do Jogo"),
                        new Tag().name("2. Presidentes"),
                        new Tag().name("3. Jogadores"),
                        new Tag().name("4. Draft - Leilao"),
                        new Tag().name("5. Draft - Sorteio"),
                        new Tag().name("6. Campeonato"),
                        new Tag().name("7. Transferencias"),
                        new Tag().name("8. Relatorios")
                ));
    }
}