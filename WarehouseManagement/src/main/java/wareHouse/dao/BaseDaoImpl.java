package wareHouse.dao;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
@Repository
@Transactional(rollbackFor = Exception.class)
public class BaseDaoImpl<E> implements BaseDAO<E> {

	final static Logger logg = Logger.getLogger(BaseDaoImpl.class);
	@Autowired
	SessionFactory sessionFactory;
	/**
	 * Find all data from database
	 * 
	 * */
	@Override
	public List<E> findAll() {
		logg.info("Find all record from database");
		StringBuilder queryString = new StringBuilder("");
		queryString.append(" from ").append(getGenericName()).append(" as model wher model.activeFlag=1");
		logg.info("Query Find All ===> "+ queryString.toString());
		return sessionFactory.getCurrentSession().createQuery(queryString.toString()).list();
	}

	/**
	 * Find data via ID
	 * */
	@Override
	public E findById(Class<E> e, Serializable id) {
		logg.info("Find by ID");
		return sessionFactory.getCurrentSession().get(e, id);
	}

	/**
	 * Find data via properti such as: code, name, ...
	 * */
	@Override
	public List<E> findByProperty(String property, Object value) {
		logg.info("Find by Properties");
		StringBuilder queryString = new StringBuilder("");
		queryString.append(" from ").append(getGenericName()).append(" as model where model.actiFlag=1 and model.").append(property).append("=?");
		logg.info("Query Find by Properties ===> "+ queryString.toString());
		Query<E> query = sessionFactory.getCurrentSession().createQuery(queryString.toString());
		query.setParameter(0, value);
		return query.getResultList();
	}

	@Override
	public void save(E instance) {
		logg.info(" save instance");
		sessionFactory.getCurrentSession().persist(instance);
		
	}

	@Override
	public void update(E instance) {
		logg.info(" update instance");
		sessionFactory.getCurrentSession().merge(instance);		
	}
	
	/**
	 *
	 * 
	 * */
	public String getGenericName() {
		String s = getClass().getGenericSuperclass().toString();
		Pattern pattern = Pattern.compile("\\<(>*?)//>");
		Matcher m = pattern.matcher(s);
		String generic = "null";
		if(m.find()) {
			generic = m.group(1);			
		}
		return generic;
	}

}
