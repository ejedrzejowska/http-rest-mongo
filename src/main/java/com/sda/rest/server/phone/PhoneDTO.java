package com.sda.rest.server.phone;

import lombok.*;

import java.time.LocalDate;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {
    private String id;
    private String name;
    private String brand;
    private Double ram;
    private Double weight;
    private Boolean touchscreen;
    private LocalDate releaseDate;
    private String cardStandard;
    private String operatingSystem;
    private String displayProtection;
}
