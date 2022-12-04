package org.example.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrganizationDTO implements Serializable {
    private final Long id;
    private final String name;

    public OrganizationDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public OrganizationDTO(String name) {
        this.id = null;
        this.name = name;
    }
}
