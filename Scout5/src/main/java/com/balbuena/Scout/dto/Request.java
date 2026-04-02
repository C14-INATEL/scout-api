package com.balbuena.Scout.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class Request {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class PresidentCreate {
        @NotBlank(message = "Nome e obrigatorio")
        @Size(min = 2, max = 50, message = "Nome deve ter entre 2 e 50 caracteres")
        public String name;

        @NotBlank(message = "Email e obrigatorio")
        @Email(message = "Email invalido")
        public String email;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class PlaceBid {
        @NotNull(message = "presidentId e obrigatorio")
        public Long presidentId;

        @NotNull(message = "bidAmount e obrigatorio")
        @DecimalMin(value = "0.1", message = "Lance minimo e R$ 0.10")
        public Double bidAmount;
    }

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class Transfer {
        @NotNull(message = "presidentId e obrigatorio")
        public Long presidentId;

        @NotNull(message = "playerOutId e obrigatorio")
        public Long playerOutId;

        @NotNull(message = "playerInId e obrigatorio")
        public Long playerInId;

        // Apenas para negociacao entre presidents
        public Long targetPresidentId;
        public Double offerAmount;
    }
}