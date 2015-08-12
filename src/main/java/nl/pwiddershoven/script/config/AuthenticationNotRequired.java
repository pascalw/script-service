package nl.pwiddershoven.script.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Target({ ElementType.METHOD })
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
public @interface AuthenticationNotRequired {}
