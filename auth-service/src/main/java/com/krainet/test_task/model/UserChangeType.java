package com.krainet.test_task.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserChangeType {
    CREATED("Создан"),
    UPDATED("Изменен"),
    DELETED("Удален");

    String title;
}
