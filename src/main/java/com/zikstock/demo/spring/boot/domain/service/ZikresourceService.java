package com.zikstock.demo.spring.boot.domain.service;

import com.zikstock.demo.spring.boot.domain.in.ZikresourceCommand;
import com.zikstock.demo.spring.boot.domain.in.ZikresourceQuery;
import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;
import com.zikstock.demo.spring.boot.domain.out.ZikresourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZikresourceService implements ZikresourceCommand, ZikresourceQuery {

    private final ZikresourceRepository zikresourceRepository;

    public ZikresourceService(ZikresourceRepository zikresourceRepository) {
        this.zikresourceRepository = zikresourceRepository;
    }

    @Override
    public void create(Zikresource zikresource) {
        zikresourceRepository.save(zikresource);
    }

    @Override
    public void update(Zikresource zikresource) {
        zikresourceRepository.save(zikresource);
    }

    @Override
    public void delete(Zikresource zikresource) {
        zikresourceRepository.delete(new ZikresourceIdentifier(zikresource.id()));
    }

    @Override
    public List<Zikresource> getZikresources() {
        return zikresourceRepository.findAll();
    }

    @Override
    public Zikresource getZikresource(ZikresourceIdentifier id) {
        return zikresourceRepository.findById(id).orElse(null);
    }
}
