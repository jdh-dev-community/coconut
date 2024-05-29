# Coconut. - AI 가상 면접 및 커뮤니티 서비스

## Summary

서비스 링크 : https://coconutapp.co.kr (테스트 id: `admin@gmail.com` // 테스트 pw: `Qwer1234#!`) <br />
서비스 히스토리 : [https://iwsaitw.tistory.com/category/운영 중인 서비스/Coconut.](https://iwsaitw.tistory.com/category/%EC%9A%B4%EC%98%81%20%EC%A4%91%EC%9D%B8%20%EC%84%9C%EB%B9%84%EC%8A%A4/Coconut.)

![Screenshot 2024-05-17 at 3 57 2](https://github.com/jdh-dev-community/coconut/assets/77978026/d773fd2f-95e4-4457-b109-ad89cf4c80c3)


### 기술 스택

- 설계: 점진적으로 MSA 구축 중 (현재 4개의 서비스 + 1개 프록시서버 구성), 멀티 모듈로 서비스 관리
- 서비스: Spring Boot, Java, JPA, QueryDSL, Spring Security, Swagger
- 데이터베이스: MySQL, Redis
- 인프라: Nginx, Docker, AWS (ec2, route53, s3, ecr, codepipeline, rds, cloudwatch ...)
- 프론트: Nextjs, Typescript

<br>
<br>

## 목차

1. 서비스 설계 다이어그램
2. 인프라 아키텍쳐 다이어그램
3. API 설계 (스웨거)
4. 폴더 구조
5. 데이터베이스 ERD
6. 통합 테스트
7. 프로젝트 진행 중 고려사항

<br>
<br>
<br>
<br>

## 1. 서비스 설계 다이어그램

클라이언트 측의 요청을 Nginx로 구축된 Reverse Proxy에서 전달 받습니다. <br />
이후 인증이 필요한 endpoint의 경우에는 인증서버를 거쳐서 개별 서비스로 전달 되도록 설계되었습니다. <br />

최초에 커뮤니티 서비스를 배포하고 이후에 점진적으로 퀴즈 서비스, 유저 & 인증 서비스를 추가하는 방식으로 구현하였습니다.

![4](https://github.com/jdh-dev-community/coconut/assets/77978026/f73cb060-b1c9-4308-831b-96b38af84f23)

<br>
<br>

## 2. 인프라 구성

프리티어 계정 여러 개를 사용하여 MSA 환경을 구축하고 있습니다. <br />
NAT 게이트웨이 사용시 비용발생의 이슈가 있어 서버를 public 공간에 위치시켰습니다. <br />

AWS의 CodePipeline을 사용하여 CI/CD를 구축하였고, <br />
Cloud Watch를 사용하여 서버에 문제가 있는 경우 discord에 알림을 받도록 설정하였습니다.<br />

<br />

서비스를 점진적으로 추가하였기 때문에 운영 중인 서비스의 배포 상태를 유지하면서 인프라 추가를 진행하였습니다.<br />
블로그: [무중단 배포 유지하면서 서비스 추가하기](https://iwsaitw.tistory.com/entry/%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC-%EC%9C%A0%EC%A7%80%ED%95%98%EB%A9%B4%EC%84%9C-EC2-%EA%B5%90%EC%B2%B4-%ED%95%98%EA%B8%B0)

![5](https://github.com/jdh-dev-community/coconut/assets/77978026/d94722a6-d1fa-477e-99cf-a5afb149bfad)

<br>
<br>

### 3. API 문서 (스웨거)

커뮤니티 서비스 스웨거 (개발환경) : [http://devapi.coconutapp.co.kr/coconut-docs/community/ui](http://devapi.coconutapp.co.kr/coconut-docs/community/ui) <br />
퀴즈 서비스 스웨거 (개발환경) : [http://devapi.coconutapp.co.kr/coconut-docs/quiz/ui](http://devapi.coconutapp.co.kr/coconut-docs/quiz/ui)

![Group 205](https://github.com/jdh-dev-community/coconut/assets/77978026/fb736183-3b21-4499-831f-dcfa5f2eead0)

<br>
<br>

### 4. 폴더 구조

전체 서비스는 멀티 모듈 형태로 구성하였습니다.
개별 서비스와 서비스 간에 공유되는 코드를 하나의 모듈로 만들어서 사용하고 있습니다. <br>

블로그: [왜 멀티 모듈 프로젝트로 변경하려고 하는가?](https://iwsaitw.tistory.com/entry/%EC%99%9C-%EB%A9%80%ED%8B%B0%EB%AA%A8%EB%93%88-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EB%A1%9C-%EB%B3%80%EA%B2%BD%ED%95%98%EB%A0%A4%EA%B3%A0-%ED%95%98%EB%8A%94%EA%B0%80)

![Group 207](https://github.com/jdh-dev-community/coconut/assets/77978026/3a76e2e6-b768-4a21-8a94-3548aa8d9184)

<br>
<br>

## 5. 데이터베이스 ERD

커뮤니티 서비스 / 퀴즈 서비스 / 유저 서비스가 각각 데이터베이스를 가지고 있습니다. <br />
<br />
현재 커뮤니티 서비스의 테이블 설계는 조회 쿼리에서 성능이 떨어진다는 것을 확인하였습니다. <br />
테이블 설계에 따라 조회와 쓰기에서 어떤 성능 차이가 발생하는지 확인해보았습니다. <br />
블로그: [쓰기에 시간을 쓸 것인가? 읽기에 시간을 쓸 것인가?](https://iwsaitw.tistory.com/entry/%EC%93%B0%EA%B8%B0%EC%97%90-%EC%8B%9C%EA%B0%84%EC%9D%84-%EC%93%B8-%EA%B2%83%EC%9D%B8%EA%B0%80-%EC%9D%BD%EA%B8%B0%EC%97%90-%EC%8B%9C%EA%B0%84%EC%9D%84-%EC%93%B8-%EA%B2%83%EC%9D%B8%EA%B0%80)

![6](https://github.com/jdh-dev-community/coconut/assets/77978026/0dcef0a5-2c10-4c8b-aae4-343c867547c5)

<br>
<br>

## 6. 통합 테스트

테스트 데이터베이스를 사용하고, mockMvc를 사용하여 api 콜을 모방하여 테스트를 진행하였습니다. (퀴즈 서비스 / 커뮤니티 서비스) <br />
블로그: [멀티 쓰레드 환경에서 조회수 증가 검증](https://iwsaitw.tistory.com/entry/%EB%A9%80%ED%8B%B0-%EC%93%B0%EB%A0%88%EB%93%9C-%ED%99%98%EA%B2%BD%EC%97%90%EC%84%9C-%EA%B2%8C%EC%8B%9C%EA%B8%80-%EC%A1%B0%ED%9A%8C%EC%88%98-%EC%A6%9D%EA%B0%80%EC%97%90-%EB%8C%80%ED%95%9C-%EA%B2%80%EC%A6%9D)

![Group 206](https://github.com/jdh-dev-community/coconut/assets/77978026/58102c2e-05a6-4f9f-8be4-b5feba664451)

<br>
<br>

<br>
<br>

## 7. 프로젝트 진행 중 고려사항

<br>

### 쓰기에 시간을 쓸 것인가? 읽기에 시간을 쓸 것인가? [블로그](https://iwsaitw.tistory.com/entry/%EC%93%B0%EA%B8%B0%EC%97%90-%EC%8B%9C%EA%B0%84%EC%9D%84-%EC%93%B8-%EA%B2%83%EC%9D%B8%EA%B0%80-%EC%9D%BD%EA%B8%B0%EC%97%90-%EC%8B%9C%EA%B0%84%EC%9D%84-%EC%93%B8-%EA%B2%83%EC%9D%B8%EA%B0%80) <br />

게시글과 댓글을 갖는 테이블 설계 시에 쓰기에 유리한 설계와 읽기에 유리한 설계를 각각 작성하고,
데이터 크기에 따라서 성능 차이가 얼마나 발생하는지 확인해보았습니다.
![8](https://github.com/jdh-dev-community/coconut/assets/77978026/b3ee9006-13b5-428f-a718-24794b3a0b63)

<br />
<br />

### 내 서버는 얼마나 버틸 수 있는가? [블로그](https://iwsaitw.tistory.com/entry/%EB%82%B4-%EC%84%9C%EB%B2%84%EB%8A%94-%EC%96%BC%EB%A7%88%EB%82%98-%EB%B2%84%ED%8B%B8-%EC%88%98-%EC%9E%88%EB%8A%94%EA%B0%80) <br />

서버 배포 이후 특정 경우에 cpu 사용량이 증가하면서 서버가 죽는 경험을 하였습니다. <br />
아파치 벤치마크를 사용하여 요청 규모에 따른 cpu 사용율을 확인하여보았습니다.
![9](https://github.com/jdh-dev-community/coconut/assets/77978026/a77a101d-57f5-407d-bd78-302ef61041eb)

<br />
<br />

### 무중단 배포 유지하면서 EC2 교체하기 [블로그](https://iwsaitw.tistory.com/entry/%EB%AC%B4%EC%A4%91%EB%8B%A8-%EB%B0%B0%ED%8F%AC-%EC%9C%A0%EC%A7%80%ED%95%98%EB%A9%B4%EC%84%9C-EC2-%EA%B5%90%EC%B2%B4-%ED%95%98%EA%B8%B0)

서비스를 점진적으로 추가하다보니 이미 운영 중인 서비스를 유지하면서 새로운 인프라 추가가 필요하였습니다. <br />
커뮤니티 서비스를 유지하면서 앞단에 Reverse proxy 서버를 추가하는 과정을 정리하였습니다. <br />

![10](https://github.com/jdh-dev-community/coconut/assets/77978026/9f4a0ac9-4b70-49ce-98d6-c26ff10d4bf9)

<br />
<br />

### 커다란 만능 모듈을 사용하면 만날 수 있는 함정 [블로그](https://iwsaitw.tistory.com/entry/%EC%BB%A4%EB%8B%A4%EB%9E%80-%EB%A7%8C%EB%8A%A5-%EB%AA%A8%EB%93%88%EC%9D%84-%EC%82%AC%EC%9A%A9%ED%95%98%EB%A9%B4-%EB%A7%8C%EB%82%A0-%EC%88%98-%EC%9E%88%EB%8A%94-%ED%95%A8%EC%A0%95)

처음에 서비스 간에 공유되는 파일을 한 개의 global 모듈에서 관리하였습니다. <br />
서비스가 추가될 수록 global 모듈이 커졌고, 불필요한 의존성들이 모든 서비스에 추가되는 상황이 발생하였습니다. <br />
global 모듈을 관심사 별로 분리하여 필요한 기능만 서비스에서 가져다 사용할 수 있도록 변경하는 과정을 기록하였습니다. <br />

![11](https://github.com/jdh-dev-community/coconut/assets/77978026/6d16f97e-c56b-4510-bc18-abc45313ace6)

<br />
<br />

### 다양한 Oauth2 Provider를 다룰 수 있는 코드 만들기 [블로그](https://iwsaitw.tistory.com/entry/%EB%8B%A4%EC%96%91%ED%95%9C-Oauth2-Provider-%EB%93%A4%EC%9D%84-%EB%8B%A4%EB%A3%B0-%EC%88%98-%EC%9E%88%EB%8A%94-%EC%BD%94%EB%93%9C-%EB%A7%8C%EB%93%A4%EA%B8%B0)

Oauth 로그인을 구현하면서 다양한 케이스를 if else로 반복하는 코드를 만들게 되었습니다. <br />
Oauth 같은 경우에는 다양한 Provieder를 사용하게 되기 때문에 충분한 확장 가능성이 있다고 생각하고 팩토리 패턴을 사용하여 <br />
보다 확장에 유연한 코드로 개선하는 과정을 정리하였습니다.
![12](https://github.com/jdh-dev-community/coconut/assets/77978026/a80547fb-e2ae-4e5c-92cb-d9c0ead4f79d)

<br />
<br />
