package com.lmartino.bank.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
public class Id {
    private String value;

    public Id(final String id) {
        this.value = id;
    }

    public static Id of(String id){
        return new Id(id);
    }

    public static Id create(){
        String id = UUID.randomUUID().toString();
        return new Id(id);
    }

}
