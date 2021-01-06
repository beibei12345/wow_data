package com.beibeixyzxyz.wow_data.controller;

import com.beibeixyzxyz.wow_data.entity.Catalog;
import com.beibeixyzxyz.wow_data.service.CatalogService;
import com.beibeixyzxyz.wow_data.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

    @RequestMapping("/getOneCatalogById/{id}")
    public String getOneCatalogById(@PathVariable("id") Integer id){
        System.out.println(catalogService.getOneById(id));
        return JsonUtil.objectToJson(catalogService.getOneById(id));
    }

    @PostMapping("/insertOne")
    public Catalog insertOne(Catalog catalog){
        return catalogService.insertOne(catalog);
    }
}