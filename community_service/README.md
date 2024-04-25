# Coconut. (코코넛.)

### 프로젝트 개요

- 프로젝트 이름: Coconut.
- 프로젝트 목표: 개발자와 개발자가 되고 싶은 사람들이 정보를 공유하는 곳!

### 기술 스택

- 인프라: AWS EC2, RDS, Elastic Cache, SNS, Lamda, CloudWatch
- 애플리케이션 개발: Spring boot 2.7.18, JPA
- 데이터베이스: MySQL, Redis
- 배포 및 문서화: Docker, Github Action, Swagger
- 테스트: Junit

### 설계 문서

- 시스템 구성도

  - v0.1
    ![Screenshot 2024-03-04 at 10 33 18 PM](https://github.com/jdh-dev-community/community_spring/assets/77978026/323492a8-ab92-4cde-8b5b-174db6c8f7e9)

<br />

- 데이터베이스 설계
  - v0.1
    ![Screenshot 2024-03-12 at 9 42 37 PM](https://github.com/jdh-dev-community/community_spring/assets/77978026/1e28fd1a-6532-4757-a7ac-29e610e00ba7)

<br />
    
- 시퀀스 다이어그램
  - 게시글 수정
    ![게시물 수정](https://github.com/jdh-dev-community/community_spring/assets/77978026/4c0e2583-3ef6-482b-8ded-c7cf3b030b3b)

  - 게시글 상세 조회
    ![게시물 상세](https://github.com/jdh-dev-community/community_spring/assets/77978026/7c2ae285-c1f8-4b0a-af98-77ee4f32ad2a)

  - 게시글 목록 조회
    ![게시물 조회](https://github.com/jdh-dev-community/community_spring/assets/77978026/eba52bff-ba31-4ab9-8243-8c5842e7e75c)

<br />

- rest api 명세
  ![Screenshot 2024-03-05 at 9 13 44 AM](https://github.com/jdh-dev-community/community_spring/assets/77978026/ca760ffe-0a0e-486d-9bbe-7c5076bd7998)

<br />

- **스프린트1 - mvp 출시**
  <img width="913" alt="Screenshot 2024-03-13 at 9 11 08 PM" src="https://github.com/jdh-dev-community/community_spring/assets/77978026/e03b2262-ca0c-40a0-8b23-e20c20a14c3b">

  - 버전: 0.1 (mvp)
  - 목표: 게시글과 댓글 작성이 가능한 웹 페이지 배포
  - 기획: https://www.figma.com/file/CAXf9vgxHMXZF4iKUqU16b/jdh-team's-team-library?type=design&node-id=2333%3A3&mode=design&t=5MpKQOmsVi1qS9zA-1
  - 디자인 링크: https://www.figma.com/file/CAXf9vgxHMXZF4iKUqU16b/jdh-team's-team-library?type=design&node-id=2333%3A2&mode=design&t=5MpKQOmsVi1qS9zA-1
