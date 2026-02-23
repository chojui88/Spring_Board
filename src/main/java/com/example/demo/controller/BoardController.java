package com.example.demo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import com.example.demo.dto.BoardDTO;
import com.example.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;

    @GetMapping("/save")
    public  String saveForm(){return "save";}

    @PostMapping("/save")
    public  String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";

    }
    @GetMapping("/")
    public String findAll(Model model) {
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";

    }

    // 게시글 상세조회
    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault(page=1) Pageable pageable) {

        //reposityry db와 직접 연결해 데이터 가져오거나 수정하는 객체 , 대화하는 창구
    /* 해당 게시글의 조회수 하나 올리고 (조회수 증가)
    게시글 데이터 가져와서 detail.html에 출력
     */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        /*  boardService 는 서비스 객체
        컨트롤러와 Reposityr 사이에서 중간 역할
        업무 로직 처리한후 db 접근
        DTO = db에서 가져온 데이터 전달하기 위한 그릇
        게시글 정보 담는 상자
         */
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList",commentDTOList);

        model.addAttribute("board", boardDTO);
        model.addAttribute("page", pageable.getPageNumber());
        /* model은 뷰로 데이터 전달하는 상자 , 컨트롤러가 html보여주고싶은 데이터 담음
        뷰는 HTML 화면 , boardDTO 객체를 모델에 ㅠoard라는 이름으로 담는다
        $는 탬플릿 엔진 (모델에서 값을 꺼내쓰겠다)
        .title은 멤버 변수
         */
        return "detail";
        /* 컨트롤러 메서드 반환값은, 뷰 이름을 의미
        /detail.html 파일을 찾아서 렌더링
         */

    }
        //PathVariable = 경로에 포함된 값을 변수로 가져온다
    //객체 = 데이터 + 기능(메서드)

    @GetMapping("/update/{id}")
    public  String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate",boardDTO); // 이름 = 화면 html 에서 사용할 이름 프 , 값에다 넣음
        return "update";
    }

    @PostMapping("/update") //http가 post 요청을 보냈을때 처리하는것
    public String update(@ModelAttribute BoardDTO boardDTO, Model model ){
       BoardDTO board =  boardService.update(boardDTO);
       model.addAttribute("board",board);
       return "detail";
       // return "redirect:/board/" + boardDTO.getID(); -> 조회수 올라가서 추천 안함
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/";
    }

    // 파라미터에서 page로 지정된 값을 받아준다
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3; // 보여지는 페이지 번호 개수
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList",boardList);
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        return "paging";
    }

}
