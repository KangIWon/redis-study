package com.sparta.redis.repository;

import com.sparta.redis.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    Optional<Memo> findByTitle(String title); // 메모 제목으로 조회
}
