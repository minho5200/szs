package com.szs.prj.advice;

import com.szs.prj.advice.exception.*;
import com.szs.prj.dto.ResExceptionDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {
    private void detailErrorPrinter(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        System.err.println(stackTrace);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> unExpectException(HttpServletRequest req, Exception e){
        this.detailErrorPrinter(e);
        ResExceptionDto result = new ResExceptionDto();
        result.setCode("E999");
        result.setMsg("서버 에러");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CEncDecFailException.class)
    protected ResponseEntity<?> cEncDecFailException(HttpServletRequest req, Exception e){
        ResExceptionDto result = new ResExceptionDto();
        result.setCode("E001");
        result.setMsg("암호화/복호화에 실패하였습니다.");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CNotExistUserException.class)
    protected ResponseEntity<?> cNotExistUserException(HttpServletRequest req, Exception e){
        ResExceptionDto result = new ResExceptionDto();
        result.setCode("E002");
        result.setMsg("존재하지 않는 유저정보입니다.");
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CInvalidException.class)
    protected ResponseEntity<?> cNotInvalidException(HttpServletRequest req, Exception e){
        ResExceptionDto result = new ResExceptionDto();
        result.setCode("E003");
        result.setMsg("요청정보가 올바르지 않습니다.");
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CDuplicatedException.class)
    protected ResponseEntity<?> cDuplicatedException(HttpServletRequest req, Exception e){
        ResExceptionDto result = new ResExceptionDto();
        result.setCode("E004");
        result.setMsg("중복된 요청입니다.");
        return new ResponseEntity<>(result, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CNotExistDataException.class)
    protected ResponseEntity<?> cNotExistDataException(HttpServletRequest req, Exception e){
        ResExceptionDto result = new ResExceptionDto();
        result.setCode("E005");
        result.setMsg("해당 데이터가 존재하지 않습니다.");
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
}
