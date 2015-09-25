package cjava.walker.common.repository;

import java.io.Serializable;


public interface BaseRepository<T,PK extends Serializable> {
	public  T get(PK id);
	
}
