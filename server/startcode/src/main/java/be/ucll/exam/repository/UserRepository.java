package be.ucll.exam.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be.ucll.exam.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}