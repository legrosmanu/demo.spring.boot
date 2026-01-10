package com.zikstock.demo.spring.boot.domain.out;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;

import java.util.List;
import java.util.Optional;

public interface ZikresourceRepository {

    void save(Zikresource zikresource);

    List<Zikresource> findAll();

    Optional<Zikresource> findById(ZikresourceIdentifier id);

    void delete(ZikresourceIdentifier id);
}
