package cjava.walker.common.repository;

import javax.inject.Inject;

import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cjava.walker.annotation.DeadlockRetry;
import cjava.walker.entity.Group;

@Repository("groupRepository")
public class GroupRepository implements BaseRepository<Group,Long>{

	private HibernateTemplate hibernateTemplate;

	//@Required
	@Inject
	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}
	
	@DeadlockRetry
	@Transactional(readOnly=true)
	public Group get(Long id){
		return hibernateTemplate.get(Group.class, id);
	}

	@Transactional(isolation=Isolation.READ_COMMITTED,propagation=Propagation.SUPPORTS)
	public void doSomeProcess(Group g){
		//do something
	}
}
