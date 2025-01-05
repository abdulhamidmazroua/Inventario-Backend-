package com.hameed.inventario.model.dto.basic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class UserDTO {
    private String username;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;
    private Set<String> roles;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private String createdBy;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private String lastUpdateBy;

//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    private LocalDateTime lastUpdateDate;
}
