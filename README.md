# spring-study-holmes
# 프로젝트 스터디 홈즈
![image](https://user-images.githubusercontent.com/51532254/166196523-d80438c4-753b-4609-a156-cea16f72cb54.png)
 
#### 프로젝트 이름(project name) : spring-study-holmes 

#### 프로젝트 기간 : 2022.04.21 ~ 2022.05.04

#### 프로젝트 인원 : 4명

## Description

([MVC2](https://github.com/ckcks1997/project-study-holmes)방식으로 제작중인 프로젝트를 스프링 방식으로 전환하여 진행한 프로젝트입니다.)

스터디 홈즈는 온/오프라인 스터디의 개최 및 참여, 커뮤니티 서비스를 제공하는 프로젝트 입니다.




### 특징

- 스터디 메뉴는 다양한 스터디 종류를 포함할 수 있게 여러 카테고리로 나뉘어져 있습니다.
 
- 오프라인 스터디의 경우 카카오 지도를 사용하여 대략적인 스터디 위치를 확인할 수 있습니다.

- 개최되어 있는 스터디에 참가시 해당 스터디를 개최한 유저에게 스터디 초대 수락을 묻는 알림이 전송됩니다.

- 스터디 요청을 수락할 경우 해당 스터디 그룹에 초대되며, 그룹원끼리 채팅 및 전용 게시판을 할 수 있습니다.
 
- 스터디가 끝나면 스터디원들의 평가를 할 수 있으며, 추후 스터디 개최/참여시 다른 사용자가 해당 평가정보를 확인할 수 있습니다.

- 질문, 정보공유가 가능한 커뮤니티 게시판이 있습니다.

* 더 자세한 기능 설명은 PPT를 참고해주세요.

  [PPT 링크](https://www.canva.com/design/DAE_Jgz1g40/Quzdh2Dn4AteLbHwDglliQ/view?utm_content=DAE_Jgz1g40&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)


Link
-------------

[초기 프론트 모델링](https://www.figma.com/file/AJvPw9I1fnItPkgoqvgpc2/%EC%8A%A4%ED%84%B0%EB%94%94-%ED%99%88%EC%A6%88?node-id=0%3A1)

[DB 모델링](https://whimsical.com/DbNmDSbumcGBr57HA7uY4N)

구현된 기능
------------
- 세션을 이용한 로그인 기능 + 카카오 로그인
- 스터디 개최/참여
- 유저 평판 기능(스터디 종료 후 평가 가능)
- 스터디 그룹내 게시판 및 채팅 기능
- 커뮤니티 게시판

사용된 기술
-------------

#### 모델링 툴 : Figma, Whimsical
#### IDE/Editor : Eclipse, VSCode
#### 웹서버 : Apache Tomcat 9.0
#### DB : Oracle
#### 개발 패턴 : Spring MVC
#### 빌드 툴 : Maven
#### 사용 언어 : HTML, CSS, Javascript, Java, JSP
#### 프론트 프레임워크/라이브러리 : Bootstrap 4, jQuery, SummerNote
#### 백엔드 프레임워크/라이브러리 : Spring 5.3, MyBatis
#### 사용 API : 카카오 API(지도, 로그인)
#### 협업 : Github

Image
=========
### ERD
<img width="764" alt="image (2)" src="https://user-images.githubusercontent.com/51532254/166196674-1d83c57a-f860-492f-abd3-36ab4a532f95.png"><br/><br/>

### 메뉴 구조도
<img width="764" alt="image (3)" src="https://user-images.githubusercontent.com/51532254/166196897-df3f903b-4fed-4127-8b39-d8522c3fa113.jpg"><br/><br/>

Screenshot
=========

 메인화면
-------------
 
<img width="764" alt="image (5)" src="https://user-images.githubusercontent.com/51532254/166634936-6b8f2c8a-7f8f-4676-a368-d3373de76896.jpg"><br/><br/>


 스터디 메뉴
-------------
<img width="764" alt="image (5)" src="https://user-images.githubusercontent.com/51532254/166197005-64e754b8-c6c8-4163-b275-910bacac7145.jpg">
<img width="764" alt="image (6)" src="https://user-images.githubusercontent.com/51532254/166197013-fbd57ad2-2b93-42d4-92b9-3bc98c4416b6.jpg">
<img width="764" alt="image (7)" src="https://user-images.githubusercontent.com/51532254/166197016-3b6f400f-bdc9-47af-9064-ea08e35f4002.jpg"><br/><br/>

유저 정보
-------------
<img width="764" alt="image (8)" src="https://user-images.githubusercontent.com/51532254/166197018-6b07c6ef-0774-4cb2-9c58-591c35f1906e.jpg"><br/><br/>

 커뮤니티
-------------
<img width="764" alt="image (9)" src="https://user-images.githubusercontent.com/51532254/166197329-f907cd66-f286-48d6-956e-03ac94cd561c.jpg">
<img width="764" alt="image (10)" src="https://user-images.githubusercontent.com/51532254/166197618-5facb7da-d5a7-4c9b-8574-b575d8da953d.jpg"><br/><br/>

알림 기능
-------------
<img width="764" alt="image (10)" src="https://user-images.githubusercontent.com/51532254/166636511-e8eb8114-6984-4ff2-85cf-e20235469f46.jpg"><br/><br/>


그룹 기능
-------------

<img width="764" alt="image (11)" src="https://user-images.githubusercontent.com/51532254/166197848-4b98eb0e-324a-4c07-8250-336cd2798ed8.jpg">
<img width="764" alt="image (12)" src="https://user-images.githubusercontent.com/51532254/166197966-7f31cc2e-aa08-4be2-a2da-a754ebc05433.png">
<img width="764" alt="image (13)" src="https://user-images.githubusercontent.com/51532254/166198413-e20da1f9-8538-4607-9bbb-51c5770a5cec.png">
<img width="764" alt="image (14)" src="https://user-images.githubusercontent.com/51532254/166198729-024bee3e-711b-4c7d-a594-0cc0a52cd406.jpg">
<img width="764" alt="image (15)" src="https://user-images.githubusercontent.com/51532254/166198922-730f811b-6a7e-4199-b646-9016068f483c.png"><br/><br/>

