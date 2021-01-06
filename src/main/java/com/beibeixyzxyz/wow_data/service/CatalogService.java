package com.beibeixyzxyz.wow_data.service;

import com.beibeixyzxyz.wow_data.entity.Catalog;


public interface CatalogService {
    public Catalog getOneById(Integer id);

    public Catalog insertOne(Catalog catalog);
}
