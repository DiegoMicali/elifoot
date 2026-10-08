package dev.java10x.elifoot.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateStadiumRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String city;
    private Integer capacity;
    private String urlImg;
}
