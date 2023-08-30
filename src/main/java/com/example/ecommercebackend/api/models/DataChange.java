package com.example.ecommercebackend.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mahyar Maleki
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataChange<T> {
    private T data;

    private ChangeType changeType;

    public enum ChangeType {
        INSERT,
        UPDATE,
        DELETE
    }
}
