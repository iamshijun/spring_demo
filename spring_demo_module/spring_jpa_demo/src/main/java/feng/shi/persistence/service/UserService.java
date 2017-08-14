package feng.shi.persistence.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import feng.shi.persistence.model.User;

@Service
@Transactional
public class UserService {

	@PersistenceContext
	private EntityManager entityManager;
	
	public void addUser(User user){
		entityManager.persist(user);
	}
}
