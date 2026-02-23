package com.example.demo.entity;

import com.example.demo.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import tools.jackson.core.ObjectReadContext;

import java.util.ArrayList;
import java.util.List;

//DB의 테이블 역할
@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity {
    @Id // pk 컬럼 지정. 필수
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(length = 20, nullable = false) //일반 컬럼 , 컬럼의 크기값 = length ,
    private String boardWriter;

    @Column //크기 255, null 가능 -> 기본옵션
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column
    private int fileAttached; //1 or 0

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    //컨트롤러에서 받는 DTO를 DB 저장 가능한 Entity로 바꿔줌
    public  static  BoardEntity toSaveEntity(BoardDTO boardDTO) {
       BoardEntity boardEntity = new BoardEntity();
       boardEntity.setBoardWriter(boardDTO.getBoardWriter());
       boardEntity.setBoardPass(boardDTO.getBoardPass());
       boardEntity.setBoardTitle(boardDTO.getBoardTitle());
       boardEntity.setBoardContents(boardDTO.getBoardContents());
       boardEntity.setBoardHits(0); // 수치값은 기본적으로 0이니까
        boardEntity.setFileAttached(0); // 파일 없음
        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId()); //set 속성에 값을 넣는 함수
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardEntity.getBoardHits());
        return boardEntity;
    }

    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO){
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0); // 수치값은 기본적으로 0이니까
        boardEntity.setFileAttached(1); // 파일 있음
        return boardEntity;
    }
}
