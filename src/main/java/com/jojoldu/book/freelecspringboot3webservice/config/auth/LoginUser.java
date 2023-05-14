package com.jojoldu.book.freelecspringboot3webservice.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // 1. 이 어노테이션이 생설될 수 있는 위치 지정 -> Parameter
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser { // 2. @interface: 이 파일을 어노테이션 클래스로 지정, LoginUser라는 이름을 가진 어노테이션이 생성되었다.
}
