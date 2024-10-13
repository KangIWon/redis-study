package com.sparta.redis.controller;

import com.sparta.redis.dto.MemoRequsetDto;
import com.sparta.redis.dto.MemoResponseDto;
import com.sparta.redis.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/memos")
public class MemoController {

    private final MemoService memoService;
    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate 주입받아 사용

    @PostMapping
    public ResponseEntity<MemoResponseDto> createMemo(@RequestBody MemoRequsetDto memoRequsetDto) {
        MemoResponseDto memoResponseDto = memoService.createMemo(memoRequsetDto);
        return ResponseEntity.status(HttpStatus.OK).body(memoResponseDto);
    }

    @GetMapping("/redis")
    public MemoResponseDto getRedisMemoResponseData(@RequestParam(name = "keyword") String keyword) {
        var key = "memo:" + keyword; // Redis에서 사용할 키 값 생성 (ex: "memo:메모 제목").
        MemoResponseDto response = (MemoResponseDto) redisTemplate.opsForValue().get(key); // Redis에서 키로 메모 데이터 조회.
        if (Objects.isNull(response)) { // Redis에 데이터가 없는 경우.
            response = memoService.getMemoByTitle(keyword); // DB에서 해당 제목으로 메모 조회.
            if (response != null) {
                redisTemplate.opsForValue().set(key, response); // Redis에 조회된 메모 데이터 캐시.
            }
        }
        return response; // 조회된 메모 데이터를 반환.
    }

    @GetMapping
    public ResponseEntity<List<MemoResponseDto>> getMemos() {
        List<MemoResponseDto> memoResponseDtoList = memoService.getMemo();
        return ResponseEntity.status(HttpStatus.OK).body(memoResponseDtoList);
    }

    @PatchMapping("/{memoId}")
    public ResponseEntity<MemoResponseDto> patchMemo(@PathVariable("memoId") Long memoId, @RequestBody MemoRequsetDto memoRequsetDto) {
        MemoResponseDto memoResponseDto = memoService.patchMemo(memoId, memoRequsetDto);
        return ResponseEntity.status(HttpStatus.OK).body(memoResponseDto);
    }

    @DeleteMapping("/{memoId}")
    public void deleteMemo(@PathVariable("memoId") Long memoId) {
        memoService.deleteMemo(memoId);
    }
}