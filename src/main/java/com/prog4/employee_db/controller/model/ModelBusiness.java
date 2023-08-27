package com.prog4.employee_db.controller.model;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ModelBusiness {
    private Long id;
    private String name;
    private String slogan;
    private String address;
    private String email;
    private String phone;
    private MultipartFile logo;
    public MultipartFile getLogo() {
        return logo;
    }
    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }
    private String nif;
    private String stat;
    private String rcs;
}
