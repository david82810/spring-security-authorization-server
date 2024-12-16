package com.david.adapter.controller.response.oauth2.line;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LineProfileResponse {
    private String userId;
    private String displayName;
}
