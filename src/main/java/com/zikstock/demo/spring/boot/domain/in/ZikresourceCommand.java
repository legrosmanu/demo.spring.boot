package com.zikstock.demo.spring.boot.domain.in;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;

public interface ZikresourceCommand {

    void create(Zikresource zikresource);

    void update(Zikresource zikresource);

    void delete(ZikresourceIdentifier zikresourceIdentifier);

}
