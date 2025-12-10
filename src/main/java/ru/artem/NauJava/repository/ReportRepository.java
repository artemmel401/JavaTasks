package ru.artem.NauJava.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.artem.NauJava.entity.Report;

@RepositoryRestResource(exported = false)
public interface ReportRepository extends JpaRepository<Report, Long> {
}
