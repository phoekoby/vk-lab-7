package org.example.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ProductDTO implements Serializable {

    private  Long id;
    @JsonProperty("name")
    private  String name;
    @JsonProperty("organization")
    private  String organizationName;
    @JsonProperty("amount")
    private  int amount;

    public ProductDTO(Long id, String name, String organizationName, int amount) {
        this.id = id;
        this.name = name;
        this.organizationName = organizationName;
        this.amount = amount;
    }

    public ProductDTO(){}
    public ProductDTO(String name, String organizationName, int amount) {
        this.id=null;
        this.name = name;
        this.organizationName = organizationName;
        this.amount = amount;
    }
}
