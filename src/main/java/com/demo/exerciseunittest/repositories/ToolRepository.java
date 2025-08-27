package com.demo.exerciseunittest.repositories;

import com.demo.exerciseunittest.entities.Tool;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ToolRepository extends JpaRepository<Tool, Long> {

}
