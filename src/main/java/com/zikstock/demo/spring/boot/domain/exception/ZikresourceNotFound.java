package com.zikstock.demo.spring.boot.domain.exception;

import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;

public class ZikresourceNotFound extends RuntimeException {

    public ZikresourceNotFound(ZikresourceIdentifier id) {
        super("Zikresource with id " + id.value() + " not found");
    }
}
