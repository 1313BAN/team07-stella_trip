# 프로젝트 개요
이 프로젝트는 여행 계획과 별자리를 엮는 컨셉을 가진 웹 프로젝트입니다.
사용자는 여행지들을 하나의 별로 생각하고, 자신이 세운 여행 계획을 기반으로 별자리 카드를 직접 만들고 공유 가능합니다.
현재 openai key 토큰 부족으로 몇개 기능이 작동하지 않습니다.

https://stellatrip.duckdns.org/


## 프로젝트 인원
| <img src="https://avatars.githubusercontent.com/u/128668023?v=4" width="120" height="120"/> | <img src="https://avatars.githubusercontent.com/u/63947262?v=4" width="120" height="120"/> |
| :-------------------------------------------------------------------------------------------------------------------:  | :----------------------------------------------------------------------------------------: |
|                                        [한승연](https://github.com/leve68) |                                                                   [강승구](https://github.com/luna156)                            |


## 기능들

### 여행지 조회
https://www.data.go.kr/data/15101578/openapi.do
공공데이터로 존재하는 한국관광공사_국문 관광정보 서비스_GW를 이용하였습니다. 사용자는 이 데이터를 기반으로 여행지 조회가 가능합니다.

![](https://velog.velcdn.com/images/luna156/post/4ce548b1-c776-4332-a805-6c65e5941a2b/image.png)


### 여행 계획 공유 및 조회
다른 사람들이 공개를 허용한 여행 계획을 볼 수 있습니다.

![](https://velog.velcdn.com/images/luna156/post/253df8a6-3436-4ef1-8610-381e6f204f3f/image.png)


### 여행 계획 생성
사용자가 여행지 계획을 자유롭게 세울 수 있습니다. 날짜별로 구분되며, 여행지 방문 순서를 변경 가능합니다. 여러 사용자가 동시 조회는 할 수 있지만, 수정은 한사람만 가능하도록 구현하였습니다.

![](https://velog.velcdn.com/images/luna156/post/5bec0368-bdac-42d3-9de2-36fac9b2b837/image.png)


### 여행 계획으로 별자리 생성
여행지들을 기반으로 사용자가 자유롭게 별자리를 생성 가능합니다. 공유하기를 누르면, 사용자의 여행 계획을 기반으로 ai가 여행 문구 및 키워드를 생성해줍니다. 현재 api 키의 토큰 부족으로 작동하진 않습니다.

![](https://velog.velcdn.com/images/luna156/post/2a418d4d-2375-4fb7-a8d8-1b9155d151a9/image.png)


### 만든 별자리 카드 공유
자신이 만든 여행 별자리 카드를 링크로 공유 가능합니다.

![](https://velog.velcdn.com/images/luna156/post/1e4309f3-10b5-4cc8-9f32-2445ad1401b0/image.png)


### 지역별 채팅방 기능
지역별로 실시간 채팅방을 구현하여 사용자들간에 정보를 실시간으로 교류 가능하도록 했습니다.

![](https://velog.velcdn.com/images/luna156/post/6e0611e9-4928-4020-a473-afb8ee0c7760/image.png)



## 프로젝트 시연 영상
<iframe width="853" height="480" src="https://www.youtube.com/embed/8d38RJrhQ9o" title="stellatrip 시연영상" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerpolicy="strict-origin-when-cross-origin" allowfullscreen></iframe>
