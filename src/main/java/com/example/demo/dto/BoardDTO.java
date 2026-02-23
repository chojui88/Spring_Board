package com.example.demo.dto;

import com.example.demo.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

// DTO = 데이터를 전송할 떄 사용하는 객체, Bean 이렇게도 쓰였음
// 롬복을 쓰지않으면 getter setter 만들때 불편하고
// 필드만 고치면 된다
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO { //필드
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits; // 조쇠수
    private LocalDateTime boardCreatedTime; // 글 작성시간
    private LocalDateTime boardUpdatedTime; // 글 수정시간

    private List<MultipartFile> boardFile; // save.html-> controller 로 넘어갈때 파일을 받음
    private String originalFileName; // 원본파일 이름
    private String storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; //파일 첨부 여부(첨부1, 미첨부0)

    public BoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardDTO.getBoardHits());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        return boardDTO;
    }
}
