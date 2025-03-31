package com.github.cristianrb.smartnews.repository;

import com.github.cristianrb.smartnews.entity.UserDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<UserDAO, String> {
}
