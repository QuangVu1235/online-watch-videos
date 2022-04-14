package com.poly.dao;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;

import com.poly.util.JpaUtil;

public class AbstractDao<T> implements AutoCloseable {
	public static final EntityManager entityManager = JpaUtil.getEntityManager();

	@Override
	public void close() throws Exception {
		entityManager.close();
	}

	public T findById(Class<T> clazz, Integer id) {
		return entityManager.find(clazz, id);
	}

	public T findById(Class<T> clazz, String id) {
		return entityManager.find(clazz, id);
	}

	public List<T> findAll(Class<T> clazz, boolean existActive) {
		String entityName = clazz.getSimpleName();
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT o FROM ").append(entityName).append(" o");
		if (existActive == true) {
			jpql.append(" WHERE o.active = 1");
		}
		TypedQuery<T> query = entityManager.createQuery(jpql.toString(), clazz);
		return query.getResultList();
	}

	public List<T> findAll(Class<T> clazz, boolean existActive, int pageNumber, int pageSize) {
		String entityName = clazz.getSimpleName();
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT o FROM ").append(entityName).append(" o");
		if (existActive == true) {
			jpql.append(" WHERE o.active = 1");
		}
		TypedQuery<T> query = entityManager.createQuery(jpql.toString(), clazz);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public List<T> findAllSorted(Class<T> clazz, boolean existActive, String attrSorted, String sortedType) {
		String entityName = clazz.getSimpleName();
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT o FROM ").append(entityName).append(" o");
		if (existActive == true) {
			jpql.append(" WHERE o.active = 1");
		}
		jpql.append(" ORDER BY o.").append(attrSorted).append(" ").append(sortedType);
		TypedQuery<T> query = entityManager.createQuery(jpql.toString(), clazz);
		return query.getResultList();
	}

	public List<T> findAllSorted(Class<T> clazz, boolean existActive, String attrSorted, String sortedType,
			int pageNumber, int pageSize) {
		String entityName = clazz.getSimpleName();
		StringBuilder jpql = new StringBuilder();
		jpql.append("SELECT o FROM ").append(entityName).append(" o");
		if (existActive == true) {
			jpql.append(" WHERE o.active = 1");
		}
		jpql.append(" ORDER BY o.").append(attrSorted).append(" ").append(sortedType);
		TypedQuery<T> query = entityManager.createQuery(jpql.toString(), clazz);
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	public T findOne(Class<T> clazz, String jpql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		List<T> result = query.getResultList();
		if (result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}

	public List<T> findMany(Class<T> clazz, String jpql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}
	
	public List<T> findManyWithLike(Class<T> clazz, int pageNumber, int pageSize, String jpql, Object... params) {
		TypedQuery<T> query = entityManager.createQuery(jpql, clazz);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i, "%" + params[i] + "%");
		}
		query.setFirstResult((pageNumber - 1) * pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Object[]> findManyByNativeQuery(String sql, Object... params) {
		Query query = entityManager.createNativeQuery(sql);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}
	
	public T create(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			System.out.println("Create success!");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Cannot insert entity: " + entity.getClass().getSimpleName() + " to DB");
			throw new RuntimeException(e);
		}
	}

	public T update(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
			System.out.println("Update success!");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			System.out.println("Cannot update entity: " + entity.getClass().getSimpleName());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public T delete(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
			System.out.println("Delete success!");
			return entity;
		} catch (Exception e) {
			entityManager.getTransaction().rollback();
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> callStored(String namedStored, Map<String, Object> params) {
		StoredProcedureQuery query = entityManager.createNamedStoredProcedureQuery(namedStored);
		params.forEach((key, value) -> query.setParameter(key, value));
		return (List<T>) query.getResultList();
	}
}
