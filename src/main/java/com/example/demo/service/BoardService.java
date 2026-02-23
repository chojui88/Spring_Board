package com.example.demo.service;

import com.example.demo.dto.BoardDTO;
import com.example.demo.entity.BoardEntity;
import com.example.demo.entity.BoardFileEntity;
import com.example.demo.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//DTO -> Entity 컨트롤 러는 dto로 넘겨받는데
//Entity -> DTO , entity는 db와 직접적 연관이 있어서 노출 많이시키지 마아라
@Service
@RequiredArgsConstructor
public class BoardService {

    private  final BoardRepository boardRepository;
    private  final BoardFileEntity boardFileRepository;
    //파일 첨부 여부에 따라 로직 분리
    if (boardDTO.getBoardFile().isEmpty()){
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
    }else {
    // 파일 있으면
       BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);

        MultipartFile boardFile = boardDTO.getBoardFile();
        String originalFilename = boardFile.getOriginalFilename(); // 변수에 담는다 -> 서버 저장용 이름 만들기
        String  storedFileName = System.currentTimeMillis() + "_" + originalFilename; //겹치면 안되니까 난수 설정
        String savePath ="/Users/jui/springboot_img/" + storedFileName; //해당 경로가 반드시 있어야 한다. c 드라이브 등
        boardFile.transferTo(new File(savePath));

        //dto 를 entity로 전환해서 board 테이블에 저장하고
        BoardEntity.toSaveFileEntity(boardDTO);

        BoardEntity boardFileEntity = boardFileEntity.toBoardFIleEntity(boardDTO);
        Long saveID = boardRepository.save(boardEntity).getId(); //저장을 한다 부모 pk값 필요, 타입이 entity라 이를 넘겨줘야
        BoardEntity board = boardRepository.findById(saveID).get()
        //부모 엔티티를 가져왔고
       BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename,storedFileName);
        //정적 메서드 호출
        boardFileRepository.save(boardFileEntity);
    }



    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));

        }
        return boardDTOList;
    }

    @Transactional //새로 만드는 쿼리는 트랙젝션 어노테이션 붙여야함 (수동적인 경우) 영속성 처리
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
        /* db 연결해주는 창구, CRUD 담당,  entity와 연결됨
        */
    }

    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        //게시글 조회 id 일치하는 글 찾기 . 없을수도 있으니 optional
        if (optionalBoardEntity.isPresent()) {
            //만약 게시글이 있다면
            BoardEntity boardEntity = optionalBoardEntity.get();
            //optional에 들어있는 실제 객체 꺼내기
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            //board entity를 dto로 변환해서 뷰로 전달할 수 있도록
            return boardDTO;
            //DTO를 컨트롤러에 반환  (뷰 연결하기 위해)
        } else{
            return null;
        }
        /* 렌더링이란 데이터 html 합쳐서 실제 브라우저 화면에 만드는 과정
    새 시그니처 만들어줌 -> 현재 클래스 파일 안에 메서드 자리 생성
    jpa가 제공하는 조건은 메서드 이름 규칙을 따라가면 자동으로 쿼리 만들어줌
    조회수 증가같은 특수 목적 쿼리는 구분이 잘 되지 않음
     */
    }

    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity); //update와 insert 값 비교하는건 id값이 있냐없냐
        return findById(boardDTO.getId());
    }


    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1;
        int pageLimit = 3; // 한 페이지에 보여줄 글 개수
        //실제 사용자가 요청한 값에서 1 뺀 값을 가져와야해
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호 사용자가 1페이지 요청했다면 0 불러옴
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        //목록 :id, writer,title, hits, creeatedTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
