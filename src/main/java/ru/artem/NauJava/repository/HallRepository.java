package ru.artem.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.artem.NauJava.entity.Hall;

@RepositoryRestResource(path = "halls")
public interface HallRepository  extends CrudRepository<Hall, Long> {

}
