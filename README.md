
    
## 주제 
게시판 프로젝트
## 목적  
Spring과 관련된 기술(SpringBoot, Spring Data JPA, Spring Security)의 숙련도를 높이고</br> 
TDD(테스트 주도 개발)을 중심으로 개발 습관을 가지기 위해 진행한 게시판 프로젝트
    
## 기술(Tech Stack)
    
-   SpringBoot, Spring Data JPA, Spring Security
-   Lombok, jet
-   test : Junit5, Assertj
-   문서화 : Spring Rest Docs

## 자동화 API 문서
(프로젝트 전 API 명세)
https://www.notion.so/API-Docs-82d56fcd92074e2bb970698fdca74a54 <br/>
Rest Docs를 이용한 API 문서는 추후 추가 예정


## 프로젝트의 회고
    
-   간단히 도메인을 분석하고 API 명세를 작성하고 개발을 진행하였으나 도메인에 대한 이해 및 확실성 부족으로 중간중간 도메인의 변경이 매우 많이 되었습니다. 이로 인해서 테스트코드 역시 많이 변경되었고 불필요한 작업을 많이 했던 것 같습니다. 따라서 저는 이 프로젝트를 통해서 해당 도메인에 대한 분석 및 설계의 중요성을 깨닫게 되었습니다. 
	-  하지만 변경이 많았던 만큼 많은 시행착오를 겪으며 Spring의 기술들을 숙달하는 시간 만큼은 확실히 된 것 같습니다.
-   이 프로젝트는 TDD를 기반으로 진행되었으며 `실패하는 테스트 → 구현 → 통과하는 테스트 → 리팩터링` 과정을 거치며 진행되었습니다. 이 프로젝트를 통해 TDD의 이점과 중요성, 테스트에 대한 이해를 좀 더 높일 수 있었던 것 같습니다..
