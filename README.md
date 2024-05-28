# Coconut. - AI 가상 면접 및 커뮤니티 서비스

<br>

- 서비스 링크 : https://coconutapp.co.kr
- 블로그 링크 : [https://iwsaitw.tistory.com/category/운영 중인 서비스/Coconut.](https://iwsaitw.tistory.com/category/%EC%9A%B4%EC%98%81%20%EC%A4%91%EC%9D%B8%20%EC%84%9C%EB%B9%84%EC%8A%A4/Coconut.)
- 깃헙 링크 : https://github.com/devsince2021

<br>

## 목차

1. 서비스 설계 다이어그램
2. 인프라 아키텍쳐 다이어그램
3. 데이터베이스 ERD
4. Junit5 테스트
5. 프로젝트 진행 중 만난 이슈 사항들

<br>
<br>

### 1. 서비스 설계 다이어그램

클라이언트 측의 요청을 Nginx로 구축된 Reverse Proxy에서 전달 받습니다. <br />
이후 인증이 필요한 endpoint의 경우에는 인증서버를 거쳐서 개별 서비스로 전달 되도록 설계되었습니다.

![4](https://github.com/jdh-dev-community/coconut/assets/77978026/f73cb060-b1c9-4308-831b-96b38af84f23)

<br>
<br>

### 2. 인프라 구성

프리티어 계정 여러 개를 사용하여 MSA 환경을 구축하고 있습니다. <br />
NAT 게이트웨이 사용시 비용발생의 이슈가 있어 서버를 public 공간에 위치시켰습니다. <br />

AWS의 CodePipeline을 사용하여 CI/CD를 구축하였고, <br />
Cloud Watch를 사용하여 서버에 문제가 있는 경우 discord에 알림을 받도록 설정하였습니다.

![5](https://github.com/jdh-dev-community/coconut/assets/77978026/d94722a6-d1fa-477e-99cf-a5afb149bfad)

<br>
<br>

### 3. API 문서 (스웨거)

<br>
<br>

### 4. 데이터베이스 ERD

커뮤니티 서비스 / 퀴즈 서비스 / 유저 서비스가 각각 데이터베이스를 가지고 있습니다.

![6](https://github.com/jdh-dev-community/coconut/assets/77978026/0dcef0a5-2c10-4c8b-aae4-343c867547c5)

![7](https://github.com/jdh-dev-community/coconut/assets/77978026/03289dd5-2ab3-4f73-9db6-72350c1b78a8)
![8](https://github.com/jdh-dev-community/coconut/assets/77978026/b3ee9006-13b5-428f-a718-24794b3a0b63)
![9](https://github.com/jdh-dev-community/coconut/assets/77978026/a77a101d-57f5-407d-bd78-302ef61041eb)
![10](https://github.com/jdh-dev-community/coconut/assets/77978026/9f4a0ac9-4b70-49ce-98d6-c26ff10d4bf9)
![11](https://github.com/jdh-dev-community/coconut/assets/77978026/6d16f97e-c56b-4510-bc18-abc45313ace6)
![12](https://github.com/jdh-dev-community/coconut/assets/77978026/a80547fb-e2ae-4e5c-92cb-d9c0ead4f79d)

- 쓰기에 시간을 쓸 것인가? 읽기에 시간을 쓸 것인가?
- 내 서버는 얼마나 버틸 수 있는가?
- 무중단 배포 유지하면서 EC2 교체하기
- 커다란 만능 모듈을 사용하면 만날 수 있는 함정
- 다양한 Oauth2 Provider를 다룰 수 있는 코드 만들기
