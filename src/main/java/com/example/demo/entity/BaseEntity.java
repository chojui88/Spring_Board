package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.sound.sampled.AudioFileFormat;
import java.time.LocalDateTime;

//시간정보를 다루는 클래스
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    @CreationTimestamp //생성됐을떄의 시간을 만들어줌
    @Column(updatable = false) //수정시에는 관여를 안한다
    private LocalDateTime createdTime;

    @UpdateTimestamp //업데이트 발생했을때 시간정보
    @Column(insertable = false) //입력시에는 관여 안한다
    private  LocalDateTime updatedTime;
}
