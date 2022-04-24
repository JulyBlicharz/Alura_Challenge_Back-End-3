package com.alura.challenge3.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@Document("user")
public class User {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;
}
