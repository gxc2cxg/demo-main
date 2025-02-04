package com.xuchen.demo.model.work.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WorkStatus {

    NORMAL(0),
    PERSISTENCE(1),
    ENQUEUE(2),
    EXECUTING(3),
    SUCCESS(4),
    FAILURE(5),
    CANCEL(6),
    ;

    private final Integer status;
}
