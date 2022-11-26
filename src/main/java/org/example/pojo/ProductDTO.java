package org.example.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDTO implements Serializable {
    private final Long id;
    private final String name;
    private final String organizationName;
    private final int amount;
}
