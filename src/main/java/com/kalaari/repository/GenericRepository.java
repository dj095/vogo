package com.kalaari.repository;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class GenericRepository {

    @PersistenceContext
    private EntityManager em;

    public <T> List<T> runQuery(String query, Class<?> tClass) {
        return em.createNativeQuery(query, tClass).getResultList();
    }

    public <T> List<T> runQuery(String query) {
        return em.createNativeQuery(query).getResultList();
    }

    public <T> List<T> runQuery(String queryStr, Map<String, Object> params) {
        Query query = em.createNativeQuery(queryStr);
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.getResultList();
    }
}
