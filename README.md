# 1. 구현 내용
## 1) 회원가입 (/szs/signup)
- userId, password, name, regNo 의 키를 갖는 json데이터를 받아 jpa를 통해 h2에 저장합니다.
- regNo에만 주민등록번호 형태의 데이터 검증을 한번 더 합니다.
- password는 데이터를 받는 즉시 암호화 하며, regNo는 검증 이후 서비스로직으로 넘기기전에 암호화합니다.
- 서비스로직에서 userId를 통해 중복가입 확인을 합니다.
- Security의 Filter로직을 거치지 않고 진행합니다.
- 성공시 200, true를 리턴합니다.

## 2) 로그인 (/szs/login)
- userId, password 의 키를 갖는 json데이터를 받습니다.
- password는 데이터를 받는 즉시 암호화 합니다.
- 서비스로직에서 userId를 통해 존재유무를 체크하며 (409) , 존재하면 이후 패스워드 검증 (400) 을 합니다.
- Security의 Filter로직을 거치지 않고 진행합니다.
- 성공시 200, accessToken 의 키를 가지며 jwt token을 리턴합니다.

## 3) 스크래핑 (/szs/scrap)
- 2에서 받은 데이터를 header에 담아 요청(post)을 합니다. (body는 필요x)
- jwt에 포함된 계정정보 (userId)를 가져옵니다.
- userId를 통해 계정의 세부사항으로 WebClient를 통해 외부 API에서 데이터를 가져옵니다.
- 해당 API요청에 따라 성공시 서비스로직으로 넘깁니다.
- 이미 존재하는 데이터의경우 (id, year 로 구분) (409), 존재하지 않는 새로운 데이터라면 구분지어 Entity 4개로 쪼개서 저장합니다.
- 성공시 200, true를 리턴합니다.

## 4) 결정세액 (/szs/refund)
- 2에서 받은 데이터를 header에 담아 요청(get)을 합니다. (body는 필요x)
- jwt에 포함된 계정정보 (userId)를 가져옵니다.
- userId를 통해 계정의 세부사항을 서비스로직에 넘깁니다.
- 3에서 저장된 데이터들을 조회하며, 각 데이터들로 요청자의 세액을 계산합니다.
- 성공시 200, 결정세액의 키를 갖는 데이터를 리턴합니다.

# 2. 검증 및 테스트
## 1) swagger
- signup, login 이후에 swagger의 token을 Authorize에 기입하고 scrap, refund 테스트 바랍니다.
- header를 각각 컨트롤러에서 검증하는 것이 아닌 filter에서 검증하고 있으므로 swagger의 파라미터엔 표기가 되지 않습니다.
- 각각 순서를 다르게 요청하거나 트러블 슈팅 하는 경우 ExceptionHandler를 통해 핸들링하고 있습니다. 미처 놓친에러의 경우 400을 뱉습니다.
## 2) Test
- 대표 기능에 대한 테스트만 있으며, 정상의 경우만 코딩하였습니다.
