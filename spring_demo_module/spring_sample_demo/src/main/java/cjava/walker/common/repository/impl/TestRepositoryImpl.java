package cjava.walker.common.repository.impl;

import org.springframework.stereotype.Repository;

import cjava.walker.common.repository.TestRepository;

@Repository
public class TestRepositoryImpl implements TestRepository{

	public void getEntityById(long id){
		System.out.println("TestRepository.getEntityById()");
	};
}
