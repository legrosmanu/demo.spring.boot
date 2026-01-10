package com.zikstock.demo.spring.boot.infrastructure.out.persistence;

import com.zikstock.demo.spring.boot.domain.model.Zikresource;
import com.zikstock.demo.spring.boot.domain.model.ZikresourceIdentifier;
import com.zikstock.demo.spring.boot.domain.out.ZikresourceRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryZikresourceRepository implements ZikresourceRepository {

    private final Map<String, Zikresource> store = new ConcurrentHashMap<>();

    @Override
    public void save(Zikresource zikresource) {
        store.put(zikresource.id().toString(), zikresource);
    }

    @Override
    public List<Zikresource> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Zikresource> findById(ZikresourceIdentifier id) {
        return Optional.ofNullable(store.get(id.value().toString()));
    }

    @Override
    public void delete(ZikresourceIdentifier id) {
        store.remove(id.value().toString());
    }
}
