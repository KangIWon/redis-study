package com.sparta.redis.service;

import com.sparta.redis.dto.MemoRequsetDto;
import com.sparta.redis.dto.MemoResponseDto;
import com.sparta.redis.entity.Memo;
import com.sparta.redis.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoService {

    private final MemoRepository memoRepository;
    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate 주입받아 사용.

    @Transactional
    public MemoResponseDto createMemo(MemoRequsetDto memoRequsetDto) {
        Memo memo = new Memo(memoRequsetDto);
        MemoResponseDto savedMemo = new MemoResponseDto(memoRepository.save(memo));
        // Redis에 새로 생성된 메모를 캐싱. 키는 메모 제목을 기준으로 설정.
        redisTemplate.opsForValue().set("memo:" + savedMemo.getTitle(), savedMemo);

        return savedMemo;
    }

    public List<MemoResponseDto> getMemo() {
        return memoRepository.findAll().stream()
                .map(MemoResponseDto::new)
                .toList();
    }

    @Transactional
    public MemoResponseDto patchMemo(Long memoId, MemoRequsetDto memoRequsetDto) {
        Memo findMemo = memoRepository.findById(memoId).orElseThrow();
        MemoResponseDto updatedMemo = new MemoResponseDto(findMemo.patchMemo(memoRequsetDto));
        // Redis에 수정된 메모를 업데이트. 키는 메모 제목을 기준으로 설정.
        redisTemplate.opsForValue().set("memo:" + updatedMemo.getTitle(), updatedMemo);

        return updatedMemo;
    }

    @Transactional
    public void deleteMemo(Long memoId) {
        Memo memo = memoRepository.findById(memoId).orElseThrow();
        memoRepository.delete(memo);
        // 메모 삭제 후 Redis에서 제거.
        redisTemplate.delete("memo:" + memo.getTitle());
    }

    public MemoResponseDto getMemoByTitle(String title) { // 제목을 기준으로 메모를 조회.
        Memo memo = memoRepository.findByTitle(title).orElse(null);
        return memo != null ? new MemoResponseDto(memo) : null;
    }
}
