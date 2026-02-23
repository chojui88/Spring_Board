package com.example.demo.repository;

import com.example.demo.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//DB 조작 가능, sql 안써도 조작 가능
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
 //update board_table (테이블 이름) set board_hits = board_hits + 1 where id = ?
    //조회하고자 하는 게시글을  현재 가지고 있는 조회수 하나 증가해서, 조회수 값을 바꾼다, 조건절 반드시 필요
    @Modifying //update, delete 쿼리 쓸때는 이 어노테이션 붙이기
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id")
    /*파라미터 바인딩, 호출시 외부에 전달한 값이 들어감 , 내가 전달한것과 같은 값을 찾아라
    엔티티를 기준으로 쿼리를 작순한다 ,n 은 실제 네이티브 쿼리 ,실제 db에서 쓰는 쿼리
    엔티티 기준으로 b 약으를 쓰는게 필수! boardEntity의 boardhits값을 접근한다
    :id 이 부분은 계속 바뀌는 부분. 여기의 id는 param의 id,
    만약 param의 id가 23 이라면, :id가 23이다.
     */
    void updateHits(@Param("id")Long id);
    //jpa 에서 제공하는 어노테이션, 새 쿼리 만들어야 함 param 사용할 파라미터 이름 지정
}
