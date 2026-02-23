7. 파일(이미지) 첨부하기
    - 단일 파일 첨부
    - 다중 파일 첨부

    - board_table(부모) - board_file_table(자식)
    - board_id 컬럼 제약조건 -> references board_table(id) on delete cascade (부모데이터가 삭제되면 자식도 삭제된다 -> 게시글 삭제되면 같이 첨부된 파일도 삭제된다)

8. 댓글 처리하기
   - 글 상세 페이지에서 댓글 입력
     - ajax 다뤕기
   - 상세조회 기존 댓글목록
   - 댓글 입력하면 새로작성한 댓글 추가
   - 댓글용 테이블