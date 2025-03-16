package ru.myblog.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.myblog.model.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAllByTags(String tag, Pageable pageable);
}
