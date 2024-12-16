package com.david.domain.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@ToString
public class Role {
    private Long id;
    private String name;
}
