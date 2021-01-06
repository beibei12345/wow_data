package com.beibeixyzxyz.wow_data.service.impl;

import com.beibeixyzxyz.wow_data.dao.CatalogDao;
import com.beibeixyzxyz.wow_data.entity.Catalog;
import com.beibeixyzxyz.wow_data.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CatalogServiceImpl implements CatalogService{
    @Autowired
    private CatalogDao catalogDao;


    @Override
    public Catalog getOneById(Integer id) {
        return catalogDao.getOne(id);
    }

    @Override
    public Catalog insertOne(Catalog catalog) {
        Date curTime = new Date();
        catalog.setCreateTime(curTime);
        catalog.setUpdateTime(curTime);
        return catalogDao.save(catalog);
    }
}
