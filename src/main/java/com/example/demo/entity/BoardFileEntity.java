package com.example.demo.entity;

import aQute.bnd.annotation.xml.XMLAttribute;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //값을 자동으로 생성하는데, DB에 맞겨서 PK자동 증가시키는 전략
    private  Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    //게시글 - 파일 : 1:N
    @ManyToOne(fetch = FetchType.LAZY)
    //데이터를 언제 가져올지 전략 lazy = 필요할 떄 가져온다, 원래 다가져와서
    @JoinColumn(name = "board id")
    //테이블에 만들어지는 컬럼 이름
    private BoardEntity boardEntity;

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName){
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity); // pk가 아니라 부모 엔티티 넘겨주기
        return boardFileEntity
    }
}
