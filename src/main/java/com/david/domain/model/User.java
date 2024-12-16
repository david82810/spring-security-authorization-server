package com.david.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class User {
    private  Long id;
    private String username;
    private String password;
    private String email;
}
