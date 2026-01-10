package com.zikstock.demo.spring.boot.domain.in;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;

import java.util.List;

public interface ZikresourceQuery {

    List<Zikresource> getZikresources();

    Zikresource getZikresource(ZikresourceIdentifier id);

}
