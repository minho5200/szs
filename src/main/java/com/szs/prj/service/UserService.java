package com.szs.prj.service;

import com.szs.prj.advice.exception.CDuplicatedException;
import com.szs.prj.advice.exception.CInvalidException;
import com.szs.prj.advice.exception.CNotExistUserException;
import com.szs.prj.compo.JwtCompo;
import com.szs.prj.dto.*;
import com.szs.prj.entity.*;
import com.szs.prj.repository.*;
import com.szs.prj.util.TaxCalculatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtCompo jwtCompo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserTaxationInfoRepository userTaxationInfoRepository;
    private final PensionRepository pensionRepository;
    private final CreditCardDeductionMonthRepository creditCardDeductionMonthRepository;
    private final CreditCardDeductionRepository creditCardDeductionRepository;

    public boolean signUp(SignUpReqDto dto) {
        //암호화 처리
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        dto.setEncRegNo();

        //조회시 user가 이미 존재하면 exception
        if (userRepository.findById(dto.getUserId()).isPresent()) {
            throw new CDuplicatedException();
        }

        User user = User.builder().userId(dto.getUserId()).password(dto.getPassword()).name(dto.getName()).regNo(dto.getRegNo()).build();

        userRepository.save(user);
        return true;
    }

    public LoginResDto login(LoginReqDto dto) {
        // 정보 조회를 시도
        User user = userRepository.findById(dto.getUserId()).orElseThrow(CNotExistUserException::new);

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            // 패스워드가 맞지 않을때
            throw new CInvalidException();
        }

        return new LoginResDto(jwtCompo.createToken(user.getUserId()));
    }

    @Transactional
    public boolean saveUserTaxaionInfo(UserTaxationInfoDto data, String userId) {
        String userName = data.getName();
        long totalIncome = data.getTotalIncome();
        IncomeDeductionsDto incomeDeductionsDto = data.getIncomeDeductions();
        String taxDeduction = incomeDeductionsDto.getTaxDeduction(); // 세액공제
        List<PensionDto> pensions = incomeDeductionsDto.getPensions(); // 국민연금
        CreditCardIncomDeductionDto creditCardIncomDeductionDto = incomeDeductionsDto.getCreditCardIncomeDeduction(); //신용카드
        int creditYear = creditCardIncomDeductionDto.getYear(); //신용카드 연도
        List<Map<String, String>> creditMonthDatas = creditCardIncomDeductionDto.getMonth(); //신용카드의 각월 값

        // UserTaxationInfo 조회
        Optional<UserTaxationInfo> userTaxationInfoOp = userTaxationInfoRepository.findByUserIdAndTaxationYear(userId, creditYear);
        UserTaxationInfo userTaxationInfo;

        if (userTaxationInfoOp.isPresent()) {
            throw new CDuplicatedException();
        } else {
            // 새로운 UserTaxationInfo 생성
            userTaxationInfo = UserTaxationInfo.builder()
                    .totalIncome(totalIncome)
                    .name(userName)
                    .taxDeduction(taxDeduction)
                    .userId(userId)
                    .taxationYear(creditYear).build();
        }

        userTaxationInfoRepository.save(userTaxationInfo);

        List<Pension> pensionList = pensions.stream().map(
                dto -> Pension.builder()
                        .deductionAmount(dto.getDeductionAmount())
                        .month(dto.getMonth())
                        .userTaxationInfo(userTaxationInfo).build()
        ).toList();

        pensionRepository.saveAll(pensionList);

        CreditCardDeduction creditCardDeduction = CreditCardDeduction.builder()
                .userTaxationInfo(userTaxationInfo)
                .year(creditYear).build();

        creditCardDeductionRepository.save(creditCardDeduction);

        List<CreditCardDeductionMonth> creditCardDeductionMonths = creditMonthDatas.stream().flatMap(dataMap -> dataMap.entrySet().stream())
                .map(entry -> CreditCardDeductionMonth.builder()
                        .month(entry.getKey())
                        .amount(entry.getValue())
                        .creditCardDeduction(creditCardDeduction).build())
                .toList();

        creditCardDeductionMonthRepository.saveAll(creditCardDeductionMonths);

        return true;
    }

    public long createDecisionTaxAmount(User user) {
        UserTaxationInfo userTaxationInfo = userTaxationInfoRepository.findByUserId(user.getUserId()).orElseThrow(CNotExistUserException::new);
        List<Pension> pensionList = pensionRepository.findByUserTaxationInfoId(userTaxationInfo.getId());
        CreditCardDeduction creditCardDeduction = creditCardDeductionRepository.findByUserTaxationInfoId(userTaxationInfo.getId()).orElseThrow(CNotExistUserException::new);
        List<CreditCardDeductionMonth> creditCardDeductionMonthList = creditCardDeductionMonthRepository.findByCreditCardDeductionId(creditCardDeduction.getId());

        return TaxCalculatorUtil.processTax(userTaxationInfo, pensionList, creditCardDeductionMonthList);
    }

}
