package com.david.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class OAuth2User {
    private String id;
    private String name;
    private String provider;
    private Long userId;
}
