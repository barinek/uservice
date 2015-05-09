package com.barinek.uservice;

import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class ResourceDAOTest {
    @Test
    public void testSelect() throws Exception {
        ResourceDAO dao = new ResourceDAO(TestDataSource.getDataSource());
        dao.deleteAll();

        String identifier = UUID.randomUUID().toString();
        dao.create(new Resource(identifier));

        List<Resource> results = dao.list();
        assertEquals(1, results.size());
        assertEquals(identifier, results.get(0).getIdentifier());
    }
}