package com.asa.processminer.repository;

import com.asa.processminer.model.PMProject;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PMProjectRepository extends PagingAndSortingRepository<PMProject, Long> {

    public List<PMProject> findAll();
}
