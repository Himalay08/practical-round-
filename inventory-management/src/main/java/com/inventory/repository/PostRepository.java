package com.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entity.Post;



public interface PostRepository extends JpaRepository<Post,Integer> {

}
