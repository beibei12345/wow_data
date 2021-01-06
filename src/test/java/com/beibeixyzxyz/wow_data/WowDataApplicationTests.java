package com.beibeixyzxyz.wow_data;

import com.beibeixyzxyz.wow_data.dao.CatalogDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WowDataApplicationTests {

	@Autowired
	private CatalogDao catalog;

	@Test
	void contextLoads() {
	}

	@Test
	void test1(){
		System.out.println(catalog);
	}

}
