package com.ecomfsrwebflux.client_service.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Role {
    @Id
    private String id;
    private String name;
}
