package com.auth.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.constant.RoleName;
import com.auth.entity.Role;

@Repository
public interface RoleDAO extends JpaRepository<Role	, Long> {

	Optional<Role> findByName(RoleName roleName);
}
