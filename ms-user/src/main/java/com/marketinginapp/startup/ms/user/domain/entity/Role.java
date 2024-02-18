package com.marketinginapp.startup.ms.user.domain.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "role")
public class Role {
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
}
