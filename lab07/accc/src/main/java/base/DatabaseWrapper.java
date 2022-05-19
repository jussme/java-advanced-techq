package base;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import hibernate.HibernateUtil;

public class DatabaseWrapper {
	public static <T> List<T> getEntities(Class<T> entityClass) {
		var session = HibernateUtil.getSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<T> criteria = builder.createQuery(entityClass);
		criteria.from(entityClass);
		var temp = session.createQuery(criteria).getResultList();
		return session.createQuery(criteria).getResultList();
	}
	
	public static <T> T add(T entity) {
		var session = HibernateUtil.getSession();
		session.beginTransaction();
		session.persist(entity);
		session.getTransaction().commit();
		session.close();
		return entity;
	}
	
	public static <T> T getEntity(Class<T> entityClass, Long id) {
		var session = HibernateUtil.getSession();
		return session.get(entityClass, id);
	}
}
