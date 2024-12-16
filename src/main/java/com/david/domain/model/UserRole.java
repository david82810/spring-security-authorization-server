package com.david.domain.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class UserRole {
    private Long id;
    private Long userId;
    private Long roleId;
}
