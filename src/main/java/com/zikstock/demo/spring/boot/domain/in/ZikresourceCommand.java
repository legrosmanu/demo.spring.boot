package com.zikstock.demo.spring.boot.domain.in;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;

public interface ZikresourceCommand {

    void create(Zikresource zikresource);

    void update(Zikresource zikresource);

    void delete(Zikresource zikresource);

}
